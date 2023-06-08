package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.event.StatementEvent;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.StatementContext;

public class LegacyStatementInterceptor implements Interceptor<StatementContext> {

    private final EventFactory eventFactory;

    private StatementEvent event;

    public LegacyStatementInterceptor(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public void preInvoke(StatementContext context) {
        event = this.eventFactory.createStatementEvent();
        event.begin();
    }

    @Override
    public void postInvoke(StatementContext context) {
        event.end();
        if (event.shouldCommit()) {
            event.setSql(context.inquiry);
            event.setPrepared(context.isPrepared);
            event.setParameter(context.getInquiryParameter());

            event.setStatementId(context.operationInfo.id);
            event.setPoolable(context.isStatementPoolable());
            event.setClosed(context.isStatementClosed());
            event.setConnectionId(context.connectionInfo.conId);
            event.setAutoCommit(context.isAutoCommitted());
            if (context.statement != null) {
                event.setStatementClass(context.statement.getClass());
            }
            event.commit();
        }
    }
}
