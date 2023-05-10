package dev.jfr4jdbc.internal;

import dev.jfr4jdbc.EventFactory;

import javax.sql.DataSource;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.ShardingKeyBuilder;

abstract public class Jfr4JdbcDataSource43 extends Jfr4JdbcDataSource42 implements DataSource {

    protected Jfr4JdbcDataSource43(DataSource datasource) {
        super(datasource);
    }

    protected Jfr4JdbcDataSource43(DataSource datasource, String monitorLabel) {
        super(datasource, monitorLabel);
    }

    protected Jfr4JdbcDataSource43(DataSource datasource, EventFactory factory) {
        super(datasource, factory);
    }

    protected Jfr4JdbcDataSource43(DataSource datasource, EventFactory factory, String monitorLabel) {
        super(datasource, factory, monitorLabel);
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
