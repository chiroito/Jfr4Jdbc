package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.OperationInfo;

import java.sql.Connection;
import java.sql.Statement;

public class CancelContext {

    public final Connection connection;

    public final Statement statement;

    public final ConnectionInfo connectionInfo;

    public final OperationInfo operationInfo;

    private Exception exception;

    public CancelContext(Connection connection, Statement statement, ConnectionInfo connectionInfo, OperationInfo operationInfo) {
        this.connection = connection;
        this.statement = statement;
        this.connectionInfo = connectionInfo;
        this.operationInfo = operationInfo;
    }

    public void setException(Exception e) {
        this.exception = e;
    }

    public Exception getException() {
        return exception;
    }
}
