package dev.jfr4jdbc.internal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ResourceMonitorManager {

    private static final ResourceMonitorManager instance = new ResourceMonitorManager();

    public static final ResourceMonitorManager getInstance() {
        return instance;
    }

    private final Map<Label, ResourceMonitor> monitors = new ConcurrentHashMap<>();

    public ResourceMonitor getOrCreateResourceMonitor(Label label) {
        return instance.monitors.computeIfAbsent(label, l -> new ResourceMonitor(l));
    }

    public List<ResourceMonitor> getMonitors() {
        return this.monitors.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }
}
