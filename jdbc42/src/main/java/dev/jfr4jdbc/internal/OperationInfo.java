package dev.jfr4jdbc.internal;

import java.util.Objects;

public class OperationInfo {

    public static final OperationInfo NO_INFO = new OperationInfo(0);

    public final int id;

    public OperationInfo(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationInfo that = (OperationInfo) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "OperationInfo{" +
                "id=" + id +
                '}';
    }
}
