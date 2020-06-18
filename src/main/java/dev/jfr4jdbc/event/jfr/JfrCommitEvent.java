package dev.jfr4jdbc.event.jfr;

import dev.jfr4jdbc.event.CommitEvent;
import jdk.jfr.Label;

@Label("Commit")
public class JfrCommitEvent extends JfrJdbcEvent implements CommitEvent {

    @Label("connectionId")
    private int connectionId;

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public int getConnectionId() {
        return connectionId;
    }
}