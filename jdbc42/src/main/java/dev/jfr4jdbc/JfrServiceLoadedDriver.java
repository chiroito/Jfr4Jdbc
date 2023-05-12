package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.DriverContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.ResourceMonitor;
import dev.jfr4jdbc.internal.ResourceMonitorKind;
import dev.jfr4jdbc.internal.ResourceMonitorManager;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class JfrServiceLoadedDriver implements Driver {

    private static final String JFR4JDBC_URL_PREFIX = "jdbc:jfr";
    private static final int JFR4JDBC_URL_PREFIX_LENGTH = JFR4JDBC_URL_PREFIX.length();
    private static final ResourceMonitor connectionMonitor;

    private final AtomicInteger connectionCounter = new AtomicInteger(1);

    private final InterceptorFactory interceptorFactory = InterceptorFactory.getDefaultInterceptorFactory();

    private final Map<String, Driver> driverCache = new HashMap<>(1);

    static {
        try {
            ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
            connectionMonitor = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection).getMonitor("Connection");
            manager.addMonitor(connectionMonitor);
            DriverManager.registerDriver(new JfrServiceLoadedDriver());
        } catch (SQLException e) {
            throw new RuntimeException("Could not register Jfr4Jdbc.", e);
        }
    }

    /**
     * @param url
     * @return
     */
    private static final String getDelegateUrl(String url) {
        String delegateUrl = "jdbc" + url.substring(JFR4JDBC_URL_PREFIX_LENGTH);
        return delegateUrl;
    }

    /**
     *
     */

    public JfrServiceLoadedDriver() {
        super();
    }

    private InterceptorFactory loadInterceptorFactory(String url, Properties info) {
        return InterceptorFactory.getDefaultInterceptorFactory();
    }

    private Driver getDelegatedDriver(String url) throws SQLException {
        Driver driver = driverCache.get(url);
        if (driver == null) {
            driver = DriverManager.getDriver(url);
            driverCache.put(url, driver);
        }
        return driver;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {

        // Is the url for Jfr4Jdbc.
        if (!url.startsWith(JFR4JDBC_URL_PREFIX)) {
            return false;
        }

        // Checking whether the driver is present.
        String delegeteJdbcDriverUrl = JfrServiceLoadedDriver.getDelegateUrl(url);
        Driver delegateDriver;
        try {
            delegateDriver = this.getDelegatedDriver(delegeteJdbcDriverUrl);
        } catch (SQLException e) {
            delegateDriver = null;
        }

        return delegateDriver != null;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {

        if (!url.startsWith(JFR4JDBC_URL_PREFIX)) {
            return null;
        }

        // Get a delegated Driver
        String delegeteUrl = JfrServiceLoadedDriver.getDelegateUrl(url);
        Driver delegateDriver = this.getDelegatedDriver(delegeteUrl);
        if (delegateDriver == null) {
            return null;
        }

        int connectionId = connectionCounter.getAndIncrement();
        DriverContext context = new DriverContext(delegateDriver, delegeteUrl, connectionId);
        Interceptor<DriverContext> interceptor = this.interceptorFactory.createDriverInterceptor();

        // Connecting to delegated url and recording connect event.
        Connection delegatedCon = null;
        try {
            this.connectionMonitor.waitAssigningResource();

            interceptor.preInvoke(context);
            delegatedCon = delegateDriver.connect(delegeteUrl, info);
            if (delegatedCon == null) {
                throw new SQLException("Invalid driver url: " + url);
            }
            context.setConnection(delegatedCon, 0);

        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
            this.connectionMonitor.assignedResource();
        }

        return new JfrConnection(delegatedCon, loadInterceptorFactory(url, info), this.connectionMonitor, new ConnectionInfo(this.connectionMonitor.getLabel(), connectionId, 0));
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        Driver driver = this.getDelegatedDriver(url);
        if (driver == null) {
            throw new Jfr4JdbcRuntimeException("No delegate Driver");
        }
        return driver.getPropertyInfo(this.getDelegateUrl(url), info);
    }

    @Override
    public int getMajorVersion() {
        return 2;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException("Jfr4Jdbc doesn't support");
    }
}