package chiroito.jfr4jdbc;

import chiroito.jfr4jdbc.event.jfr.JfrCloseEvent;
import chiroito.jfr4jdbc.event.jfr.JfrCommitEvent;
import chiroito.jfr4jdbc.event.jfr.JfrRollbackEvent;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
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

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrCommitEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getInt("connectionId") > 0);
    }

    @DisplayName("create RollbackEvent")
    @Test
    void createRollbackEvent() throws Exception {
        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        connection.rollback();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrRollbackEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getInt("connectionId") > 0);
    }

    @DisplayName("create CloseEvent")
    @Test
    void createCloseEvent() throws Exception {
        JfrConnection connection = new JfrConnection(this.delegatedCon);
        FlightRecording fr = FlightRecording.start();
        connection.close();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrCloseEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getInt("connectionId") > 0);
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
    void beginRequest() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.beginRequest();
        }

        verify(this.delegatedCon).beginRequest();
    }

    @Test
    void endRequest() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.endRequest();
        }

        verify(this.delegatedCon).endRequest();
    }

    @Test
    void setShardingKeyIfValid() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setShardingKeyIfValid(null, 0);
        }

        verify(this.delegatedCon).setShardingKeyIfValid(null, 0);
    }

    @Test
    void setShardingKeyIfValid1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setShardingKeyIfValid(null, null, 0);
        }

        verify(this.delegatedCon).setShardingKeyIfValid(null, null, 0);
    }

    @Test
    void setShardingKey() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setShardingKey(null);
        }

        verify(this.delegatedCon).setShardingKey(null);
    }

    @Test
    void setShardingKey1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setShardingKey(null, null);
        }

        verify(this.delegatedCon).setShardingKey(null, null);
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
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setSavepoint();
        }

        verify(this.delegatedCon).setSavepoint();
    }

    @Test
    void setSavepoint1() throws SQLException {
        try (Connection con = new JfrConnection(this.delegatedCon)) {
            con.setSavepoint(null);
        }

        verify(this.delegatedCon).setSavepoint(null);
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