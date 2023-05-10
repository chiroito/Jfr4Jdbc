package dev.jfr4jdbc.internal;

import dev.jfr4jdbc.EventFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ShardingKey;

abstract public class JfrConnection43 extends JfrConnection42 implements Connection {

    protected JfrConnection43(Connection con) {
        super(con);
    }

    protected JfrConnection43(Connection con, String label) {
        super(con, label);
    }

    protected JfrConnection43(Connection con, ResourceMonitor connectionMonitor) {
        super(con, connectionMonitor);
    }

    protected JfrConnection43(Connection con, EventFactory factory, ResourceMonitor connectionMonitor) {
        super(con, factory, connectionMonitor);
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