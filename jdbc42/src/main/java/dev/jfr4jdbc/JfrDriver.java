package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.DriverContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.interceptor.InterceptorManager;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.ResourceMonitor;
import dev.jfr4jdbc.internal.ResourceMonitorKind;
import dev.jfr4jdbc.internal.ResourceMonitorManager;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author Chihiro Ito
 */
public class JfrDriver implements Driver {

    private static final AtomicInteger labelCounter = new AtomicInteger(0);

    private final AtomicInteger connectionCounter = new AtomicInteger(1);

    private final Driver wrappedDriver;

    private final ResourceMonitor connectionMonitor;

    private final InterceptorFactory interceptorFactory;

    private final String driverLabel;

    JfrDriver(Driver driver) {
        this(driver, InterceptorManager.getDefaultInterceptorFactory(), "Driver#" + labelCounter.incrementAndGet());
    }

    JfrDriver(Driver driver, InterceptorFactory interceptorFactory) {
        this(driver, interceptorFactory, "Driver#" + labelCounter.incrementAndGet());
    }

    JfrDriver(Driver driver, String driverLabel) {
        this(driver, InterceptorManager.getDefaultInterceptorFactory(), driverLabel);
    }

    JfrDriver(Driver driver, InterceptorFactory interceptorFactory, String driverLabel) {

        this.wrappedDriver = driver;
        this.interceptorFactory = interceptorFactory;
        this.driverLabel = driverLabel;

        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor monitor = manager.getMonitor(driverLabel);
        if (monitor == null) {
            monitor = manager.createConnectionMonitor(driver, driverLabel, interceptorFactory);
            manager.addMonitor(monitor);
        }
        this.connectionMonitor = monitor;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return this.wrappedDriver.acceptsURL(url);
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {

        int connectionId = connectionCounter.getAndIncrement();
        DriverContext context = new DriverContext(this.wrappedDriver, url, connectionId);
        Interceptor<DriverContext> interceptor = this.interceptorFactory.createDriverInterceptor();

        Connection delegatedCon = null;
        try {
            this.connectionMonitor.waitAssigningResource();

            interceptor.preInvoke(context);
            delegatedCon = this.wrappedDriver.connect(url, info);
            context.setConnection(delegatedCon, 0);

        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
            this.connectionMonitor.assignedResource();
        }

        return new JfrConnection(delegatedCon, interceptorFactory, this.connectionMonitor, new ConnectionInfo(this.driverLabel, connectionId, 0));
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return this.wrappedDriver.getPropertyInfo(url, info);
    }

    @Override
    public int getMajorVersion() {
        return this.wrappedDriver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        return this.wrappedDriver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return this.wrappedDriver.jdbcCompliant();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.wrappedDriver.getParentLogger();
    }
}