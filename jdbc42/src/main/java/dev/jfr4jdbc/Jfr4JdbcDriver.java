package dev.jfr4jdbc;

import dev.jfr4jdbc.event.ConnectEvent;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author Chihiro Ito
 */
public class Jfr4JdbcDriver implements Driver {

    private static final String JFR4JDBC_URL_PREFIX = "jdbc:jfr";
    private static final int JFR4JDBC_URL_PREFIX_LENGTH = JFR4JDBC_URL_PREFIX.length();
    private static final ResourceMonitor connectionMonitor;

    static {
        try {
            ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
            connectionMonitor = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection).getMonitor("Connection");
            manager.addMonitor(connectionMonitor);
            DriverManager.registerDriver(new Jfr4JdbcDriver());
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
    private Driver delegateJdbcDriver;
    private EventFactory factory = EventFactory.getDefaultEventFactory();

    public Jfr4JdbcDriver() {
        super();
    }

    Jfr4JdbcDriver(Driver driver) {
        this.delegateJdbcDriver = driver;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {

        // Is the url for Jfr4Jdbc.
        if (!url.startsWith(JFR4JDBC_URL_PREFIX)) {
            return false;
        }

        // Checking whether the driver is present.
        String delegeteJdbcDriverUrl = Jfr4JdbcDriver.getDelegateUrl(url);
        Driver delegateDriver;
        if (this.delegateJdbcDriver == null) {
            try {
                delegateDriver = DriverManager.getDriver(delegeteJdbcDriverUrl);
            } catch (SQLException e) {
                delegateDriver = null;
            }
        } else {
            delegateDriver = this.delegateJdbcDriver;
        }

        return delegateDriver != null;
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {

        if (!url.startsWith(JFR4JDBC_URL_PREFIX)) {
            return null;
        }

        // Get a delegated Driver
        String delegeteUrl = Jfr4JdbcDriver.getDelegateUrl(url);
        Driver delegateDriver = (this.delegateJdbcDriver == null) ? DriverManager.getDriver(delegeteUrl) : this.delegateJdbcDriver;
        if (delegateDriver == null) {
            return null;
        }
        this.delegateJdbcDriver = delegateDriver;

        // Connecting to delegated url and recording connect event.
        ConnectEvent event = factory.createConnectEvent();
        event.setUrl(delegeteUrl);
        event.begin();
        Connection delegatedCon = null;
        try {
            this.connectionMonitor.waitAssigningResource();

            delegatedCon = delegateDriver.connect(delegeteUrl, info);
            if (delegatedCon == null) {
                throw new SQLException("Invalid driver url: " + url);
            } else {
                event.setConnectionClass(delegatedCon.getClass());
                event.setConnectionId(System.identityHashCode(delegatedCon));
            }
        } catch (SQLException | RuntimeException e) {
            throw e;
        } finally {
            this.connectionMonitor.assignedResource();
            event.commit();
        }

        return new JfrConnection(delegatedCon);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        if (this.delegateJdbcDriver == null) {
            throw new Jfr4JdbcRuntimeException("No delegate Driver");
        }
        return this.delegateJdbcDriver.getPropertyInfo(this.getDelegateUrl(url), info);
    }

    @Override
    public int getMajorVersion() {
        if (this.delegateJdbcDriver == null) {
            throw new Jfr4JdbcRuntimeException("No delegate Driver");
        }
        return this.delegateJdbcDriver.getMajorVersion();
    }

    @Override
    public int getMinorVersion() {
        if (this.delegateJdbcDriver == null) {
            throw new Jfr4JdbcRuntimeException("No delegate Driver");
        }
        return this.delegateJdbcDriver.getMinorVersion();
    }

    @Override
    public boolean jdbcCompliant() {
        return true;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        if (this.delegateJdbcDriver == null) {
            throw new Jfr4JdbcRuntimeException("No delegate Driver");
        }
        return this.delegateJdbcDriver.getParentLogger();
    }
}