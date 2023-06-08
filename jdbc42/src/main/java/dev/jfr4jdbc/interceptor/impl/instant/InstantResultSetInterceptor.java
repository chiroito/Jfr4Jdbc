package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.ResultSetContext;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class InstantResultSetInterceptor implements Interceptor<ResultSetContext> {

    private long start;

    @Override
    public void preInvoke(ResultSetContext context) {
        ResultSetStartEvent event = new ResultSetStartEvent();
        if (event.isEnabled()) {
            ConnectionInfo conInfo = context.connectionInfo;
            event.dataSourceLabel = conInfo.dataSourceLabel;
            event.connectionId = conInfo.conId;
            event.wrappedConnectionId = conInfo.wrappedConId;
            event.operationId = context.operationInfo.id;
            event.commit();
        }
        start = System.nanoTime();
    }

    @Override
    public void postInvoke(ResultSetContext context) {
        long end = System.nanoTime();
        ResultSetEndEvent event = new ResultSetEndEvent();
        if (event.isEnabled()) {
            event.duration = end - start;
            ConnectionInfo conInfo = context.connectionInfo;
            event.dataSourceLabel = conInfo.dataSourceLabel;
            event.connectionId = conInfo.conId;
            event.wrappedConnectionId = conInfo.wrappedConId;
            event.operationId = context.operationInfo.id;
            event.result = context.isResult();
            event.rowNo = context.getRowNo();
            if (context.getException() != null) {
                event.exception = context.getException().getClass();
                event.exceptionMessage = context.getException().getMessage();
            }
            event.commit();
        }
    }
}
