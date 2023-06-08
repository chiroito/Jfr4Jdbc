package dev.jfr4jdbc.interceptor.impl.period;

import dev.jfr4jdbc.interceptor.DriverContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class PeriodDriverInterceptor implements Interceptor<DriverContext> {

    private PeriodDriverEvent event;

    @Override
    public void preInvoke(DriverContext context) {
        event = new PeriodDriverEvent();
        event.begin();
    }

    @Override
    public void postInvoke(DriverContext context) {
        event.end();
        if (event.shouldCommit()) {
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
