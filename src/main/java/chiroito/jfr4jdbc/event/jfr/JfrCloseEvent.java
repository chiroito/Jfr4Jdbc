package chiroito.jfr4jdbc.event.jfr;

import chiroito.jfr4jdbc.event.CloseEvent;
import jdk.jfr.Label;

@Label("Close")
public class JfrCloseEvent extends JfrJdbcEvent implements CloseEvent {

    @Label("connectionId")
    private int connectionId;

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public int getConnectionId() {
        return connectionId;
    }
}