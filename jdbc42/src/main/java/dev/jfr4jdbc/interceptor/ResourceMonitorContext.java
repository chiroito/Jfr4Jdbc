package dev.jfr4jdbc.interceptor;

import javax.sql.DataSource;
import java.sql.Driver;

public class ResourceMonitorContext {

    public final DataSource dataSource;

    public final Driver driver;

    public final String dataSourceLabel;

    public final int usage;

    public final int wait;

    public ResourceMonitorContext(DataSource dataSource, Driver driver, String dataSourceLabel, int usage, int wait) {
        this.dataSource = dataSource;
        this.driver = driver;
        this.dataSourceLabel = dataSourceLabel;
        this.usage = usage;
        this.wait = wait;
    }
}
