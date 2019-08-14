package chiroito.jfr4jdbc;

import chiroito.jfr4jdbc.event.jfr.JfrCancelEvent;
import chiroito.jfr4jdbc.event.jfr.JfrStatementEvent;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JfrStatementTest {

    protected static final String SAMPLE_SQL = "SELECT 1 FROM DUAL";

    @Mock
    Statement delegateState;

    @BeforeAll
    static void initClass() throws Exception {
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("create StatementEvent")
    @Test
    void createStatementEvent() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeQuery(SAMPLE_SQL);
        TimeUnit.SECONDS.sleep(5);

        fr.stop();

        System.out.printf("start: "+System.nanoTime());
        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Statement")).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(SAMPLE_SQL.equals(event.getString("sql")));
        assertTrue(event.getInt("statementId") > 0);
        assertFalse(event.getBoolean("poolable"));
        assertFalse(event.getBoolean("closed"));
        assertTrue(event.getClass("statementClass") != null);
        assertFalse(event.getInt("connectionId") > 0);
        assertFalse(event.getBoolean("autoCommit"));
    }

    @DisplayName("create StatementEvent with Poolable")
    @Test
    void createStatementEventPoolable() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        when(delegateState.isPoolable()).thenReturn(true);

        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeQuery(SAMPLE_SQL);
        fr.stop();

        System.out.printf("start: "+System.nanoTime());
        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Statement")).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(SAMPLE_SQL.equals(event.getString("sql")));
        assertTrue(event.getInt("statementId") > 0);
        assertTrue(event.getBoolean("poolable"));
        assertFalse(event.getBoolean("closed"));
        assertTrue(event.getClass("statementClass") != null);
        assertFalse(event.getInt("connectionId") > 0);
        assertFalse(event.getBoolean("autoCommit"));
    }

    @DisplayName("create StatementEvent with Closed")
    @Test
    void createStatementEventClosed() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        when(delegateState.isClosed()).thenReturn(true);

        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeQuery(SAMPLE_SQL);
        fr.stop();

        System.out.printf("start: "+start.toString()+", end:"+LocalDateTime.now());
        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Statement")).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(SAMPLE_SQL.equals(event.getString("sql")));
        assertTrue(event.getInt("statementId") > 0);
        assertFalse(event.getBoolean("poolable"));
        assertTrue(event.getBoolean("closed"));
        assertTrue(event.getClass("statementClass") != null);
        assertFalse(event.getInt("connectionId") > 0);
        assertFalse(event.getBoolean("autoCommit"));
    }

    @DisplayName("create StatementEvent Connection")
    @Test
    void createStatementEventConnection() throws Exception {
        LocalDateTime start = LocalDateTime.now();
        Connection delegatedCon = mock(Connection.class);
        when(delegateState.getConnection()).thenReturn(delegatedCon);

        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeQuery(SAMPLE_SQL);
        fr.stop();

        System.out.printf("start: "+start.toString()+", end:"+LocalDateTime.now());
        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Statement")).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(SAMPLE_SQL.equals(event.getString("sql")));
        assertTrue(event.getInt("statementId") > 0);
        assertFalse(event.getBoolean("poolable"));
        assertFalse(event.getBoolean("closed"));
        assertTrue(event.getClass("statementClass") != null);
        assertTrue(event.getInt("connectionId") > 0);
        assertFalse(event.getBoolean("autoCommit"));
    }

    @DisplayName("create StatementEvent Auto Commit")
    @Test
    void createStatementEventAutoCommit() throws Exception {
        LocalDateTime start = LocalDateTime.now();

        Connection delegatedCon = mock(Connection.class);
        when(delegateState.getConnection()).thenReturn(delegatedCon);
        when(delegatedCon.getAutoCommit()).thenReturn(true);

        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeQuery(SAMPLE_SQL);
        fr.stop();

        System.out.printf("start: "+start.toString()+", end:"+LocalDateTime.now());
        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Statement")).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(SAMPLE_SQL.equals(event.getString("sql")));
        assertTrue(event.getInt("statementId") > 0);
        assertFalse(event.getBoolean("poolable"));
        assertFalse(event.getBoolean("closed"));
        assertTrue(event.getClass("statementClass") != null);
        assertTrue(event.getInt("connectionId") > 0);
        assertTrue(event.getBoolean("autoCommit"));
    }

    @DisplayName("return JfrResultSet by executeQuery")
    @Test
    void returnJfrResultSetByExecuteQuery() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        ResultSet resultSet = statement.executeQuery(SAMPLE_SQL);

        assertEquals(JfrResultSet.class, resultSet.getClass());
    }

    @DisplayName("create ResultSetEvent by getGeneratedKeys")
    @Test
    void createResultSetEventByGetGeneratedKeys() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.getGeneratedKeys();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("return JfrResultSet by getGeneratedKeys")
    @Test
    void returnJfrResultSetByGetGeneratedKeys() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        ResultSet resultSet = statement.getGeneratedKeys();

        assertEquals(JfrResultSet.class, resultSet.getClass());
    }

    @DisplayName("create JfrStatementEvent by executeUpdate")
    @Test
    void createStatementEventByExecuteUpdate() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeUpdate(SAMPLE_SQL);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrStatementEvent by executeUpdate(sql, autoGeneratedKeys)")
    @Test
    void createStatementEventByExecuteUpdate1() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeUpdate(SAMPLE_SQL, 0);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrStatementEvent by executeUpdate(sql, columnIndexes)")
    @Test
    void createStatementEventByExecuteUpdate2() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeUpdate(SAMPLE_SQL, (int[]) null);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrStatementEvent by executeUpdate(sql, columnNames)")
    @Test
    void createStatementEventByExecuteUpdate3() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeUpdate(SAMPLE_SQL, (String[]) null);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrStatementEvent by execute")
    @Test
    void createStatementEventByExecute() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.execute(SAMPLE_SQL);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrStatementEvent by execute(sql, autoGeneratedKeys)")
    @Test
    void createStatementEventByExecute1() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.execute(SAMPLE_SQL, 0);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrStatementEvent by execute(sql, columnIndexes)")
    @Test
    void createStatementEventByExecute2() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.execute(SAMPLE_SQL, (int[]) null);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrStatementEvent by execute(sql, columnNames)")
    @Test
    void createStatementEventByExecute3() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.execute(SAMPLE_SQL, (String[]) null);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrStatementEvent by executeBatch")
    @Test
    void createStatementEventByExecuteBatch() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.executeBatch();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("create JfrCancelEvent by cancel")
    @Test
    void createStatementEventByCancel() throws Exception {
        JfrStatement statement = new JfrStatement(this.delegateState);
        FlightRecording fr = FlightRecording.start();
        statement.cancel();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrCancelEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
    }

    @DisplayName("addBatch")
    @Test
    void addBatch() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.addBatch(null);
        verify(this.delegateState).addBatch(null);
    }

    @DisplayName("cancel")
    @Test
    void cancel() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.cancel();
        verify(this.delegateState).cancel();
    }

    @DisplayName("clearBatch")
    @Test
    void clearBatch() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.clearBatch();
        verify(this.delegateState).clearBatch();
    }

    @DisplayName("clearWarnings")
    @Test
    void clearWarnings() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.clearWarnings();
        verify(this.delegateState).clearWarnings();
    }

    @DisplayName("close")
    @Test
    void close() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.close();
        verify(this.delegateState).close();
    }

    @DisplayName("closeOnCompletion")
    @Test
    void closeOnCompletion() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.closeOnCompletion();
        verify(this.delegateState).closeOnCompletion();
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

    @DisplayName("execute")
    @Test
    void execute() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.execute(null);
        verify(this.delegateState).execute(null);
    }

    @DisplayName("execute")
    @Test
    void execute1() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.execute(null, 0);
        verify(this.delegateState).execute(null, 0);
    }

    @DisplayName("execute")
    @Test
    void execute2() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.execute(null, (int[]) null);
        verify(this.delegateState).execute(null, (int[]) null);
    }

    @DisplayName("execute")
    @Test
    void execute3() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.execute(null, (String[]) null);
        verify(this.delegateState).execute(null, (String[]) null);
    }

    @DisplayName("executeBatch")
    @Test
    void executeBatch() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeBatch();
        verify(this.delegateState).executeBatch();
    }

    @DisplayName("executeLargeBatch")
    @Test
    void executeLargeBatch() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeLargeBatch();
        verify(this.delegateState).executeLargeBatch();
    }

    @DisplayName("executeLargeUpdate")
    @Test
    void executeLargeUpdate() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeLargeUpdate(null);
        verify(this.delegateState).executeLargeUpdate(null);
    }

    @DisplayName("executeLargeUpdate")
    @Test
    void executeLargeUpdate1() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeLargeUpdate(null, 0);
        verify(this.delegateState).executeLargeUpdate(null, 0);
    }

    @DisplayName("executeLargeUpdate")
    @Test
    void executeLargeUpdate2() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeLargeUpdate(null, (int[]) null);
        verify(this.delegateState).executeLargeUpdate(null, (int[]) null);
    }

    @DisplayName("executeLargeUpdate")
    @Test
    void executeLargeUpdate3() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeLargeUpdate(null, (String[]) null);
        verify(this.delegateState).executeLargeUpdate(null, (String[]) null);
    }

    @DisplayName("executeQuery")
    @Test
    void executeQuery() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeQuery(null);
        verify(this.delegateState).executeQuery(null);
    }

    @DisplayName("executeUpdate")
    @Test
    void executeUpdate() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeUpdate(null);
        verify(this.delegateState).executeUpdate(null);
    }

    @DisplayName("executeUpdate")
    @Test
    void executeUpdate1() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeUpdate(null, 0);
        verify(this.delegateState).executeUpdate(null, 0);
    }

    @DisplayName("executeUpdate")
    @Test
    void executeUpdate2() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeUpdate(null, (int[]) null);
        verify(this.delegateState).executeUpdate(null, (int[]) null);
    }

    @DisplayName("executeUpdate")
    @Test
    void executeUpdate3() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.executeUpdate(null, (String[]) null);
        verify(this.delegateState).executeUpdate(null, (String[]) null);
    }

    @DisplayName("getConnection")
    @Test
    void getConnection() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getConnection();
        verify(this.delegateState).getConnection();
    }

    @DisplayName("getFetchDirection")
    @Test
    void getFetchDirection() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getFetchDirection();
        verify(this.delegateState).getFetchDirection();
    }

    @DisplayName("getFetchSize")
    @Test
    void getFetchSize() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getFetchSize();
        verify(this.delegateState).getFetchSize();
    }

    @DisplayName("getGeneratedKeys")
    @Test
    void getGeneratedKeys() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getGeneratedKeys();
        verify(this.delegateState).getGeneratedKeys();
    }

    @DisplayName("getLargeMaxRows")
    @Test
    void getLargeMaxRows() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getLargeMaxRows();
        verify(this.delegateState).getLargeMaxRows();
    }

    @DisplayName("getLargeUpdateCount")
    @Test
    void getLargeUpdateCount() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getLargeUpdateCount();
        verify(this.delegateState).getLargeUpdateCount();
    }

    @DisplayName("getMaxFieldSize")
    @Test
    void getMaxFieldSize() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getMaxFieldSize();
        verify(this.delegateState).getMaxFieldSize();
    }

    @DisplayName("getMaxRows")
    @Test
    void getMaxRows() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getMaxRows();
        verify(this.delegateState).getMaxRows();
    }

    @DisplayName("getMoreResults")
    @Test
    void getMoreResults() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getMoreResults();
        verify(this.delegateState).getMoreResults();
    }

    @DisplayName("getMoreResults")
    @Test
    void getMoreResults1() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getMoreResults(0);
        verify(this.delegateState).getMoreResults(0);
    }

    @DisplayName("getQueryTimeout")
    @Test
    void getQueryTimeout() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getQueryTimeout();
        verify(this.delegateState).getQueryTimeout();
    }

    @DisplayName("getResultSet")
    @Test
    void getResultSet() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getResultSet();
        verify(this.delegateState).getResultSet();
    }

    @DisplayName("getResultSetConcurrency")
    @Test
    void getResultSetConcurrency() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getResultSetConcurrency();
        verify(this.delegateState).getResultSetConcurrency();
    }

    @DisplayName("getResultSetHoldability")
    @Test
    void getResultSetHoldability() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getResultSetHoldability();
        verify(this.delegateState).getResultSetHoldability();
    }

    @DisplayName("getResultSetType")
    @Test
    void getResultSetType() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getResultSetType();
        verify(this.delegateState).getResultSetType();
    }

    @DisplayName("getUpdateCount")
    @Test
    void getUpdateCount() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getUpdateCount();
        verify(this.delegateState).getUpdateCount();
    }

    @DisplayName("getWarnings")
    @Test
    void getWarnings() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.getWarnings();
        verify(this.delegateState).getWarnings();
    }

    @DisplayName("isCloseOnCompletion")
    @Test
    void isCloseOnCompletion() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.isCloseOnCompletion();
        verify(this.delegateState).isCloseOnCompletion();
    }

    @DisplayName("isClosed")
    @Test
    void isClosed() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.isClosed();
        verify(this.delegateState).isClosed();
    }

    @DisplayName("isPoolable")
    @Test
    void isPoolable() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.isPoolable();
        verify(this.delegateState).isPoolable();
    }

    @DisplayName("isSimpleIdentifier")
    @Test
    void isSimpleIdentifier() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.isSimpleIdentifier(null);
        verify(this.delegateState).isSimpleIdentifier(null);
    }

    @DisplayName("isWrapperFor")
    @Test
    void isWrapperFor() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.isWrapperFor(null);
        verify(this.delegateState).isWrapperFor(null);
    }

    @DisplayName("setCursorName")
    @Test
    void setCursorName() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setCursorName(null);
        verify(this.delegateState).setCursorName(null);
    }

    @DisplayName("setEscapeProcessing")
    @Test
    void setEscapeProcessing() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setEscapeProcessing(false);
        verify(this.delegateState).setEscapeProcessing(false);
    }

    @DisplayName("setFetchDirection")
    @Test
    void setFetchDirection() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setFetchDirection(0);
        verify(this.delegateState).setFetchDirection(0);
    }

    @DisplayName("setFetchSize")
    @Test
    void setFetchSize() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setFetchSize(0);
        verify(this.delegateState).setFetchSize(0);
    }

    @DisplayName("setLargeMaxRows")
    @Test
    void setLargeMaxRows() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setLargeMaxRows(0L);
        verify(this.delegateState).setLargeMaxRows(0L);
    }

    @DisplayName("setMaxFieldSize")
    @Test
    void setMaxFieldSize() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setMaxFieldSize(0);
        verify(this.delegateState).setMaxFieldSize(0);
    }

    @DisplayName("setMaxRows")
    @Test
    void setMaxRows() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setMaxRows(0);
        verify(this.delegateState).setMaxRows(0);
    }

    @DisplayName("setPoolable")
    @Test
    void setPoolable() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setPoolable(false);
        verify(this.delegateState).setPoolable(false);
    }

    @DisplayName("setQueryTimeout")
    @Test
    void setQueryTimeout() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.setQueryTimeout(0);
        verify(this.delegateState).setQueryTimeout(0);
    }

    @DisplayName("unwrap")
    @Test
    void unwrap() throws SQLException {
        JfrStatement statement = new JfrStatement(this.delegateState);
        statement.unwrap(null);
        verify(this.delegateState).unwrap(null);
    }

}