package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.RollbackContext;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class InstantRollbackInterceptor implements Interceptor<RollbackContext> {

    private long start;

    @Override
    public void preInvoke(RollbackContext context) {
        RollbackStartEvent event = new RollbackStartEvent();
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
    public void postInvoke(RollbackContext context) {
        long end = System.nanoTime();
        RollbackEndEvent event = new RollbackEndEvent();
        if (event.isEnabled()) {
            event.duration = end - start;
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
