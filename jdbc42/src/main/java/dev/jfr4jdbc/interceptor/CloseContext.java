package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.OperationInfo;

import java.sql.Connection;

public class CloseContext {

    public final Connection connection;

    public final ConnectionInfo connectionInfo;

    public final OperationInfo operationInfo;

    private Exception exception;

    public CloseContext(Connection connection, ConnectionInfo connectionInfo, OperationInfo operationInfo) {
        this.connection = connection;
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
