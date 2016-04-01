package chiroito.jfr4jdbc.event.jfr;

import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.ValueDefinition;

import chiroito.jfr4jdbc.event.CloseEvent;

@SuppressWarnings({ "deprecation", "restriction" })
@EventDefinition(name = "Close", path = "jdbc/Close", thread = true)
public class JfrCloseEvent extends JfrJdbcEvent implements CloseEvent{

	public JfrCloseEvent(EventToken paramEventToken) {
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