package dev.jfr4jdbc.internal;

import dev.jfr4jdbc.event.jfr.JfrConnectionResourceEvent;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.interceptor.ResourceMonitorContext;
import jdk.jfr.FlightRecorder;

import javax.sql.DataSource;
import java.sql.Driver;
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

    public final DataSource dataSource;

    public final Driver driver;

    private final String dataSourceLabel;

    private final Interceptor<ResourceMonitorContext> interceptor;

    private final AtomicInteger usageCount = new AtomicInteger(0);
    private final AtomicInteger waitCount = new AtomicInteger(0);

    public ResourceMonitor(DataSource dataSource, String dataSourceLabel, InterceptorFactory factory) {
        this(dataSource, null, dataSourceLabel, factory);
    }

    public ResourceMonitor(Driver driver, String dataSourceLabel, InterceptorFactory factory) {
        this(null, driver, dataSourceLabel, factory);
    }

    public ResourceMonitor(DataSource dataSource, Driver driver, String dataSourceLabel, InterceptorFactory factory) {
        this.dataSource = dataSource;
        this.driver = driver;
        this.dataSourceLabel = dataSourceLabel;
        this.interceptor = factory.createResourceMonitorInterceptor();
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
            ResourceMonitorContext context = new ResourceMonitorContext(null, null, m.dataSourceLabel, m.getUsage(), m.getWait());
            m.interceptor.preInvoke(context);
            // currently nothing to do
            m.interceptor.postInvoke(context);
        });
    }

    public String getDataSourceLabel() {
        return this.dataSourceLabel;
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
