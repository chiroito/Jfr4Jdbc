package dev.jfr4jdbc.interceptor.impl;

import dev.jfr4jdbc.event.jfr.JfrConnectionResourceEvent;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.ResourceMonitorContext;

public class ResourceMonitorInterceptor implements Interceptor<ResourceMonitorContext> {

    private JfrConnectionResourceEvent event;

    @Override
    public void preInvoke(ResourceMonitorContext context) {
    }

    @Override
    public void postInvoke(ResourceMonitorContext context) {
        event = new JfrConnectionResourceEvent(context.dataSourceLabel, context.usage, context.wait);
        if (event.isEnabled()) {
            event.commit();
        }
    }
}
