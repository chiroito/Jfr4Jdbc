package chiroito.jfr4jdbc.event.jfr;

import chiroito.jfr4jdbc.event.StatementEvent;
import jdk.jfr.Label;

@Label("Statement")
public class JfrStatementEvent extends JfrJdbcEvent implements StatementEvent {

    @Label("SQL")
    private String sql;

    @Label("ConnectionId")
    private int connectionId;

    @Label("StatementId")
    private int statementId;

    @Label("StatementClass")
    private String statementClass;

    @Label("closed")
    private boolean closed;

    @Label("poolable")
    private boolean poolable;

    @Label("autoCommit")
    private boolean autoCommit;

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }

    public void setStatementClass(Class<?> clazz) {
        this.statementClass = clazz.getCanonicalName();
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

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