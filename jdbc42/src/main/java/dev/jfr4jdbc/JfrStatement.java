package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.OperationInfo;

import java.sql.Statement;

public class JfrStatement extends JfrStatement42 implements Statement {
    public JfrStatement(Statement s) {
        super(s);
    }

    public JfrStatement(Statement s, InterceptorFactory factory) {
        super(s, factory);
    }

    JfrStatement(Statement s, InterceptorFactory factory, ConnectionInfo connectionInfo, OperationInfo operationInfo) {
        super(s, factory, connectionInfo, operationInfo);
    }
}