package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.OperationInfo;

import java.sql.ResultSet;

public class ResultSetContext {

    public final ResultSet resultSet;

    public final ConnectionInfo connectionInfo;

    public final OperationInfo operationInfo;

    private boolean result;

    private int rowNo;

    private Exception exception;

    public ResultSetContext(ResultSet resultSet, ConnectionInfo connectionInfo, OperationInfo operationInfo) {
        this.resultSet = resultSet;
        this.connectionInfo = connectionInfo;
        this.operationInfo = operationInfo;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }


    public int getRowNo() {
        return rowNo;
    }

    public void setRowNo(int rowNo) {
        this.rowNo = rowNo;
    }

    public void setException(Exception e) {
        this.exception = e;
    }

    public Exception getException() {
        return exception;
    }
}
