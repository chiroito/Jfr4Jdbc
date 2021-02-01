package dev.jfr4jdbc;

import java.sql.Connection;

public class JfrConnection extends JfrConnection42 {

    public JfrConnection(Connection con) {
        super(con);
    }

    public JfrConnection(Connection con, String label) {
        super(con, label);
    }

    public JfrConnection(Connection con, ResourceMonitor connectionMonitor) {
        super(con, connectionMonitor);
    }

    public JfrConnection(Connection con, EventFactory factory, ResourceMonitor connectionMonitor) {
        super(con, factory, connectionMonitor);
    }
}
