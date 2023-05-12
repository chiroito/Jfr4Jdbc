package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.event.CommitEvent;
import dev.jfr4jdbc.interceptor.CommitContext;
import dev.jfr4jdbc.interceptor.Interceptor;

public class LegacyCommitInterceptor implements Interceptor<CommitContext> {

    private final EventFactory eventFactory;
    private CommitEvent event;

    public LegacyCommitInterceptor(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public void preInvoke(CommitContext context) {
        event = eventFactory.createCommitEvent();
        event.setConnectionId(context.connectionInfo.conId);
        event.begin();
    }

    @Override
    public void postInvoke(CommitContext context) {
        event.commit();
    }
}
