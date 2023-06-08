package dev.jfr4jdbc.interceptor.impl.period;

import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.RollbackContext;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class PeriodRollbackInterceptor implements Interceptor<RollbackContext> {

    private PeriodRollbackEvent event;

    @Override
    public void preInvoke(RollbackContext context) {
        event = new PeriodRollbackEvent();
        event.begin();
    }

    @Override
    public void postInvoke(RollbackContext context) {
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
