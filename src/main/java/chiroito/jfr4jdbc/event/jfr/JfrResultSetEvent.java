package chiroito.jfr4jdbc.event.jfr;

import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.ValueDefinition;

import chiroito.jfr4jdbc.event.ResultSetEvent;

@SuppressWarnings({ "deprecation", "restriction" })
@EventDefinition(name = "ResultSet", path = "jdbc/ResultSet", thread = true)
public class JfrResultSetEvent extends JfrJdbcEvent implements ResultSetEvent {

	public JfrResultSetEvent(EventToken paramEventToken) {
		super(paramEventToken);
	}

	@ValueDefinition(name = "RowNo")
	private int rowNo;

	@ValueDefinition(name = "ConnectionId", relationKey = "ConnectionId")
	private int connectionId;

	@ValueDefinition(name = "StatementId", relationKey = "StatementId")
	private int statementId;

	@ValueDefinition(name = "ResultSetClass")
	private String resultSetClass;

	@ValueDefinition(name = "ResultSetId", relationKey = "ResultSetId")
	private int resultSetId;

	@Override
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	@Override
	public void setStatementId(int statementId) {
		this.statementId = statementId;
	}

	@Override
	public void setResultSetClass(Class<?> clazz) {
		this.resultSetClass = clazz.getCanonicalName();
	}

	@Override
	public void setRowNo(int rowNo) {
		this.rowNo = rowNo;
	}

	@Override
	public void setResultSetId(int resultSetId) {
		this.resultSetId = resultSetId;
	}
	
	public int getRowNo() {
		return rowNo;
	}

	public int getStatementId() {
		return statementId;
	}

	public int getConnectionId() {
		return connectionId;
	}

	public String getResultSetClass() {
		return resultSetClass;
	}

	public int getResultSetId() {
		return resultSetId;
	}
}
