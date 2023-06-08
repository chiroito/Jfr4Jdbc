package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.interceptor.DriverContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class InstantDriverInterceptor implements Interceptor<DriverContext> {

    private long start;

    @Override
    public void preInvoke(DriverContext context) {
        DriverStartEvent event = new DriverStartEvent();
        if (event.isEnabled()) {
            ConnectionInfo conInfo = context.getConnectionInfo();
            event.driverLabel = conInfo.dataSourceLabel;
            event.connectionId = conInfo.conId;
            event.url = context.url;
            event.commit();
        }
        start = System.nanoTime();
    }

    @Override
    public void postInvoke(DriverContext context) {
        long end = System.nanoTime();
        DriverEndEvent event = new DriverEndEvent();
        if (event.isEnabled()) {
            event.duration = end - start;
            ConnectionInfo conInfo = context.getConnectionInfo();
            event.driverLabel = conInfo.dataSourceLabel;
            event.connectionId = conInfo.conId;
            event.wrappedConnectionId = conInfo.wrappedConId;
            event.url = context.url;

            if (context.getException() != null) {
                event.exception = context.getException().getClass();
                event.exceptionMessage = context.getException().getMessage();
            }
            event.commit();
        }
    }
}
