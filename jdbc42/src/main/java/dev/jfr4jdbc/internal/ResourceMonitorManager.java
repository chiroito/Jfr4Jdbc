package dev.jfr4jdbc.internal;

import dev.jfr4jdbc.interceptor.InterceptorFactory;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ResourceMonitorManager {

    private static final Map<ResourceMonitorKind, ResourceMonitorManager> instances = new ConcurrentHashMap<>(1);
    private final Map<String, ResourceMonitor> monitors = new ConcurrentHashMap<>();

    public static final ResourceMonitorManager getInstance(ResourceMonitorKind kind) {
        return instances.computeIfAbsent(kind, k -> new ResourceMonitorManager());
    }

    public ResourceMonitor getMonitor(String label) {
        return this.monitors.get(label);
    }

    public ResourceMonitor createConnectionMonitor(DataSource dataSource, String dataSourceLabel, InterceptorFactory factory) {
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor resourceMonitor = new ResourceMonitor(dataSource, dataSourceLabel, factory);
        manager.addMonitor(resourceMonitor);

        return resourceMonitor;
    }

    public ResourceMonitor createConnectionMonitor(Driver driver, String dataSourceLabel, InterceptorFactory factory) {
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor resourceMonitor = new ResourceMonitor(driver, dataSourceLabel, factory);
        manager.addMonitor(resourceMonitor);

        return resourceMonitor;
    }

    public ResourceMonitor createConnectionMonitor(String dataSourceLabel, InterceptorFactory factory) {
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor resourceMonitor = new ResourceMonitor(null, null, dataSourceLabel, factory);
        manager.addMonitor(resourceMonitor);

        return resourceMonitor;
    }

    public List<ResourceMonitor> getMonitors() {
        return this.monitors.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
    }

    public void addMonitor(ResourceMonitor monitor) {
        this.monitors.put(monitor.getDataSourceLabel(), monitor);
    }

    public void removeMonitor(ResourceMonitor monitor) {
        this.monitors.remove(monitor.getDataSourceLabel());
    }
}
