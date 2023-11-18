package dev.jfr4jdbc.internal;

import dev.jfr4jdbc.ResourceMetrics;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.concurrent.atomic.AtomicInteger;

public class ResourceMonitor {

    private final Label label;

    private final AtomicInteger usageCount = new AtomicInteger(0);
    private final AtomicInteger waitCount = new AtomicInteger(0);

    ResourceMonitor(Label label) {
        this.label = label;
    }

    public ResourceMetrics getMetrics() {
        return new ResourceMetrics(label.value, usageCount.get(), waitCount.get());
    }

    public Label getLabel() {
        return label;
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
