package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.InterceptorFactory;

import javax.sql.DataSource;

public class JfrDataSource extends JfrDataSource42 implements DataSource {

    public JfrDataSource(DataSource datasource) {
        super(datasource);
    }

    public JfrDataSource(DataSource datasource, String monitorLabel) {
        super(datasource, monitorLabel);
    }

    public JfrDataSource(DataSource datasource, InterceptorFactory factory) {
        super(datasource, factory);
    }

    public JfrDataSource(DataSource datasource, InterceptorFactory factory, String monitorLabel) {
        super(datasource, factory, monitorLabel);
    }
}
