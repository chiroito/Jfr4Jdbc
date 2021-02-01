package dev.jfr4jdbc;

import java.sql.SQLException;
import java.sql.Statement;

public class JfrStatement43 extends JfrStatement42 implements Statement {

    public JfrStatement43(Statement s) {
        super(s);
    }

    public JfrStatement43(Statement s, EventFactory factory) {
        super(s, factory);
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
