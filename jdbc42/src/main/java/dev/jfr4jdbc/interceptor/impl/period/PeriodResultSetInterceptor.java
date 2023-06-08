package dev.jfr4jdbc.interceptor.impl.period;

import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.ResultSetContext;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class PeriodResultSetInterceptor implements Interceptor<ResultSetContext> {

    private PeriodResultSetEvent event;

    @Override
    public void preInvoke(ResultSetContext context) {
        event = new PeriodResultSetEvent();
        event.begin();
    }

    @Override
    public void postInvoke(ResultSetContext context) {
        event.end();
        if (event.shouldCommit()) {
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
