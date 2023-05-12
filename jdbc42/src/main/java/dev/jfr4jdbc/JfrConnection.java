package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.ResourceMonitor;

import java.sql.Connection;

public class JfrConnection extends JfrConnection42 implements Connection {

    public JfrConnection(Connection con) {
        super(con);
    }

    public JfrConnection(Connection con, String dataSourceLabel) {
        super(con, dataSourceLabel);
    }

    public JfrConnection(Connection con, InterceptorFactory factory) {
        super(con, factory);
    }

    public JfrConnection(Connection con, InterceptorFactory factory, String dataSourceLabel) {
        super(con, factory, dataSourceLabel);
    }

    JfrConnection(Connection con, InterceptorFactory factory, ResourceMonitor connectionMonitor, ConnectionInfo connectionInfo) {
        super(con, factory, connectionMonitor, connectionInfo);
    }
}
