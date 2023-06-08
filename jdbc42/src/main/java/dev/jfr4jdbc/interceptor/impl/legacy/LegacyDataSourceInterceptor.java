package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.event.ConnectEvent;
import dev.jfr4jdbc.interceptor.DataSourceContext;
import dev.jfr4jdbc.interceptor.Interceptor;

public class LegacyDataSourceInterceptor implements Interceptor<DataSourceContext> {

    private final EventFactory factory;

    public LegacyDataSourceInterceptor(EventFactory factory) {
        this.factory = factory;
    }

    private ConnectEvent event;

    @Override
    public void preInvoke(DataSourceContext context) {
        this.event = factory.createConnectEvent();
        event.begin();
    }

    @Override
    public void postInvoke(DataSourceContext context) {
        event.end();
        if (event.shouldCommit()) {
            if (context.getConnection() != null) {
                event.setConnectionClass(context.getConnection().getClass());
            }
            event.setDataSourceId(context.dataSourceId);
            event.setDataSourceClass(context.dataSource.getClass());
            event.setUserName(context.getUsername());
            event.setPassword(context.getPassword());
            event.setConnectionId(context.getConnectionInfo().conId);
            event.commit();
        }
    }
}
