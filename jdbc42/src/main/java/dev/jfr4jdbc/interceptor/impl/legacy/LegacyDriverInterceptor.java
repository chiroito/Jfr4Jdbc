package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.event.ConnectEvent;
import dev.jfr4jdbc.interceptor.DriverContext;
import dev.jfr4jdbc.interceptor.Interceptor;

public class LegacyDriverInterceptor implements Interceptor<DriverContext> {

    private EventFactory eventFactory;

    private ConnectEvent event;

    public LegacyDriverInterceptor(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public void preInvoke(DriverContext context) {
        event = this.eventFactory.createConnectEvent();
        event.begin();
    }

    @Override
    public void postInvoke(DriverContext context) {
        event.end();
        if (event.shouldCommit()) {
            if (context.getConnection() != null) {
                event.setConnectionClass(context.getConnection().getClass());
            }
            event.setUrl(context.url);
            event.setConnectionId(context.getConnectionInfo().conId);
            event.commit();
        }
    }
}
