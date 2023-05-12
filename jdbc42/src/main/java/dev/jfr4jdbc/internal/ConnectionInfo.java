package dev.jfr4jdbc.internal;

public class ConnectionInfo {
    public final String dataSourceLabel;
    public final int conId;

    public final int wrappedConId;

    public ConnectionInfo(String dataSourceLabel, int conId, int wrappedConId) {
        this.dataSourceLabel = dataSourceLabel;
        this.conId = conId;
        this.wrappedConId = wrappedConId;
    }
}
