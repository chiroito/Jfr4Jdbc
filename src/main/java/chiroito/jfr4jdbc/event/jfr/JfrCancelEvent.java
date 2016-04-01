package chiroito.jfr4jdbc.event.jfr;

import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.ValueDefinition;

import chiroito.jfr4jdbc.event.CancelEvent;

@SuppressWarnings({ "deprecation", "restriction" })
@EventDefinition(name = "Cancel", path = "jdbc/Cancel", thread = true)
public class JfrCancelEvent extends JfrJdbcEvent implements CancelEvent {

	public JfrCancelEvent(EventToken paramEventToken) {
		super(paramEventToken);
	}

	@ValueDefinition(name = "ConnectionId", relationKey = "ConnectionId")
	private int connectionId;
	
	@ValueDefinition(name = "StatementId", relationKey = "StatementId")
	private int statementId;

	@Override
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;		
	}

	@Override
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
