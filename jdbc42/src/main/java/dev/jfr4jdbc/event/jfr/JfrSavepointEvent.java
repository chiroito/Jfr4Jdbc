package dev.jfr4jdbc.event.jfr;

import dev.jfr4jdbc.event.SavepointEvent;
import jdk.jfr.Label;

@Label("Savepoint")
public final class JfrSavepointEvent extends JfrJdbcEvent implements SavepointEvent {

    @Label("Id")
    private int id;

    @Label("Name")
    private String name;

    /**
     * One of: {@code create}, {@code rollback}, or {@code release}.
     */
    @Label("Action")
    private final String action;

    public JfrSavepointEvent(String action) {
        this.action = action;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}