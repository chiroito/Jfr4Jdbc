package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.DriverContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.interceptor.InterceptorManager;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.Label;
import dev.jfr4jdbc.internal.ResourceMonitor;
import dev.jfr4jdbc.internal.ResourceMonitorManager;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * @author Chihiro Ito
 */
public class JfrDriver implements Driver {

    private static final AtomicInteger defaultLabelCounter = new AtomicInteger(0);

    private final AtomicInteger connectionCounter = new AtomicInteger(1);

    private final Driver wrappedDriver;

    private final ResourceMonitor resourceMonitor;

    private final InterceptorFactory interceptorFactory;

    private final String label;

    JfrDriver(Driver driver) {
        this(driver, InterceptorManager.getDefaultInterceptorFactory(), "Driver#" + defaultLabelCounter.incrementAndGet());
    }

    JfrDriver(Driver driver, InterceptorFactory interceptorFactory) {
        this(driver, interceptorFactory, "Driver#" + defaultLabelCounter.incrementAndGet());
    }

    JfrDriver(Driver driver, String label) {
        this(driver, InterceptorManager.getDefaultInterceptorFactory(), label);
    }

    JfrDriver(Driver driver, InterceptorFactory interceptorFactory, String label) {

        this.wrappedDriver = driver;
        this.interceptorFactory = interceptorFactory;
        this.label = label;

        this.resourceMonitor = ResourceMonitorManager.getInstance().getOrCreateResourceMonitor(new Label(label));
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
            this.resourceMonitor.waitAssigningResource();

            interceptor.preInvoke(context);
            delegatedCon = this.wrappedDriver.connect(url, info);
            context.setConnection(delegatedCon, 0);

        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
            this.resourceMonitor.assignedResource();
        }

        return new JfrConnection(delegatedCon, interceptorFactory, this.resourceMonitor, new ConnectionInfo(this.label, connectionId, 0));
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