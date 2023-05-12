package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.OperationInfo;

import java.sql.SQLException;
import java.sql.Statement;

public class JfrStatement43 extends JfrStatement42 implements Statement {

    protected JfrStatement43(Statement s) {
        super(s);
    }

    protected JfrStatement43(Statement s, InterceptorFactory factory) {
        super(s, factory);
    }

    protected JfrStatement43(Statement s, InterceptorFactory factory, ConnectionInfo connectionInfo, OperationInfo operationInfo) {
        super(s, factory, connectionInfo, operationInfo);
    }

    @Override
    public String enquoteLiteral(String val) throws SQLException {
        return this.jdbcStatement.enquoteLiteral(val);
    }

    @Override
    public String enquoteIdentifier(String identifier, boolean alwaysQuote) throws SQLException {
        return this.jdbcStatement.enquoteIdentifier(identifier, alwaysQuote);
    }

    @Override
    public boolean isSimpleIdentifier(String identifier) throws SQLException {
        return this.jdbcStatement.isSimpleIdentifier(identifier);
    }

    @Override
    public String enquoteNCharLiteral(String val) throws SQLException {
        return this.jdbcStatement.enquoteNCharLiteral(val);
    }
}
