package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.ConnectionId;
import dev.jfr4jdbc.DataSourceLabel;
import dev.jfr4jdbc.WrappedConnectionId;
import jdk.jfr.Category;
import jdk.jfr.Event;
import jdk.jfr.Label;

@Category("jdbc")
@Label("CommitEnd")
public class CommitStartEvent extends Event {

    @DataSourceLabel
    public String dataSourceLabel;

    @ConnectionId
    public int connectionId;

    @WrappedConnectionId
    public int wrappedConnectionId;

    public int operationId;
}
