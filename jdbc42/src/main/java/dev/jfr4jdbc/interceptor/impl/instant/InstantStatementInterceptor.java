package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.StatementContext;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class InstantStatementInterceptor implements Interceptor<StatementContext> {

    private long start;

    @Override
    public void preInvoke(StatementContext context) {
        StatementStartEvent event = new StatementStartEvent();
        if (event.isEnabled()) {
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
            event.commit();
        }
        start = System.nanoTime();
    }

    @Override
    public void postInvoke(StatementContext context) {
        long end = System.nanoTime();
        StatementEndEvent event = new StatementEndEvent();
        if (event.isEnabled()) {
            event.period = end - start;
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
