package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.event.RollbackEvent;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.RollbackContext;

public class LegacyRollbackInterceptor implements Interceptor<RollbackContext> {

    private final EventFactory eventFactory;

    private RollbackEvent event;

    public LegacyRollbackInterceptor(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public void preInvoke(RollbackContext context) {
        event = eventFactory.createRollbackEvent();
        event.setConnectionId(context.connectionInfo.conId);
        event.begin();
    }

    @Override
    public void postInvoke(RollbackContext context) {
        event.commit();
    }
}
