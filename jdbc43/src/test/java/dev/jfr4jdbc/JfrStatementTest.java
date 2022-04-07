package dev.jfr4jdbc;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JfrStatementTest {

    protected static final String SAMPLE_SQL = "SELECT 1";

    @Mock
    Statement delegateState;

    @BeforeAll
    static void initClass() throws Exception {
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @DisplayName("enquoteIdentifier")
    @Test
    void enquoteIdentifier() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.enquoteIdentifier(null, false);
        verify(this.delegateState).enquoteIdentifier(null, false);
    }

    @DisplayName("enquoteLiteral")
    @Test
    void enquoteLiteral() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.enquoteLiteral(null);
        verify(this.delegateState).enquoteLiteral(null);
    }

    @DisplayName("enquoteNCharLiteral")
    @Test
    void enquoteNCharLiteral() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.enquoteNCharLiteral(null);
        verify(this.delegateState).enquoteNCharLiteral(null);
    }

    @DisplayName("isSimpleIdentifier")
    @Test
    void isSimpleIdentifier() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.isSimpleIdentifier(null);
        verify(this.delegateState).isSimpleIdentifier(null);
    }
}