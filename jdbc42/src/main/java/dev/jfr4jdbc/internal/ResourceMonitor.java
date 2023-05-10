package dev.jfr4jdbc.internal;

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

    public static final void recordResourceMonitor(ResourceMonitorManager manager) {
        List<ResourceMonitor> monitors = manager.getMonitors();
        monitors.forEach(m -> {
            JfrConnectionResourceEvent e = new JfrConnectionResourceEvent(m.getLabel(), m.getUsage(), m.getWait());
            e.commit();
        });
    }

    public String getLabel() {
        return this.label;
    }

    public int getUsage() {
        return this.usageCount.get();
    }

    public int getWait() {
        return this.waitCount.get();
    }

    public void waitAssigningResource() {
        this.waitCount.incrementAndGet();
    }

    public void assignedResource() {
        this.waitCount.decrementAndGet();
    }

    public void useResource() {
        this.usageCount.incrementAndGet();
    }

    public void releaseResource() {
        this.usageCount.decrementAndGet();
    }
}
