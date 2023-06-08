package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.event.ResultSetEvent;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.ResultSetContext;

public class LegacyResultSetInterceptor implements Interceptor<ResultSetContext> {

    private final EventFactory eventFactory;

    private ResultSetEvent event;

    public LegacyResultSetInterceptor(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public void preInvoke(ResultSetContext context) {
        event = eventFactory.createResultSetEvent();
        event.begin();
    }

    @Override
    public void postInvoke(ResultSetContext context) {
        event.end();
        if (event.shouldCommit()) {
            event.setResultSetId(0);
            event.setResultSetClass(context.resultSet.getClass());
            event.setConnectionId(context.connectionInfo.conId);
            event.setStatementId(context.operationInfo.id);
            event.setRowNo(context.getRowNo());
            event.commit();
        }
    }
}
