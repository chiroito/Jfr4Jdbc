package chiroito.jfr4jdbc.event.jfr;

import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.ValueDefinition;

import chiroito.jfr4jdbc.event.CommitEvent;

@SuppressWarnings({ "deprecation", "restriction" })
@EventDefinition(name = "Commit", path = "jdbc/Commit", thread = true)
public class JfrCommitEvent extends JfrJdbcEvent implements CommitEvent {

	public JfrCommitEvent(EventToken paramEventToken) {
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