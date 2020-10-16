package dev.jfr4jdbc.event.jfr;

import jdk.jfr.Label;
import jdk.jfr.Period;
import jdk.jfr.StackTrace;

@Period("1 s")
@Label("ConnectionResource")
@StackTrace(false)
public class JfrConnectionResourceEvent extends JfrJdbcEvent {

    public String label;
    public int usage;
}
