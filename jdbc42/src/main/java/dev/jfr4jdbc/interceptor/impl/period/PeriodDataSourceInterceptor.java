package dev.jfr4jdbc.interceptor.impl.period;

import dev.jfr4jdbc.interceptor.DataSourceContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.internal.ConnectionInfo;

public class PeriodDataSourceInterceptor implements Interceptor<DataSourceContext> {

    private PeriodDataSourceEvent event;

    @Override
    public void preInvoke(DataSourceContext context) {
        event = new PeriodDataSourceEvent();
        event.begin();
    }

    @Override
    public void postInvoke(DataSourceContext context) {
        event.end();
        if (event.shouldCommit()) {
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
