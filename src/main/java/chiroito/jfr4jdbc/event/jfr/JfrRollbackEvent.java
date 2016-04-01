package chiroito.jfr4jdbc.event.jfr;

import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.ValueDefinition;

import chiroito.jfr4jdbc.event.RollbackEvent;

@SuppressWarnings({ "deprecation", "restriction" })
@EventDefinition(name = "Rollback", path = "jdbc/Rollback", thread = true)
public class JfrRollbackEvent extends JfrJdbcEvent implements RollbackEvent{
	
	public JfrRollbackEvent(EventToken paramEventToken) {
		super(paramEventToken);
	}

	@ValueDefinition(name = "ConnectionId", relationKey = "ConnectionId")
	private int connectionId;
	
	@Override
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	public int getConnectionId() {
		return connectionId;
	}
}