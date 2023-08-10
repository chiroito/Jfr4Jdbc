package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.interceptor.CancelContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class InstantCancelInterceptor implements Interceptor<CancelContext> {

    private long start;

    @Override
    public void preInvoke(CancelContext context) {
        CancelStartEvent event = new CancelStartEvent();
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
    public void postInvoke(CancelContext context) {
        long end = System.nanoTime();
        CancelEndEvent event = new CancelEndEvent();
        if (event.isEnabled()) {
            event.period = end - start;
            ConnectionInfo conInfo = context.connectionInfo;
            event.dataSourceLabel = conInfo.dataSourceLabel;
            event.connectionId = conInfo.conId;
            event.wrappedConnectionId = conInfo.wrappedConId;
            event.operationId = context.operationInfo.id;
            if (context.getException() != null) {
                event.exception = context.getException().getClass();
                event.exceptionMessage = context.getException().getMessage();
            }
            event.commit();
        }
    }
}
