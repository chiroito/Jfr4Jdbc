package dev.jfr4jdbc.interceptor.impl.period;

import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.StatementContext;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class PeriodStatementInterceptor implements Interceptor<StatementContext> {

    private PeriodStatementEvent event;

    @Override
    public void preInvoke(StatementContext context) {
        event = new PeriodStatementEvent();
        event.begin();
    }

    @Override
    public void postInvoke(StatementContext context) {
        event.end();
        if (event.shouldCommit()) {
            ConnectionInfo conInfo = context.connectionInfo;
            event.dataSourceLabel = conInfo.dataSourceLabel;
            event.connectionId = conInfo.conId;
            event.wrappedConnectionId = conInfo.wrappedConId;
            event.operationId = context.operationInfo.id;
            event.inquiry = context.inquiry;
            event.isPrepared = context.isPrepared;
            event.inquiryParameter = context.getInquiryParameter();
            event.isStatementPoolable = context.isStatementPoolable();
            event.isStatementClosed = context.isStatementClosed();
            event.isAutoCommitted = context.isAutoCommitted();

            if (context.getException() != null) {
                event.exception = context.getException().getClass();
                event.exceptionMessage = context.getException().getMessage();
            }
            event.commit();
        }
    }
}
