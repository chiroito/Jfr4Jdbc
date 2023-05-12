package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.InterceptorFactory;

import javax.sql.DataSource;
import java.sql.ConnectionBuilder;
import java.sql.SQLException;
import java.sql.ShardingKeyBuilder;

abstract public class JfrDataSource43 extends JfrDataSource42 implements DataSource {

    protected JfrDataSource43(DataSource datasource) {
        super(datasource);
    }

    protected JfrDataSource43(DataSource datasource, String monitorLabel) {
        super(datasource, monitorLabel);
    }

    protected JfrDataSource43(DataSource datasource, InterceptorFactory factory) {
        super(datasource, factory);
    }

    protected JfrDataSource43(DataSource datasource, InterceptorFactory factory, String monitorLabel) {
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
