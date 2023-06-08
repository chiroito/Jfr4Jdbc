package dev.jfr4jdbc.interceptor.impl.period;

import dev.jfr4jdbc.ConnectionId;
import dev.jfr4jdbc.DataSourceLabel;
import dev.jfr4jdbc.WrappedConnectionId;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("jdbc")
@Label("Cancel")
public class PeriodCancelEvent extends Event {
    @DataSourceLabel
    public String dataSourceLabel;

    @ConnectionId
    public int connectionId;

    @WrappedConnectionId
    public int wrappedConnectionId;

    public int operationId;

    public Class exception;

    public String exceptionMessage;
}
