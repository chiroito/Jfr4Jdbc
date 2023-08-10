package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.ConnectionId;
import dev.jfr4jdbc.DataSourceLabel;
import dev.jfr4jdbc.WrappedConnectionId;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("jdbc")
@Label("RollbackStart")
public class RollbackEndEvent extends Event {

    public long period;

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
