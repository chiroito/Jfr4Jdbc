package dev.jfr4jdbc.event.jfr;

import dev.jfr4jdbc.event.StatementEvent;
import jdk.jfr.Label;

@Label("Statement")
public class JfrStatementEvent extends JfrJdbcEvent implements StatementEvent {

    @Label("sql")
    private String sql;

    @Label("connectionId")
    private int connectionId;

    @Label("statementId")
    private int statementId;

    @Label("statementClass")
    private Class<?> statementClass;

    @Label("closed")
    private boolean closed;

    @Label("poolable")
    private boolean poolable;

    @Label("autoCommit")
    private boolean autoCommit;

    @Label("prepared")
    private boolean prepared;

    @Label("parameter")
    private String parameter;

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }

    public void setStatementClass(Class<?> clazz) {
        this.statementClass = clazz;
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

    public void setPrepared(boolean prepared) {
        this.prepared = prepared;
    }

    public String getParameter() {
        return parameter;
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

    public Class<?> getStatementClass() {
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

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public boolean getPrepared() {
        return prepared;
    }
}