package dev.jfr4jdbc.interceptor.impl.period;

import dev.jfr4jdbc.interceptor.CloseContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class PeriodCloseInterceptor implements Interceptor<CloseContext> {

    private PeriodCloseEvent event;

    @Override
    public void preInvoke(CloseContext context) {
        event = new PeriodCloseEvent();
        event.begin();
    }

    @Override
    public void postInvoke(CloseContext context) {
        event.end();
        if (event.shouldCommit()) {
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
