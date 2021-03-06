package dev.jfr4jdbc.event.jfr;

import jdk.jfr.Label;
import jdk.jfr.Period;
import jdk.jfr.StackTrace;

@Period("1 s")
@Label("ConnectionResource")
@StackTrace(false)
public class JfrConnectionResourceEvent extends JfrJdbcEvent {

    private final String label;
    private final int usage;
    private final int wait;

    public JfrConnectionResourceEvent(String label, int usage, int wait) {
        this.label = label;
        this.usage = usage;
        this.wait = wait;
    }

    public String getLabel() {
        return label;
    }

    public int getUsage() {
        return usage;
    }

    public int getWait() {
        return wait;
    }
}
