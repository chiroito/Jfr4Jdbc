package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.*;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.OperationInfo;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(10)
public class LinkageIdTest {

    @DisplayName("connection Id")
    @Test
    void connectionIdTest() throws Exception {
        MockJDBC db = new MockJDBC();

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrDataSource ds = new JfrDataSource(db.getDataSource(1), mockInterceptorFactory);
        ConnectionInfo connectionInfo;
        try (Connection con = ds.getConnection()) {
            connectionInfo = ((JfrConnection) con).getConnectionInfo();
            try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM dual"); ResultSet resultSet = stmt.executeQuery();) {
                resultSet.next();
                con.commit();
                con.rollback();
            }
        }

        List<DataSourceContext> connectionEvents = mockInterceptorFactory.createDataSourceInterceptor().getAllPostEvents();
        List<CloseContext> closeEvents = mockInterceptorFactory.createCloseInterceptor().getAllPostEvents();
        List<CommitContext> commitEvents = mockInterceptorFactory.createCommitInterceptor().getAllPostEvents();
        List<RollbackContext> rollbackEvents = mockInterceptorFactory.createRollbackInterceptor().getAllPostEvents();
        List<StatementContext> statementEvents = mockInterceptorFactory.createStatementInterceptor().getAllPostEvents();
        List<ResultSetContext> resultSetEvents = mockInterceptorFactory.createResultSetInterceptor().getAllPostEvents();

        // Connection ID
        assertEquals(connectionInfo, connectionEvents.get(0).getConnectionInfo());
        assertEquals(connectionInfo, closeEvents.get(0).connectionInfo);
        assertEquals(connectionInfo, commitEvents.get(0).connectionInfo);
        assertEquals(connectionInfo, rollbackEvents.get(0).connectionInfo);
        assertEquals(connectionInfo, statementEvents.get(0).connectionInfo);
        assertEquals(connectionInfo, resultSetEvents.get(0).connectionInfo);
    }

    @DisplayName("statement Id")
    @Test
    void statementIdTest() throws Exception {
        MockJDBC db = new MockJDBC();

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrDataSource ds = new JfrDataSource(db.getDataSource(1), mockInterceptorFactory);
        OperationInfo operationInfo;
        try (Connection con = ds.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM dual"); ResultSet resultSet = stmt.executeQuery();) {
                operationInfo = ((JfrPreparedStatement) stmt).getOperationInfo();
                resultSet.next();
            }
        }

        List<StatementContext> statementEvents = mockInterceptorFactory.createStatementInterceptor().getAllPostEvents();
        List<ResultSetContext> resultSetEvents = mockInterceptorFactory.createResultSetInterceptor().getAllPostEvents();

        // Statement ID
        assertEquals(operationInfo, statementEvents.get(0).operationInfo);
        assertEquals(operationInfo, resultSetEvents.get(0).operationInfo);
    }
}