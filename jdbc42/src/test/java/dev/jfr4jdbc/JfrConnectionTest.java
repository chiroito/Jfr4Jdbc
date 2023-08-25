package dev.jfr4jdbc;

import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(1)
class JfrConnectionTest {

    @Mock
    private Connection delegatedCon;

    @BeforeAll
    static void initClass() throws Exception {
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("create CommitEvent")
    @Test
    void createCommitEvent() throws Exception {
        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        connection.commit();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("Commit");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getInt("connectionId") > 0);
    }

    @DisplayName("create CommitEvent throw exception as expected")
    @Test
    void createCommitEventThrowSQLException() throws Exception {
        Mockito.doThrow(new SQLException()).when(delegatedCon).commit();

        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        try {
            connection.commit();
            fail();
        } catch (SQLException e) {

        } finally {
            fr.stop();
        }

        List<RecordedEvent> events = fr.getEvents("Commit");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(connection.getConnectionId(), event.getInt("connectionId"));
    }

    @DisplayName("create CommitEvent throw exception as unexpected")
    @Test
    void createCommitEventThrowRuntimeException() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(delegatedCon).commit();

        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        try {
            connection.commit();
            fail();
        } catch (RuntimeException e) {

        } finally {
            fr.stop();
        }

        List<RecordedEvent> events = fr.getEvents("Commit");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(connection.getConnectionId(), event.getInt("connectionId"));
    }

    @DisplayName("create RollbackEvent")
    @Test
    void createRollbackEvent() throws Exception {
        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        connection.rollback();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("Rollback");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(connection.getConnectionId(), event.getInt("connectionId"));
    }

    @DisplayName("create RollbackEvent throw exception as expected")
    @Test
    void createRollbackEventThrowSQLException() throws Exception {
        Mockito.doThrow(new SQLException()).when(delegatedCon).rollback();

        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        try {
            connection.rollback();
            fail();
        } catch (SQLException e) {

        } finally {
            fr.stop();
        }

        List<RecordedEvent> events = fr.getEvents("Rollback");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(connection.getConnectionId(), event.getInt("connectionId"));
    }

    @DisplayName("create RollbackEvent throw exception as unexpected")
    @Test
    void createRollbackEventThrowRuntimeException() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(delegatedCon).rollback();

        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        try {
            connection.rollback();
            fail();
        } catch (RuntimeException e) {

        } finally {
            fr.stop();
        }

        List<RecordedEvent> events = fr.getEvents("Rollback");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(connection.getConnectionId(), event.getInt("connectionId"));
    }

    @DisplayName("create CloseEvent")
    @Test
    void createCloseEvent() throws Exception {
        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        connection.close();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("Close");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(connection.getConnectionId(), event.getInt("connectionId"));
    }

    @DisplayName("create CloseEvent throw exception as expected")
    @Test
    void createCloseEventThrowSQLException() throws Exception {
        Mockito.doThrow(new SQLException()).when(delegatedCon).close();

        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        try {
            connection.close();
            fail();
        } catch (SQLException e) {

        } finally {
            fr.stop();
        }

        List<RecordedEvent> events = fr.getEvents("Close");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(connection.getConnectionId(), event.getInt("connectionId"));
    }

    @DisplayName("create CloseEvent throw exception as unexpected")
    @Test
    void createCloseEventThrowRuntimeException() throws Exception {
        Mockito.doThrow(new RuntimeException()).when(delegatedCon).close();

        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        try {
            connection.close();
            fail();
        } catch (RuntimeException e) {

        } finally {
            fr.stop();
        }

        List<RecordedEvent> events = fr.getEvents("Close");
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(connection.getConnectionId(), event.getInt("connectionId"));
    }

    @DisplayName("create SavepointEvents")
    @Test
    void createSavepointEvents() throws Exception {
        int savepointId = 42;
        String savepointName = "Named";
        Savepoint savepoint = mock(Savepoint.class);
        when(savepoint.getSavepointId()).thenReturn(savepointId);
        when(savepoint.getSavepointName()).thenReturn(savepointName);
        when(this.delegatedCon.setSavepoint()).thenReturn(savepoint);
        when(this.delegatedCon.setSavepoint(savepointName)).thenReturn(savepoint);

        FlightRecording fr = FlightRecording.start();
        try (JfrConnection connection = new JfrConnection(this.delegatedCon)) {
            Savepoint byId = connection.setSavepoint();
            Savepoint byName = connection.setSavepoint(savepointName);
            connection.rollback(byId);
            connection.releaseSavepoint(byName);
        }
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("Savepoint");
        assertEquals(4, events.size());
        RecordedEvent event = events.get(0);
        assertEquals("create", event.getString("action"));
        assertEquals(savepointId, event.getInt("id"));

        event = events.get(1);
        assertEquals("create", event.getString("action"));
        assertEquals(savepointName, event.getString("name"));

        event = events.get(2);
        assertEquals("rollback", event.getString("action"));
        assertEquals(savepointId, event.getInt("id"));

        event = events.get(3);
        assertEquals("release", event.getString("action"));
        assertEquals(savepointName, event.getString("name"));
    }

    @Test
    void commit() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.commit();
        }

        verify(this.delegatedCon).commit();
    }

    @Test
    void rollback() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.rollback();
        }

        verify(this.delegatedCon).rollback();
    }

    @Test
    void close() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
        }

        verify(this.delegatedCon).close();
    }

    @Test
    void prepareStatement() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareStatement(null);
        }

        verify(this.delegatedCon).prepareStatement(null);
    }

    @Test
    void prepareStatement1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareStatement(null, 0);
        }

        verify(this.delegatedCon).prepareStatement(null, 0);
    }

    @Test
    void prepareStatement2() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareStatement(null, new int[0]);
        }

        verify(this.delegatedCon).prepareStatement(null, new int[0]);
    }

    @Test
    void prepareStatement3() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareStatement(null, new String[0]);
        }

        verify(this.delegatedCon).prepareStatement(null, new String[0]);
    }

    @Test
    void prepareStatement4() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareStatement(null, 0, 0);
        }

        verify(this.delegatedCon).prepareStatement(null, 0, 0);
    }

    @Test
    void prepareStatement5() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareStatement(null, 0, 0, 0);
        }

        verify(this.delegatedCon).prepareStatement(null, 0, 0, 0);
    }

    @Test
    void createStatement() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createStatement();
        }

        verify(this.delegatedCon).createStatement();
    }

    @Test
    void createStatement1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createStatement(0, 0);
        }

        verify(this.delegatedCon).createStatement(0, 0);
    }

    @Test
    void createStatement2() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createStatement(0, 0, 0);
        }

        verify(this.delegatedCon).createStatement(0, 0, 0);
    }

    @Test
    void prepareCall() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareCall(null);
        }

        verify(this.delegatedCon).prepareCall(null);
    }

    @Test
    void prepareCall1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareCall(null, 0, 0);
        }

        verify(this.delegatedCon).prepareCall(null, 0, 0);
    }

    @Test
    void prepareCall2() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.prepareCall(null, 0, 0, 0);
        }

        verify(this.delegatedCon).prepareCall(null, 0, 0, 0);
    }

    @Test
    void unwrap() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.unwrap(null);
        }

        verify(this.delegatedCon).unwrap(null);
    }

    @Test
    void isWrapperFor() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.isWrapperFor(null);
        }

        verify(this.delegatedCon).isWrapperFor(null);
    }

    @Test
    void nativeSQL() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.nativeSQL(null);
        }

        verify(this.delegatedCon).nativeSQL(null);
    }

    @Test
    void setAutoCommit() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setAutoCommit(true);
        }

        verify(this.delegatedCon).setAutoCommit(true);
    }

    @Test
    void getAutoCommit() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getAutoCommit();
        }

        verify(this.delegatedCon).getAutoCommit();
    }

    @Test
    void isClosed() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.isClosed();
        }

        verify(this.delegatedCon).isClosed();
    }

    @Test
    void getMetaData() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getMetaData();
        }

        verify(this.delegatedCon).getMetaData();
    }

    @Test
    void setReadOnly() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setReadOnly(true);
        }

        verify(this.delegatedCon).setReadOnly(true);
    }

    @Test
    void isReadOnly() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.isReadOnly();
        }

        verify(this.delegatedCon).isReadOnly();
    }

    @Test
    void setCatalog() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setCatalog(null);
        }

        verify(this.delegatedCon).setCatalog(null);
    }

    @Test
    void getCatalog() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getCatalog();
        }

        verify(this.delegatedCon).getCatalog();
    }

    @Test
    void setTransactionIsolation() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setTransactionIsolation(0);
        }

        verify(this.delegatedCon).setTransactionIsolation(0);
    }

    @Test
    void getTransactionIsolation() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getTransactionIsolation();
        }

        verify(this.delegatedCon).getTransactionIsolation();
    }

    @Test
    void getWarnings() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getWarnings();
        }

        verify(this.delegatedCon).getWarnings();
    }

    @Test
    void clearWarnings() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.clearWarnings();
        }

        verify(this.delegatedCon).clearWarnings();
    }

    @Test
    void getTypeMap() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getTypeMap();
        }

        verify(this.delegatedCon).getTypeMap();
    }

    @Test
    void setTypeMap() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setTypeMap(null);
        }

        verify(this.delegatedCon).setTypeMap(null);
    }

    @Test
    void setHoldability() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setHoldability(0);
        }

        verify(this.delegatedCon).setHoldability(0);
    }

    @Test
    void getHoldability() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getHoldability();
        }

        verify(this.delegatedCon).getHoldability();
    }

    @Test
    void setSavepoint() throws SQLException {
        int savepointId = 42;
        Savepoint savepoint = mock(Savepoint.class);
        when(savepoint.getSavepointId()).thenReturn(savepointId);
        when(this.delegatedCon.setSavepoint()).thenReturn(savepoint);
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setSavepoint();
        }

        verify(this.delegatedCon).setSavepoint();
    }

    @Test
    void setSavepoint1() throws SQLException {
        String savepointName = "Named";
        Savepoint savepoint = mock(Savepoint.class);
        when(savepoint.getSavepointName()).thenReturn(savepointName);
        when(this.delegatedCon.setSavepoint(savepointName)).thenReturn(savepoint);
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setSavepoint(savepointName);
        }

        verify(this.delegatedCon).setSavepoint(savepointName);
    }

    @Test
    void rollback1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.rollback(null);
        }

        verify(this.delegatedCon).rollback(null);
    }

    @Test
    void releaseSavepoint() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.releaseSavepoint(null);
        }

        verify(this.delegatedCon).releaseSavepoint(null);
    }

    @Test
    void createClob() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createClob();
        }

        verify(this.delegatedCon).createClob();
    }

    @Test
    void createBlob() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createBlob();
        }

        verify(this.delegatedCon).createBlob();
    }

    @Test
    void createNClob() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createNClob();
        }

        verify(this.delegatedCon).createNClob();
    }

    @Test
    void createSQLXML() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createSQLXML();
        }

        verify(this.delegatedCon).createSQLXML();
    }

    @Test
    void isValid() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.isValid(0);
        }

        verify(this.delegatedCon).isValid(0);
    }

    @Test
    void setClientInfo() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setClientInfo(null);
        }

        verify(this.delegatedCon).setClientInfo(null);
    }

    @Test
    void setClientInfo1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setClientInfo(null, null);
        }

        verify(this.delegatedCon).setClientInfo(null, null);
    }

    @Test
    void getClientInfo() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getClientInfo();
        }

        verify(this.delegatedCon).getClientInfo();
    }

    @Test
    void getClientInfo1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getClientInfo(null);
        }

        verify(this.delegatedCon).getClientInfo(null);
    }

    @Test
    void createArrayOf() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createArrayOf(null, null);
        }

        verify(this.delegatedCon).createArrayOf(null, null);
    }

    @Test
    void createStruct() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.createStruct(null, null);
        }

        verify(this.delegatedCon).createStruct(null, null);
    }

    @Test
    void setSchema() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setSchema(null);
        }

        verify(this.delegatedCon).setSchema(null);
    }

    @Test
    void getSchema() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getSchema();
        }

        verify(this.delegatedCon).getSchema();
    }

    @Test
    void abort() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.abort(null);
        }

        verify(this.delegatedCon).abort(null);
    }

    @Test
    void setNetworkTimeout() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setNetworkTimeout(null, 0);
        }

        verify(this.delegatedCon).setNetworkTimeout(null, 0);
    }

    @Test
    void getNetworkTimeout() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.getNetworkTimeout();
        }

        verify(this.delegatedCon).getNetworkTimeout();
    }
}