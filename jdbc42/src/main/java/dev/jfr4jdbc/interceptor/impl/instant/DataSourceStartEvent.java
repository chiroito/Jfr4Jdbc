package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.ConnectionId;
import dev.jfr4jdbc.DataSourceLabel;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("jdbc")
@Label("DataSourceEnd")
public class DataSourceStartEvent extends Event {

    @DataSourceLabel
    public String dataSourceLabel;

    @ConnectionId
    public int connectionId;

    public String username;

    public String password;
}
