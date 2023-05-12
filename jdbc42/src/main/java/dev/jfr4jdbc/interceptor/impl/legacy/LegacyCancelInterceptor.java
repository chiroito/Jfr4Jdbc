package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.event.CancelEvent;
import dev.jfr4jdbc.interceptor.CancelContext;
import dev.jfr4jdbc.interceptor.Interceptor;

public class LegacyCancelInterceptor implements Interceptor<CancelContext> {

    private final EventFactory eventFactory;

    private CancelEvent event;

    public LegacyCancelInterceptor(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public void preInvoke(CancelContext context) {
        event = eventFactory.createCancelEvent();
        event.setConnectionId(context.connectionInfo.conId);
        event.setStatementId(context.connectionInfo.wrappedConId);
        event.begin();
    }

    @Override
    public void postInvoke(CancelContext cancelContext) {
        event.commit();
    }
}
