package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.interceptor.CommitContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class InstantCommitInterceptor implements Interceptor<CommitContext> {

    private long start;

    @Override
    public void preInvoke(CommitContext context) {
        CommitStartEvent event = new CommitStartEvent();
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
    public void postInvoke(CommitContext context) {
        long end = System.nanoTime();
        CommitEndEvent event = new CommitEndEvent();
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
