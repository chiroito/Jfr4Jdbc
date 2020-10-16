package dev.jfr4jdbc;

import dev.jfr4jdbc.event.ConnectEvent;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Jfr4JdbcDataSource implements DataSource {

    private static final AtomicInteger labelCounter = new AtomicInteger(0);

    private final DataSource datasource;
    private final int datasourceId;
    private final EventFactory factory;

    private final ResourceMonitor connectionMonitor;

    public Jfr4JdbcDataSource(DataSource datasource) {
        this(datasource, EventFactory.getDefaultEventFactory());
    }

    public Jfr4JdbcDataSource(DataSource datasource, String monitorLabel) {
        this(datasource, EventFactory.getDefaultEventFactory(), monitorLabel);
    }

    public Jfr4JdbcDataSource(DataSource datasource, EventFactory factory) {
        this(datasource, factory, "DataSource#" + labelCounter.incrementAndGet());
    }

    public Jfr4JdbcDataSource(DataSource datasource, EventFactory factory, String monitorLabel) {
        super();
        if (datasource == null) {
            throw new Jfr4JdbcRuntimeException("No delegate DataSource");
        }
        this.datasource = datasource;
        this.datasourceId = System.identityHashCode(datasource);
        this.factory = factory;

        this.connectionMonitor = new ResourceMonitor(monitorLabel);
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        manager.addMonitor(this.connectionMonitor);
    }

    @Override
    public Connection getConnection() throws SQLException {

        ConnectEvent event = factory.createConnectEvent();

        event.begin();
        event.setDataSourceId(this.datasourceId);
        event.setDataSourceClass(this.datasource.getClass());

        Connection delegatedCon = null;
        try {
            delegatedCon = this.datasource.getConnection();
            if (delegatedCon != null) {

                event.setConnectionClass(delegatedCon.getClass());
                event.setConnectionId(System.identityHashCode(delegatedCon));
            }

        } catch (SQLException | RuntimeException e) {
            throw e;
        } finally {
            event.commit();
        }

        return new JfrConnection(delegatedCon, this.connectionMonitor);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        ConnectEvent event = factory.createConnectEvent();

        event.begin();
        event.setUserName(username);
        event.setPassword(password);
        event.setDataSourceId(this.datasourceId);
        event.setDataSourceClass(this.datasource.getClass());

        Connection delegatedCon = null;
        try {
            delegatedCon = this.datasource.getConnection(username, password);
            if (delegatedCon != null) {
                event.setConnectionClass(delegatedCon.getClass());
                event.setConnectionId(System.identityHashCode(delegatedCon));
            }

        } catch (SQLException | RuntimeException e) {
            throw e;
        } finally {
            event.commit();
        }

        return new JfrConnection(delegatedCon, this.connectionMonitor);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.datasource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.datasource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.datasource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.datasource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.datasource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.datasource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.datasource.isWrapperFor(iface);
    }

    @Override
    public ShardingKeyBuilder createShardingKeyBuilder() throws SQLException {
        return this.datasource.createShardingKeyBuilder();
    }

    @Override
    public ConnectionBuilder createConnectionBuilder() throws SQLException {
        return this.datasource.createConnectionBuilder();
    }
}
