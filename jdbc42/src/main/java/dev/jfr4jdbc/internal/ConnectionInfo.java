package dev.jfr4jdbc.internal;

import java.util.Objects;

public class ConnectionInfo {

    public static final ConnectionInfo NO_INFO = new ConnectionInfo(null, 0, 0);

    public final String dataSourceLabel;
    public final int conId;

    public final int wrappedConId;

    public ConnectionInfo(String dataSourceLabel, int conId, int wrappedConId) {
        this.dataSourceLabel = dataSourceLabel;
        this.conId = conId;
        this.wrappedConId = wrappedConId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionInfo that = (ConnectionInfo) o;
        return conId == that.conId && wrappedConId == that.wrappedConId && Objects.equals(dataSourceLabel, that.dataSourceLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataSourceLabel, conId, wrappedConId);
    }

    @Override
    public String toString() {
        return "ConnectionInfo{" +
                "dataSourceLabel='" + dataSourceLabel + '\'' +
                ", conId=" + conId +
                ", wrappedConId=" + wrappedConId +
                '}';
    }
}
