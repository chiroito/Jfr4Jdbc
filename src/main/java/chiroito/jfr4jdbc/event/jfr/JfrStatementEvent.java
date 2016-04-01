package chiroito.jfr4jdbc.event.jfr;

import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.ValueDefinition;

import chiroito.jfr4jdbc.event.StatementEvent;

@SuppressWarnings({ "deprecation", "restriction" })
@EventDefinition(name = "Statement", path = "jdbc/Statement", thread = true)
public class JfrStatementEvent extends JfrJdbcEvent implements StatementEvent {

	public JfrStatementEvent(EventToken paramEventToken) {
		super(paramEventToken);
	}

	@ValueDefinition(name = "SQL")
	private String sql;

	@ValueDefinition(name = "ConnectionId", relationKey = "ConnectionId")
	private int connectionId;

	@ValueDefinition(name = "StatementId", relationKey = "StatementId")
	private int statementId;

	@ValueDefinition(name = "StatementClass")
	private String statementClass;

	@ValueDefinition(name = "closed")
	private boolean closed;

	@ValueDefinition(name = "poolable")
	private boolean poolable;

	@ValueDefinition(name = "autoCommit")
	private boolean autoCommit;

	@Override
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	@Override
	public void setStatementId(int statementId) {
		this.statementId = statementId;
	}

	@Override
	public void setStatementClass(Class<?> clazz) {
		this.statementClass = clazz.getCanonicalName();
	}

	@Override
	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public void setClosed(boolean closed) {
		this.closed = closed;
	}

	@Override
	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	@Override
	public void setPoolable(boolean poolable) {
		this.poolable = poolable;
	}

	public String getSql() {
		return sql;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public int getStatementId() {
		return statementId;
	}

	public String getStatementClass() {
		return statementClass;
	}

	public boolean getClosed() {
		return closed;
	}

	public boolean getPoolable() {
		return poolable;
	}

	public boolean getAutoCommit() {
		return autoCommit;
	}
}