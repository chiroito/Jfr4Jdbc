package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.InterceptorFactory;

import javax.sql.DataSource;

public class JfrDataSource extends JfrDataSource42 implements DataSource {

    public JfrDataSource(DataSource datasource) {
        super(datasource);
    }

    public JfrDataSource(DataSource datasource, String label) {
        super(datasource, label);
    }

    public JfrDataSource(DataSource datasource, InterceptorFactory factory) {
        super(datasource, factory);
    }

    public JfrDataSource(DataSource datasource, InterceptorFactory factory, String label) {
        super(datasource, factory, label);
    }
}
