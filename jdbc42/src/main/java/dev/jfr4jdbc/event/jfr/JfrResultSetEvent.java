package dev.jfr4jdbc.event.jfr;

import dev.jfr4jdbc.event.ResultSetEvent;
import jdk.jfr.Label;

@Label("ResultSet")
public class JfrResultSetEvent extends JfrJdbcEvent implements ResultSetEvent {

    @Label("rowNo")
    private int rowNo;

    @Label("connectionId")
    private int connectionId;

    @Label("statementId")
    private int statementId;

    @Label("resultSetClass")
    private Class<?> resultSetClass;

    @Label("resultSetId")
    private int resultSetId;

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public void setStatementId(int statementId) {
        this.statementId = statementId;
    }

    public void setResultSetClass(Class<?> clazz) {
        this.resultSetClass = clazz;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

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

    public Class<?> getResultSetClass() {
        return resultSetClass;
    }

    public int getResultSetId() {
        return resultSetId;
    }
}
