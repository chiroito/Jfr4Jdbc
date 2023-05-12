package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.event.CloseEvent;
import dev.jfr4jdbc.interceptor.CloseContext;
import dev.jfr4jdbc.interceptor.Interceptor;

public class LegacyCloseInterceptor implements Interceptor<CloseContext> {

    private final EventFactory eventFactory;
    private CloseEvent event;

    public LegacyCloseInterceptor(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public void preInvoke(CloseContext context) {
        event = eventFactory.createCloseEvent();
        event.setConnectionId(context.connectionInfo.conId);
        event.begin();
    }

    @Override
    public void postInvoke(CloseContext context) {
        event.commit();
    }
}
