package dev.jfr4jdbc;

import dev.jfr4jdbc.internal.JfrStatement42;

import java.sql.Statement;

public class JfrStatement extends JfrStatement42 implements Statement {
    public JfrStatement(Statement s) {
        super(s);
    }

    public JfrStatement(Statement s, EventFactory factory) {
        super(s, factory);
    }
}
