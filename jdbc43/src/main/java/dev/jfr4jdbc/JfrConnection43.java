package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.ResourceMonitor;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ShardingKey;

abstract public class JfrConnection43 extends JfrConnection42 implements Connection {

    public JfrConnection43(Connection con) {
        super(con);
    }

    public JfrConnection43(Connection con, String dataSourceLabel) {
        super(con, dataSourceLabel);
    }

    public JfrConnection43(Connection con, InterceptorFactory factory) {
        super(con, factory);
    }

    public JfrConnection43(Connection con, InterceptorFactory factory, String dataSourceLabel) {
        super(con, factory, dataSourceLabel);
    }

    public JfrConnection43(Connection con, InterceptorFactory factory, ResourceMonitor connectionMonitor, ConnectionInfo connectionInfo) {
        super(con, factory, connectionMonitor, connectionInfo);
    }


    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.connection.getNetworkTimeout();
    }

    @Override
    public void beginRequest() throws SQLException {
        this.connection.beginRequest();
    }

    @Override
    public void endRequest() throws SQLException {
        this.connection.endRequest();
    }

    @Override
    public boolean setShardingKeyIfValid(ShardingKey shardingKey, int timeout) throws SQLException {
        return this.connection.setShardingKeyIfValid(shardingKey, timeout);
    }

    @Override
    public boolean setShardingKeyIfValid(ShardingKey shardingKey, ShardingKey superShardingKey, int timeout) throws SQLException {
        return this.connection.setShardingKeyIfValid(shardingKey, superShardingKey, timeout);
    }

    @Override
    public void setShardingKey(ShardingKey shardingKey) throws SQLException {
        this.connection.setShardingKey(shardingKey);
    }

    @Override
    public void setShardingKey(ShardingKey shardingKey, ShardingKey superShardingKey) throws SQLException {
        this.connection.setShardingKey(shardingKey, superShardingKey);
    }
}