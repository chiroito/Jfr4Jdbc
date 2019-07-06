package chiroito.jfr4jdbc.event.jfr;

import chiroito.jfr4jdbc.event.CancelEvent;
import jdk.jfr.Label;

@Label("Cancel")
public class JfrCancelEvent extends JfrJdbcEvent implements CancelEvent {

    @Label("ConnectionId")
    private int connectionId;

    @Label("StatementId")
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
