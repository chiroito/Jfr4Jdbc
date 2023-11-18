package dev.jfr4jdbc.internal;

import java.util.Objects;

public class Label {

    public final String value;

    public Label(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Label label = (Label) o;
        return Objects.equals(value, label.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Label{" +
                "value='" + value + '\'' +
                '}';
    }
}
