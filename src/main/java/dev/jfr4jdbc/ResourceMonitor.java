package dev.jfr4jdbc;

import dev.jfr4jdbc.event.jfr.JfrConnectionResourceEvent;
import jdk.jfr.FlightRecorder;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ResourceMonitor {

    private static volatile boolean isRecord = true;

    static {
        FlightRecorder.addPeriodicEvent(JfrConnectionResourceEvent.class, () -> {
            if (isRecord) {
                ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
                recordResourceMonitor(manager);
            }
        });
    }

    private final String label;
    private final AtomicInteger usageCount = new AtomicInteger(0);

    public ResourceMonitor(String label) {
        this.label = label;
    }

    static void stopRecording() {
        isRecord = false;
    }

    static void startRecording() {
        isRecord = true;
    }

    static final void recordResourceMonitor(ResourceMonitorManager manager) {
        List<ResourceMonitor> monitors = manager.getMonitors();
        monitors.stream().forEach(m -> {
            JfrConnectionResourceEvent e = new JfrConnectionResourceEvent();
            e.label = m.getLabel();
            e.usage = m.getUsage();
            e.commit();
        });
    }

    String getLabel() {
        return this.label;
    }

    private int getUsage() {
        return this.usageCount.get();
    }

    void useResource() {
        this.usageCount.incrementAndGet();
    }

    void releaseResource() {
        this.usageCount.decrementAndGet();
    }
}
