package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.internal.ConnectionInfo;

import javax.sql.DataSource;
import java.sql.Connection;

public class DataSourceContext {

    public final DataSource dataSource;

    private ConnectionInfo connectionInfo;

    public final int dataSourceId;

    private String username;

    private String password;

    private Connection connection;

    private Exception exception;

    public DataSourceContext(DataSource dataSource, ConnectionInfo connectionInfo, int dataSourceId) {
        this.dataSource = dataSource;
        this.connectionInfo = connectionInfo;
        this.dataSourceId = dataSourceId;
    }

    public ConnectionInfo getConnectionInfo() {
        return connectionInfo;
    }

    public String getUsername() {
        return username;
    }

    public void setAuth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection, int wrappedConnectionId) {
        this.connection = connection;
        this.connectionInfo = new ConnectionInfo(this.connectionInfo.dataSourceLabel, this.connectionInfo.conId, wrappedConnectionId);
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
