package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.ConnectionId;
import dev.jfr4jdbc.DataSourceLabel;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("jdbc")
@Label("DriverEnd")
public class DriverStartEvent extends Event {

    @DataSourceLabel
    public String driverLabel;

    @ConnectionId
    public int connectionId;

    public String url;
}
