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
    private final AtomicInteger waitCount = new AtomicInteger(0);

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
            JfrConnectionResourceEvent e = new JfrConnectionResourceEvent(m.getLabel(), m.getUsage(), m.getWait());
            e.commit();
        });
    }

    String getLabel() {
        return this.label;
    }

    int getUsage() {
        return this.usageCount.get();
    }

    int getWait() {
        return this.waitCount.get();
    }

    void waitAssigningResource() {
        this.waitCount.incrementAndGet();
    }

    void assignedResource() {
        this.waitCount.decrementAndGet();
    }

    void useResource() {
        this.usageCount.incrementAndGet();
    }

    void releaseResource() {
        this.usageCount.decrementAndGet();
    }
}
