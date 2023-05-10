package dev.jfr4jdbc;

import dev.jfr4jdbc.internal.Jfr4JdbcDataSource43;

import javax.sql.DataSource;

public class Jfr4JdbcDataSource extends Jfr4JdbcDataSource43 implements DataSource{

    public Jfr4JdbcDataSource(DataSource datasource) {
        super(datasource);
    }

    public Jfr4JdbcDataSource(DataSource datasource, String monitorLabel) {
        super(datasource, monitorLabel);
    }

    public Jfr4JdbcDataSource(DataSource datasource, EventFactory factory) {
        super(datasource, factory);
    }

    public Jfr4JdbcDataSource(DataSource datasource, EventFactory factory, String monitorLabel) {
        super(datasource, factory, monitorLabel);
    }
}