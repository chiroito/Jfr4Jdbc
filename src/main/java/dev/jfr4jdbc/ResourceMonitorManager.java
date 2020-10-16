package dev.jfr4jdbc;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ResourceMonitorManager {

    private static final Map<ResourceMonitorKind, ResourceMonitorManager> instances = new ConcurrentHashMap<>(1);
    private final Map<String, ResourceMonitor> monitors = new ConcurrentHashMap<>();

    static final ResourceMonitorManager getInstance(ResourceMonitorKind kind) {
        return instances.computeIfAbsent(kind, k -> new ResourceMonitorManager());
    }

    ResourceMonitor getMonitor(String label) {
        return this.monitors.computeIfAbsent(label, id -> new ResourceMonitor(id));
    }

    List<ResourceMonitor> getMonitors() {
        return this.monitors.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    void addMonitor(ResourceMonitor monitor) {
        this.monitors.put(monitor.getLabel(), monitor);
    }
}
