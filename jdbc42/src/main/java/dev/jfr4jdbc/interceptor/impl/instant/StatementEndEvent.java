package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.ConnectionId;
import dev.jfr4jdbc.DataSourceLabel;
import dev.jfr4jdbc.WrappedConnectionId;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("jdbc")
@Label("StatementStart")
public class StatementEndEvent extends Event {

    public long duration;

    @DataSourceLabel
    public String dataSourceLabel;

    @ConnectionId
    public int connectionId;

    @WrappedConnectionId
    public int wrappedConnectionId;

    public int operationId;

    public String inquiry;

    public boolean isPrepared;

    public String inquiryParameter;

    public boolean isStatementPoolable;

    public boolean isStatementClosed;

    public boolean isAutoCommitted;

    public Class exception;

    public String exceptionMessage;
}
