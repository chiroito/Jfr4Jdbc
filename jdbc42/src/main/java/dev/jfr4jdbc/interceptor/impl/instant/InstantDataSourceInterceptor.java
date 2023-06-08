package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.interceptor.DataSourceContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class InstantDataSourceInterceptor implements Interceptor<DataSourceContext> {

    private long start;

    @Override
    public void preInvoke(DataSourceContext context) {
        DataSourceStartEvent event = new DataSourceStartEvent();
        if (event.isEnabled()) {
            ConnectionInfo conInfo = context.getConnectionInfo();
            event.dataSourceLabel = conInfo.dataSourceLabel;
            event.connectionId = conInfo.conId;
            event.username = context.getUsername();
            event.password = context.getPassword();
            event.commit();
        }
        start = System.nanoTime();
    }

    @Override
    public void postInvoke(DataSourceContext context) {
        long end = System.nanoTime();
        DataSourceEndEvent event = new DataSourceEndEvent();
        if (event.isEnabled()) {
            event.duration = end - start;
            ConnectionInfo conInfo = context.getConnectionInfo();
            event.dataSourceLabel = conInfo.dataSourceLabel;
            event.connectionId = conInfo.conId;
            event.wrappedConnectionId = conInfo.wrappedConId;
            event.username = context.getUsername();
            event.password = context.getPassword();

            if (context.getException() != null) {
                event.exception = context.getException().getClass();
                event.exceptionMessage = context.getException().getMessage();
            }
            event.commit();
        }
    }
}
