package dev.jfr4jdbc;

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

        FlightRecording fr = FlightRecording.start();

        DataSource ds = new Jfr4JdbcDataSource(db.getDataSource(1));
        int connectionId;
        try (Connection con = ds.getConnection()) {
            connectionId = ((JfrConnection) con).getConnectionId();
            try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM dual"); ResultSet resultSet = stmt.executeQuery();) {
                resultSet.next();
                con.commit();
                con.rollback();
            }
        }

        fr.stop();

        List<RecordedEvent> connectionEvents = fr.getEvents("Connection");
        List<RecordedEvent> closeEvents = fr.getEvents("Close");
        List<RecordedEvent> commitEvents = fr.getEvents("Commit");
        List<RecordedEvent> rollbackEvents = fr.getEvents("Rollback");
        List<RecordedEvent> statementEvents = fr.getEvents("Statement");
        List<RecordedEvent> resultSetEvents = fr.getEvents("ResultSet");

        // Connection ID
        assertEquals(connectionId, connectionEvents.get(0).getInt("connectionId"));
        assertEquals(connectionId, closeEvents.get(0).getInt("connectionId"));
        assertEquals(connectionId, commitEvents.get(0).getInt("connectionId"));
        assertEquals(connectionId, rollbackEvents.get(0).getInt("connectionId"));
        assertEquals(connectionId, statementEvents.get(0).getInt("connectionId"));
        assertEquals(connectionId, resultSetEvents.get(0).getInt("connectionId"));
    }

    @DisplayName("statement Id")
    @Test
    void statementIdTest() throws Exception {
        MockJDBC db = new MockJDBC();

        FlightRecording fr = FlightRecording.start();

        DataSource ds = new Jfr4JdbcDataSource(db.getDataSource(1));
        int statementId;
        try (Connection con = ds.getConnection()) {
            try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM dual"); ResultSet resultSet = stmt.executeQuery();) {
                statementId = ((JfrPreparedStatement) stmt).getStatementId();
                resultSet.next();
            }
        }

        fr.stop();

        List<RecordedEvent> statementEvents = fr.getEvents("Statement");
        List<RecordedEvent> resultSetEvents = fr.getEvents("ResultSet");

        // Statement ID
        assertEquals(statementId, statementEvents.get(0).getInt("statementId"));
        assertEquals(statementId, resultSetEvents.get(0).getInt("statementId"));
    }

}