package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.ConnectionId;
import dev.jfr4jdbc.DataSourceLabel;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("jdbc")
@Label("DriverStart")
public class DriverEndEvent extends Event {

    public long period;

    @DataSourceLabel
    public String driverLabel;

    @ConnectionId
    public int connectionId;

    public String url;

    public int wrappedConnectionId;

    public Class exception;

    public String exceptionMessage;
}
