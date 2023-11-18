package dev.jfr4jdbc;

public class ResourceMetrics {
    public final String label;
    public final int usage;
    public final int wait;

    public ResourceMetrics(String label, int usage, int wait) {
        this.label = label;
        this.usage = usage;
        this.wait = wait;
    }
}
