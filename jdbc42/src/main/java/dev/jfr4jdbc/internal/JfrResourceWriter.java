package dev.jfr4jdbc.internal;

import dev.jfr4jdbc.ResourceMetrics;

public class JfrResourceWriter {

    public void write(ResourceMonitor resourceMonitor) {
        ResourceMetrics metrics = resourceMonitor.getMetrics();
        JfrConnectionResourceEvent event = new JfrConnectionResourceEvent(resourceMonitor.getLabel().value, metrics.usage, metrics.wait);
        event.commit();
    }
}
