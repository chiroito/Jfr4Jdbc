package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.ResourceMonitor;

import java.sql.Connection;

public class JfrConnection extends JfrConnection42 implements Connection {

    public JfrConnection(Connection con) {
        super(con);
    }

    public JfrConnection(Connection con, String label) {
        super(con, label);
    }

    public JfrConnection(Connection con, InterceptorFactory factory) {
        super(con, factory);
    }

    public JfrConnection(Connection con, InterceptorFactory factory, String label) {
        super(con, factory, label);
    }

    JfrConnection(Connection con, InterceptorFactory factory, ResourceMonitor connectionMonitor, ConnectionInfo connectionInfo) {
        super(con, factory, connectionMonitor, connectionInfo);
    }
}
