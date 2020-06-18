package dev.jfr4jdbc.event.jfr;

import dev.jfr4jdbc.event.CancelEvent;
import jdk.jfr.Label;

@Label("Cancel")
public class JfrCancelEvent extends JfrJdbcEvent implements CancelEvent {

    @Label("connectionId")
    private int connectionId;

    @Label("statementId")
    private int statementId;

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public int getStatementId() {
        return statementId;
    }
}
