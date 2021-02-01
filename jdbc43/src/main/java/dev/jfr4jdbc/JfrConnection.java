package dev.jfr4jdbc;

import java.sql.Connection;

public class JfrConnection extends JfrConnection43 {

    protected JfrConnection(Connection con) {
        super(con);
    }

    protected JfrConnection(Connection con, String label) {
        super(con, label);
    }

    protected JfrConnection(Connection con, ResourceMonitor connectionMonitor) {
        super(con, connectionMonitor);
    }

    protected JfrConnection(Connection con, EventFactory factory, ResourceMonitor connectionMonitor) {
        super(con, factory, connectionMonitor);
    }
}
