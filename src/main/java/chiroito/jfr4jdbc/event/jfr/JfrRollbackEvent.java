package chiroito.jfr4jdbc.event.jfr;

import chiroito.jfr4jdbc.event.RollbackEvent;
import jdk.jfr.Label;

@Label("Rollback")
public class JfrRollbackEvent extends JfrJdbcEvent implements RollbackEvent{
	
	@Label("ConnectionId")
	private int connectionId;
	
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	public int getConnectionId() {
		return connectionId;
	}
}