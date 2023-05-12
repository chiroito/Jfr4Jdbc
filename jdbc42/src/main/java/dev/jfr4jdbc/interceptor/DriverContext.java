package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.internal.ConnectionInfo;

import java.sql.Connection;
import java.sql.Driver;

public class DriverContext {

    public final Driver driver;

    public final String url;

    private ConnectionInfo connectionInfo;

    private Connection connection;

    private Exception exception;

    public DriverContext(Driver driver, String url, int connectionId) {
        this.driver = driver;
        this.url = url;
        this.connectionInfo = new ConnectionInfo(url, connectionId, 0);
    }

    public ConnectionInfo getConnectionInfo() {
        return this.connectionInfo;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection, int wrappedConnectionId) {
        this.connection = connection;
        this.connectionInfo = new ConnectionInfo(url, this.connectionInfo.conId, wrappedConnectionId);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
