package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.OperationInfo;

import java.sql.Connection;
import java.sql.Statement;

public class StatementContext {

    private Connection connection;

    public final Statement statement;

    public final ConnectionInfo connectionInfo;

    public final OperationInfo operationInfo;

    public final String inquiry;

    public final boolean isPrepared;

    private String inquiryParameter;

    private boolean isStatementPoolable;

    private boolean isStatementClosed;

    private boolean isAutoCommitted;

    private Exception exception;

    public StatementContext(Statement statement, ConnectionInfo connectionInfo, OperationInfo operationInfo, String inquiry, boolean isPrepared) {
        this.statement = statement;
        this.connectionInfo = connectionInfo;
        this.operationInfo = operationInfo;
        this.inquiry = inquiry;
        this.isPrepared = isPrepared;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getInquiryParameter() {
        return inquiryParameter;
    }

    public void setInquiryParameter(String inquiryParameter) {
        this.inquiryParameter = inquiryParameter;
    }

    public boolean isStatementPoolable() {
        return isStatementPoolable;
    }

    public void setStatementPoolable(boolean statementPoolable) {
        isStatementPoolable = statementPoolable;
    }

    public boolean isStatementClosed() {
        return isStatementClosed;
    }

    public void setStatementClosed(boolean statementClosed) {
        isStatementClosed = statementClosed;
    }

    public boolean isAutoCommitted() {
        return isAutoCommitted;
    }

    public void setAutoCommitted(boolean autoCommitted) {
        isAutoCommitted = autoCommitted;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
