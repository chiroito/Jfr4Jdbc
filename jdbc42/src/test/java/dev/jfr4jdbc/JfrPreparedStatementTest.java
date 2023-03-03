package dev.jfr4jdbc;

import dev.jfr4jdbc.event.jfr.JfrStatementEvent;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(2)
class JfrPreparedStatementTest {

    @Mock
    private PreparedStatement delegatePstate;

    @BeforeAll
    static void initClass() throws Exception {
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("create StatementEvent by executeQuery")
    @Test
    void createStatementEventByExecuteQuery() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        FlightRecording fr = FlightRecording.start();
        statement.executeQuery();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getBoolean("prepared"));
    }

    @DisplayName("create StatementEvent by executeQuery with Null Param")
    @Test
    void createStatementEventByExecuteQueryWithNullParam() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        statement.setString(0, null);
        FlightRecording fr = FlightRecording.start();
        statement.executeQuery();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getBoolean("prepared"));
    }

    @DisplayName("return JfrResultSet by executeQuery")
    @Test
    void returnJfrResultSetByExecuteQuery() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        ResultSet resultSet = statement.executeQuery();

        assertEquals(JfrResultSet.class, resultSet.getClass());
    }

    @DisplayName("create StatementEvent by executeUpdate")
    @Test
    void createStatementEventByExecuteUpdate() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        FlightRecording fr = FlightRecording.start();
        statement.executeUpdate();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getBoolean("prepared"));
    }

    @DisplayName("create StatementEvent by executeUpdate with Null Param")
    @Test
    void createStatementEventByExecuteUpdateWithNullParam() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        statement.setString(0, null);
        FlightRecording fr = FlightRecording.start();
        statement.executeUpdate();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getBoolean("prepared"));
    }

    @DisplayName("create StatementEvent by execute")
    @Test
    void createStatementEventByExecute() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        FlightRecording fr = FlightRecording.start();
        statement.execute();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getBoolean("prepared"));
    }

    @DisplayName("create StatementEvent by execute with Null Param")
    @Test
    void createStatementEventByExecuteWithNullParam() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        statement.setString(0, null);
        FlightRecording fr = FlightRecording.start();
        statement.execute();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getBoolean("prepared"));
    }

    @DisplayName("addBatch")
    @Test
    void addBatch() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.addBatch();
        verify(this.delegatePstate).addBatch();
    }

    @DisplayName("clearParameters")
    @Test
    void clearParameters() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.clearParameters();
        verify(this.delegatePstate).clearParameters();
    }

    @DisplayName("execute")
    @Test
    void execute() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.execute();
        verify(this.delegatePstate).execute();
    }

    @DisplayName("executeLargeUpdate")
    @Test
    void executeLargeUpdate() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.executeLargeUpdate();
        verify(this.delegatePstate).executeLargeUpdate();
    }

    @DisplayName("executeQuery")
    @Test
    void executeQuery() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.executeQuery();
        verify(this.delegatePstate).executeQuery();
    }

    @DisplayName("executeUpdate")
    @Test
    void executeUpdate() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.executeUpdate();
        verify(this.delegatePstate).executeUpdate();
    }

    @DisplayName("getMetaData")
    @Test
    void getMetaData() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.getMetaData();
        verify(this.delegatePstate).getMetaData();
    }

    @DisplayName("getParameterMetaData")
    @Test
    void getParameterMetaData() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.getParameterMetaData();
        verify(this.delegatePstate).getParameterMetaData();
    }

    @DisplayName("setArray")
    @Test
    void setArray() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setArray(0, null);
        verify(this.delegatePstate).setArray(0, null);
    }

    @DisplayName("setAsciiStream")
    @Test
    void setAsciiStream() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setAsciiStream(0, null);
        verify(this.delegatePstate).setAsciiStream(0, null);
    }

    @DisplayName("setAsciiStream")
    @Test
    void setAsciiStream1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setAsciiStream(0, null, 0);
        verify(this.delegatePstate).setAsciiStream(0, null, 0);
    }

    @DisplayName("setAsciiStream")
    @Test
    void setAsciiStream2() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setAsciiStream(0, null, 0L);
        verify(this.delegatePstate).setAsciiStream(0, null, 0L);
    }

    @DisplayName("setBigDecimal")
    @Test
    void setBigDecimal() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBigDecimal(0, null);
        verify(this.delegatePstate).setBigDecimal(0, null);
    }

    @DisplayName("setBinaryStream")
    @Test
    void setBinaryStream() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBinaryStream(0, null);
        verify(this.delegatePstate).setBinaryStream(0, null);
    }

    @DisplayName("setBinaryStream")
    @Test
    void setBinaryStream1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBinaryStream(0, null, 0);
        verify(this.delegatePstate).setBinaryStream(0, null, 0);
    }

    @DisplayName("setBinaryStream")
    @Test
    void setBinaryStream2() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBinaryStream(0, null, 0L);
        verify(this.delegatePstate).setBinaryStream(0, null, 0L);
    }

    @DisplayName("setBlob")
    @Test
    void setBlob() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBlob(0, (InputStream) null);
        verify(this.delegatePstate).setBlob(0, (InputStream) null);
    }

    @DisplayName("setBlob")
    @Test
    void setBlob1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBlob(0, (Blob) null);
        verify(this.delegatePstate).setBlob(0, (Blob) null);
    }

    @DisplayName("setBlob")
    @Test
    void setBlob2() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBlob(0, null, 0L);
        verify(this.delegatePstate).setBlob(0, null, 0L);
    }

    @DisplayName("setBoolean")
    @Test
    void setBoolean() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBoolean(0, false);
        verify(this.delegatePstate).setBoolean(0, false);
    }

    @DisplayName("setByte")
    @Test
    void setByte() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setByte(0, (byte) 0);
        verify(this.delegatePstate).setByte(0, (byte) 0);
    }

    @DisplayName("setBytes")
    @Test
    void setBytes() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setBytes(0, null);
        verify(this.delegatePstate).setBytes(0, null);
    }

    @DisplayName("setCharacterStream")
    @Test
    void setCharacterStream() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setCharacterStream(0, null);
        verify(this.delegatePstate).setCharacterStream(0, null);
    }

    @DisplayName("setCharacterStream")
    @Test
    void setCharacterStream1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setCharacterStream(0, null, 0);
        verify(this.delegatePstate).setCharacterStream(0, null, 0);
    }

    @DisplayName("setCharacterStream")
    @Test
    void setCharacterStream2() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setCharacterStream(0, null, 0L);
        verify(this.delegatePstate).setCharacterStream(0, null, 0L);
    }

    @DisplayName("setClob")
    @Test
    void setClob() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setClob(0, (Reader) null);
        verify(this.delegatePstate).setClob(0, (Reader) null);
    }

    @DisplayName("setClob")
    @Test
    void setClob1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setClob(0, (Clob) null);
        verify(this.delegatePstate).setClob(0, (Clob) null);
    }

    @DisplayName("setClob")
    @Test
    void setClob2() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setClob(0, null, 0L);
        verify(this.delegatePstate).setClob(0, null, 0L);
    }

    @DisplayName("setDate")
    @Test
    void setDate() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setDate(0, null);
        verify(this.delegatePstate).setDate(0, null);
    }

    @DisplayName("setDate")
    @Test
    void setDate1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setDate(0, null, null);
        verify(this.delegatePstate).setDate(0, null, null);
    }

    @DisplayName("setDouble")
    @Test
    void setDouble() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setDouble(0, 0.0d);
        verify(this.delegatePstate).setDouble(0, 0.0d);
    }

    @DisplayName("setFloat")
    @Test
    void setFloat() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setFloat(0, 0.0f);
        verify(this.delegatePstate).setFloat(0, 0.0f);
    }

    @DisplayName("setInt")
    @Test
    void setInt() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setInt(0, 0);
        verify(this.delegatePstate).setInt(0, 0);
    }

    @DisplayName("setLong")
    @Test
    void setLong() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setLong(0, 0L);
        verify(this.delegatePstate).setLong(0, 0L);
    }

    @DisplayName("setNCharacterStream")
    @Test
    void setNCharacterStream() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setNCharacterStream(0, null);
        verify(this.delegatePstate).setNCharacterStream(0, null);
    }

    @DisplayName("setNCharacterStream")
    @Test
    void setNCharacterStream1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setNCharacterStream(0, null, 0L);
        verify(this.delegatePstate).setNCharacterStream(0, null, 0L);
    }

    @DisplayName("setNClob")
    @Test
    void setNClob() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setNClob(0, (Reader) null);
        verify(this.delegatePstate).setNClob(0, (Reader) null);
    }

    @DisplayName("setNClob")
    @Test
    void setNClob1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setNClob(0, (NClob) null);
        verify(this.delegatePstate).setNClob(0, (NClob) null);
    }

    @DisplayName("setNClob")
    @Test
    void setNClob2() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setNClob(0, null, 0L);
        verify(this.delegatePstate).setNClob(0, null, 0L);
    }

    @DisplayName("setNString")
    @Test
    void setNString() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setNString(0, null);
        verify(this.delegatePstate).setNString(0, null);
    }

    @DisplayName("setNull")
    @Test
    void setNull() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setNull(0, 0);
        verify(this.delegatePstate).setNull(0, 0);
    }

    @DisplayName("setNull")
    @Test
    void setNull1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setNull(0, 0, null);
        verify(this.delegatePstate).setNull(0, 0, null);
    }

    @DisplayName("setObject")
    @Test
    void setObject() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setObject(0, null);
        verify(this.delegatePstate).setObject(0, null);
    }

    @DisplayName("setObject")
    @Test
    void setObject1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setObject(0, null, 0);
        verify(this.delegatePstate).setObject(0, null, 0);
    }

    @DisplayName("setObject")
    @Test
    void setObject2() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setObject(0, null, 0, 0);
        verify(this.delegatePstate).setObject(0, null, 0, 0);
    }

    @DisplayName("setObject")
    @Test
    void setObject3() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setObject(0, null, null);
        verify(this.delegatePstate).setObject(0, null, null);
    }

    @DisplayName("setObject")
    @Test
    void setObject4() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setObject(0, null, null, 0);
        verify(this.delegatePstate).setObject(0, null, null, 0);
    }

    @DisplayName("setRef")
    @Test
    void setRef() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setRef(0, null);
        verify(this.delegatePstate).setRef(0, null);
    }

    @DisplayName("setRowId")
    @Test
    void setRowId() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setRowId(0, null);
        verify(this.delegatePstate).setRowId(0, null);
    }

    @DisplayName("setSQLXML")
    @Test
    void setSQLXML() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setSQLXML(0, null);
        verify(this.delegatePstate).setSQLXML(0, null);
    }

    @DisplayName("setShort")
    @Test
    void setShort() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setShort(0, (short) 0);
        verify(this.delegatePstate).setShort(0, (short) 0);
    }

    @DisplayName("setString")
    @Test
    void setString() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setString(0, null);
        verify(this.delegatePstate).setString(0, null);
    }

    @DisplayName("setTime")
    @Test
    void setTime() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setTime(0, null);
        verify(this.delegatePstate).setTime(0, null);
    }

    @DisplayName("setTime")
    @Test
    void setTime1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setTime(0, null, null);
        verify(this.delegatePstate).setTime(0, null, null);
    }

    @DisplayName("setTimestamp")
    @Test
    void setTimestamp() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setTimestamp(0, null);
        verify(this.delegatePstate).setTimestamp(0, null);
    }

    @DisplayName("setTimestamp")
    @Test
    void setTimestamp1() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setTimestamp(0, null, null);
        verify(this.delegatePstate).setTimestamp(0, null, null);
    }

    @DisplayName("setURL")
    @Test
    void setURL() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setURL(0, null);
        verify(this.delegatePstate).setURL(0, null);
    }

    @DisplayName("setUnicodeStream")
    @Test
    void setUnicodeStream() throws SQLException {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, "sql");
        statement.setUnicodeStream(0, null, 0);
        verify(this.delegatePstate).setUnicodeStream(0, null, 0);
    }

}