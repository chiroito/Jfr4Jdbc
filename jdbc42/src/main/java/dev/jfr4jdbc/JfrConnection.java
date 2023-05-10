package dev.jfr4jdbc;

import dev.jfr4jdbc.internal.JfrConnection42;

import java.sql.Connection;

public class JfrConnection extends JfrConnection42 implements Connection{

    public JfrConnection(Connection con) {
        super(con);
    }

    public JfrConnection(Connection con, String label) {
        super(con, label);
    }
}
