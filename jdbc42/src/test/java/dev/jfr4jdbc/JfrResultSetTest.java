package dev.jfr4jdbc;

import dev.jfr4jdbc.event.jfr.JfrResultSetEvent;
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
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(3)
class JfrResultSetTest {

    @Mock
    private ResultSet delegateResultSet;

    @BeforeAll
    static void initClass() throws Exception {
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("create ResultSetEvent")
    @Test
    void createStatementEvent() throws Exception {
        when(this.delegateResultSet.getRow()).thenReturn(1);

        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        FlightRecording fr = FlightRecording.start();
        resultSet.next();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrResultSetEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals(1, event.getInt("rowNo"));
        assertEquals(0, event.getInt("connectionId"));
        assertEquals(0, event.getInt("statementId"));
        assertEquals(0, event.getInt("resultSetId"));
        assertTrue(event.getClass("resultSetClass") != null);
    }

    @DisplayName("absolute")
    @Test
    void absolute() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.absolute(0);
        verify(this.delegateResultSet).absolute(0);
    }

    @DisplayName("afterLast")
    @Test
    void afterLast() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.afterLast();
        verify(this.delegateResultSet).afterLast();
    }

    @DisplayName("beforeFirst")
    @Test
    void beforeFirst() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.beforeFirst();
        verify(this.delegateResultSet).beforeFirst();
    }

    @DisplayName("cancelRowUpdates")
    @Test
    void cancelRowUpdates() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.cancelRowUpdates();
        verify(this.delegateResultSet).cancelRowUpdates();
    }

    @DisplayName("clearWarnings")
    @Test
    void clearWarnings() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.clearWarnings();
        verify(this.delegateResultSet).clearWarnings();
    }

    @DisplayName("close")
    @Test
    void close() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.close();
        verify(this.delegateResultSet).close();
    }

    @DisplayName("deleteRow")
    @Test
    void deleteRow() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.deleteRow();
        verify(this.delegateResultSet).deleteRow();
    }

    @DisplayName("findColumn")
    @Test
    void findColumn() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.findColumn(null);
        verify(this.delegateResultSet).findColumn(null);
    }

    @DisplayName("first")
    @Test
    void first() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.first();
        verify(this.delegateResultSet).first();
    }

    @DisplayName("getArray")
    @Test
    void getArray() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getArray(0);
        verify(this.delegateResultSet).getArray(0);
    }

    @DisplayName("getArray")
    @Test
    void getArray1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getArray(null);
        verify(this.delegateResultSet).getArray(null);
    }

    @DisplayName("getAsciiStream")
    @Test
    void getAsciiStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getAsciiStream(0);
        verify(this.delegateResultSet).getAsciiStream(0);
    }

    @DisplayName("getAsciiStream")
    @Test
    void getAsciiStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getAsciiStream(null);
        verify(this.delegateResultSet).getAsciiStream(null);
    }

    @DisplayName("getBigDecimal")
    @Test
    void getBigDecimal() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBigDecimal(0);
        verify(this.delegateResultSet).getBigDecimal(0);
    }

    @DisplayName("getBigDecimal")
    @Test
    void getBigDecimal1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBigDecimal(0, 0);
        verify(this.delegateResultSet).getBigDecimal(0, 0);
    }

    @DisplayName("getBigDecimal")
    @Test
    void getBigDecimal2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBigDecimal(null);
        verify(this.delegateResultSet).getBigDecimal(null);
    }

    @DisplayName("getBigDecimal")
    @Test
    void getBigDecimal3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBigDecimal(null, 0);
        verify(this.delegateResultSet).getBigDecimal(null, 0);
    }

    @DisplayName("getBinaryStream")
    @Test
    void getBinaryStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBinaryStream(0);
        verify(this.delegateResultSet).getBinaryStream(0);
    }

    @DisplayName("getBinaryStream")
    @Test
    void getBinaryStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBinaryStream(null);
        verify(this.delegateResultSet).getBinaryStream(null);
    }

    @DisplayName("getBlob")
    @Test
    void getBlob() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBlob(0);
        verify(this.delegateResultSet).getBlob(0);
    }

    @DisplayName("getBlob")
    @Test
    void getBlob1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBlob(null);
        verify(this.delegateResultSet).getBlob(null);
    }

    @DisplayName("getBoolean")
    @Test
    void getBoolean() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBoolean(0);
        verify(this.delegateResultSet).getBoolean(0);
    }

    @DisplayName("getBoolean")
    @Test
    void getBoolean1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBoolean(null);
        verify(this.delegateResultSet).getBoolean(null);
    }

    @DisplayName("getByte")
    @Test
    void getByte() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getByte(0);
        verify(this.delegateResultSet).getByte(0);
    }

    @DisplayName("getByte")
    @Test
    void getByte1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getByte(null);
        verify(this.delegateResultSet).getByte(null);
    }

    @DisplayName("getBytes")
    @Test
    void getBytes() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBytes(0);
        verify(this.delegateResultSet).getBytes(0);
    }

    @DisplayName("getBytes")
    @Test
    void getBytes1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getBytes(null);
        verify(this.delegateResultSet).getBytes(null);
    }

    @DisplayName("getCharacterStream")
    @Test
    void getCharacterStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getCharacterStream(0);
        verify(this.delegateResultSet).getCharacterStream(0);
    }

    @DisplayName("getCharacterStream")
    @Test
    void getCharacterStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getCharacterStream(null);
        verify(this.delegateResultSet).getCharacterStream(null);
    }

    @DisplayName("getClob")
    @Test
    void getClob() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getClob(0);
        verify(this.delegateResultSet).getClob(0);
    }

    @DisplayName("getClob")
    @Test
    void getClob1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getClob(null);
        verify(this.delegateResultSet).getClob(null);
    }

    @DisplayName("getConcurrency")
    @Test
    void getConcurrency() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getConcurrency();
        verify(this.delegateResultSet).getConcurrency();
    }

    @DisplayName("getCursorName")
    @Test
    void getCursorName() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getCursorName();
        verify(this.delegateResultSet).getCursorName();
    }

    @DisplayName("getDate")
    @Test
    void getDate() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getDate(0);
        verify(this.delegateResultSet).getDate(0);
    }

    @DisplayName("getDate")
    @Test
    void getDate1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getDate(0, null);
        verify(this.delegateResultSet).getDate(0, null);
    }

    @DisplayName("getDate")
    @Test
    void getDate2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getDate(null);
        verify(this.delegateResultSet).getDate(null);
    }

    @DisplayName("getDate")
    @Test
    void getDate3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getDate(null, null);
        verify(this.delegateResultSet).getDate(null, null);
    }

    @DisplayName("getDouble")
    @Test
    void getDouble() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getDouble(0);
        verify(this.delegateResultSet).getDouble(0);
    }

    @DisplayName("getDouble")
    @Test
    void getDouble1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getDouble(null);
        verify(this.delegateResultSet).getDouble(null);
    }

    @DisplayName("getFetchDirection")
    @Test
    void getFetchDirection() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getFetchDirection();
        verify(this.delegateResultSet).getFetchDirection();
    }

    @DisplayName("getFetchSize")
    @Test
    void getFetchSize() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getFetchSize();
        verify(this.delegateResultSet).getFetchSize();
    }

    @DisplayName("getFloat")
    @Test
    void getFloat() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getFloat(0);
        verify(this.delegateResultSet).getFloat(0);
    }

    @DisplayName("getFloat")
    @Test
    void getFloat1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getFloat(null);
        verify(this.delegateResultSet).getFloat(null);
    }

    @DisplayName("getHoldability")
    @Test
    void getHoldability() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getHoldability();
        verify(this.delegateResultSet).getHoldability();
    }

    @DisplayName("getInt")
    @Test
    void getInt() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getInt(0);
        verify(this.delegateResultSet).getInt(0);
    }

    @DisplayName("getInt")
    @Test
    void getInt1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getInt(null);
        verify(this.delegateResultSet).getInt(null);
    }

    @DisplayName("getLong")
    @Test
    void getLong() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getLong(0);
        verify(this.delegateResultSet).getLong(0);
    }

    @DisplayName("getLong")
    @Test
    void getLong1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getLong(null);
        verify(this.delegateResultSet).getLong(null);
    }

    @DisplayName("getMetaData")
    @Test
    void getMetaData() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getMetaData();
        verify(this.delegateResultSet).getMetaData();
    }

    @DisplayName("getNCharacterStream")
    @Test
    void getNCharacterStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getNCharacterStream(0);
        verify(this.delegateResultSet).getNCharacterStream(0);
    }

    @DisplayName("getNCharacterStream")
    @Test
    void getNCharacterStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getNCharacterStream(null);
        verify(this.delegateResultSet).getNCharacterStream(null);
    }

    @DisplayName("getNClob")
    @Test
    void getNClob() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getNClob(0);
        verify(this.delegateResultSet).getNClob(0);
    }

    @DisplayName("getNClob")
    @Test
    void getNClob1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getNClob(null);
        verify(this.delegateResultSet).getNClob(null);
    }

    @DisplayName("getNString")
    @Test
    void getNString() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getNString(0);
        verify(this.delegateResultSet).getNString(0);
    }

    @DisplayName("getNString")
    @Test
    void getNString1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getNString(null);
        verify(this.delegateResultSet).getNString(null);
    }

    @DisplayName("getObject")
    @Test
    void getObject() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getObject(0);
        verify(this.delegateResultSet).getObject(0);
    }

    @DisplayName("getObject")
    @Test
    void getObject1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getObject(0, (Map<String, Class<?>>) null);
        verify(this.delegateResultSet).getObject(0, (Map<String, Class<?>>) null);
    }

    @DisplayName("getObject")
    @Test
    void getObject2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getObject(0, (Class<Object>) null);
        verify(this.delegateResultSet).getObject(0, (Class<Object>) null);
    }

    @DisplayName("getObject")
    @Test
    void getObject3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getObject(null);
        verify(this.delegateResultSet).getObject(null);
    }

    @DisplayName("getObject")
    @Test
    void getObject4() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getObject(null, (Map<String, Class<?>>) null);
        verify(this.delegateResultSet).getObject(null, (Map<String, Class<?>>) null);
    }

    @DisplayName("getObject")
    @Test
    void getObject5() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getObject(null, (Class<Object>) null);
        verify(this.delegateResultSet).getObject(null, (Class<Object>) null);
    }

    @DisplayName("getRef")
    @Test
    void getRef() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getRef(0);
        verify(this.delegateResultSet).getRef(0);
    }

    @DisplayName("getRef")
    @Test
    void getRef1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getRef(null);
        verify(this.delegateResultSet).getRef(null);
    }

    @DisplayName("getRow")
    @Test
    void getRow() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getRow();
        verify(this.delegateResultSet).getRow();
    }

    @DisplayName("getRowId")
    @Test
    void getRowId() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getRowId(0);
        verify(this.delegateResultSet).getRowId(0);
    }

    @DisplayName("getRowId")
    @Test
    void getRowId1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getRowId(null);
        verify(this.delegateResultSet).getRowId(null);
    }

    @DisplayName("getSQLXML")
    @Test
    void getSQLXML() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getSQLXML(0);
        verify(this.delegateResultSet).getSQLXML(0);
    }

    @DisplayName("getSQLXML")
    @Test
    void getSQLXML1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getSQLXML(null);
        verify(this.delegateResultSet).getSQLXML(null);
    }

    @DisplayName("getShort")
    @Test
    void getShort() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getShort(0);
        verify(this.delegateResultSet).getShort(0);
    }

    @DisplayName("getShort")
    @Test
    void getShort1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getShort(null);
        verify(this.delegateResultSet).getShort(null);
    }

    @DisplayName("getStatement")
    @Test
    void getStatement() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getStatement();
        verify(this.delegateResultSet).getStatement();
    }

    @DisplayName("getString")
    @Test
    void getString() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getString(0);
        verify(this.delegateResultSet).getString(0);
    }

    @DisplayName("getString")
    @Test
    void getString1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getString(null);
        verify(this.delegateResultSet).getString(null);
    }

    @DisplayName("getTime")
    @Test
    void getTime() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getTime(0);
        verify(this.delegateResultSet).getTime(0);
    }

    @DisplayName("getTime")
    @Test
    void getTime1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getTime(0, null);
        verify(this.delegateResultSet).getTime(0, null);
    }

    @DisplayName("getTime")
    @Test
    void getTime2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getTime(null);
        verify(this.delegateResultSet).getTime(null);
    }

    @DisplayName("getTime")
    @Test
    void getTime3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getTime(null, null);
        verify(this.delegateResultSet).getTime(null, null);
    }

    @DisplayName("getTimestamp")
    @Test
    void getTimestamp() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getTimestamp(0);
        verify(this.delegateResultSet).getTimestamp(0);
    }

    @DisplayName("getTimestamp")
    @Test
    void getTimestamp1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getTimestamp(0, null);
        verify(this.delegateResultSet).getTimestamp(0, null);
    }

    @DisplayName("getTimestamp")
    @Test
    void getTimestamp2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getTimestamp(null);
        verify(this.delegateResultSet).getTimestamp(null);
    }

    @DisplayName("getTimestamp")
    @Test
    void getTimestamp3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getTimestamp(null, null);
        verify(this.delegateResultSet).getTimestamp(null, null);
    }

    @DisplayName("getType")
    @Test
    void getType() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getType();
        verify(this.delegateResultSet).getType();
    }

    @DisplayName("getURL")
    @Test
    void getURL() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getURL(0);
        verify(this.delegateResultSet).getURL(0);
    }

    @DisplayName("getURL")
    @Test
    void getURL1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getURL(null);
        verify(this.delegateResultSet).getURL(null);
    }

    @DisplayName("getUnicodeStream")
    @Test
    void getUnicodeStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getUnicodeStream(0);
        verify(this.delegateResultSet).getUnicodeStream(0);
    }

    @DisplayName("getUnicodeStream")
    @Test
    void getUnicodeStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getUnicodeStream(null);
        verify(this.delegateResultSet).getUnicodeStream(null);
    }

    @DisplayName("getWarnings")
    @Test
    void getWarnings() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.getWarnings();
        verify(this.delegateResultSet).getWarnings();
    }

    @DisplayName("insertRow")
    @Test
    void insertRow() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.insertRow();
        verify(this.delegateResultSet).insertRow();
    }

    @DisplayName("isAfterLast")
    @Test
    void isAfterLast() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.isAfterLast();
        verify(this.delegateResultSet).isAfterLast();
    }

    @DisplayName("isBeforeFirst")
    @Test
    void isBeforeFirst() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.isBeforeFirst();
        verify(this.delegateResultSet).isBeforeFirst();
    }

    @DisplayName("isClosed")
    @Test
    void isClosed() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.isClosed();
        verify(this.delegateResultSet).isClosed();
    }

    @DisplayName("isFirst")
    @Test
    void isFirst() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.isFirst();
        verify(this.delegateResultSet).isFirst();
    }

    @DisplayName("isLast")
    @Test
    void isLast() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.isLast();
        verify(this.delegateResultSet).isLast();
    }

    @DisplayName("isWrapperFor")
    @Test
    void isWrapperFor() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.isWrapperFor(null);
        verify(this.delegateResultSet).isWrapperFor(null);
    }

    @DisplayName("last")
    @Test
    void last() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.last();
        verify(this.delegateResultSet).last();
    }

    @DisplayName("moveToCurrentRow")
    @Test
    void moveToCurrentRow() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.moveToCurrentRow();
        verify(this.delegateResultSet).moveToCurrentRow();
    }

    @DisplayName("moveToInsertRow")
    @Test
    void moveToInsertRow() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.moveToInsertRow();
        verify(this.delegateResultSet).moveToInsertRow();
    }

    @DisplayName("next")
    @Test
    void next() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.next();
        verify(this.delegateResultSet).next();
    }

    @DisplayName("previous")
    @Test
    void previous() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.previous();
        verify(this.delegateResultSet).previous();
    }

    @DisplayName("refreshRow")
    @Test
    void refreshRow() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.refreshRow();
        verify(this.delegateResultSet).refreshRow();
    }

    @DisplayName("relative")
    @Test
    void relative() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.relative(0);
        verify(this.delegateResultSet).relative(0);
    }

    @DisplayName("rowDeleted")
    @Test
    void rowDeleted() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.rowDeleted();
        verify(this.delegateResultSet).rowDeleted();
    }

    @DisplayName("rowInserted")
    @Test
    void rowInserted() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.rowInserted();
        verify(this.delegateResultSet).rowInserted();
    }

    @DisplayName("rowUpdated")
    @Test
    void rowUpdated() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.rowUpdated();
        verify(this.delegateResultSet).rowUpdated();
    }

    @DisplayName("setFetchDirection")
    @Test
    void setFetchDirection() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.setFetchDirection(0);
        verify(this.delegateResultSet).setFetchDirection(0);
    }

    @DisplayName("setFetchSize")
    @Test
    void setFetchSize() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.setFetchSize(0);
        verify(this.delegateResultSet).setFetchSize(0);
    }

    @DisplayName("unwrap")
    @Test
    void unwrap() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.unwrap(null);
        verify(this.delegateResultSet).unwrap(null);
    }

    @DisplayName("updateArray")
    @Test
    void updateArray() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateArray(0, null);
        verify(this.delegateResultSet).updateArray(0, null);
    }

    @DisplayName("updateArray")
    @Test
    void updateArray1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateArray(null, null);
        verify(this.delegateResultSet).updateArray(null, null);
    }

    @DisplayName("updateAsciiStream")
    @Test
    void updateAsciiStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateAsciiStream(0, null);
        verify(this.delegateResultSet).updateAsciiStream(0, null);
    }

    @DisplayName("updateAsciiStream")
    @Test
    void updateAsciiStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateAsciiStream(0, null, 0);
        verify(this.delegateResultSet).updateAsciiStream(0, null, 0);
    }

    @DisplayName("updateAsciiStream")
    @Test
    void updateAsciiStream2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateAsciiStream(0, null, 0L);
        verify(this.delegateResultSet).updateAsciiStream(0, null, 0L);
    }

    @DisplayName("updateAsciiStream")
    @Test
    void updateAsciiStream3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateAsciiStream(null, null);
        verify(this.delegateResultSet).updateAsciiStream(null, null);
    }

    @DisplayName("updateAsciiStream")
    @Test
    void updateAsciiStream4() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateAsciiStream(null, null, 0);
        verify(this.delegateResultSet).updateAsciiStream(null, null, 0);
    }

    @DisplayName("updateAsciiStream")
    @Test
    void updateAsciiStream5() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateAsciiStream(null, null, 0L);
        verify(this.delegateResultSet).updateAsciiStream(null, null, 0L);
    }

    @DisplayName("updateBigDecimal")
    @Test
    void updateBigDecimal() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBigDecimal(0, null);
        verify(this.delegateResultSet).updateBigDecimal(0, null);
    }

    @DisplayName("updateBigDecimal")
    @Test
    void updateBigDecimal1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBigDecimal(null, null);
        verify(this.delegateResultSet).updateBigDecimal(null, null);
    }

    @DisplayName("updateBinaryStream")
    @Test
    void updateBinaryStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBinaryStream(0, null);
        verify(this.delegateResultSet).updateBinaryStream(0, null);
    }

    @DisplayName("updateBinaryStream")
    @Test
    void updateBinaryStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBinaryStream(0, null, 0);
        verify(this.delegateResultSet).updateBinaryStream(0, null, 0);
    }

    @DisplayName("updateBinaryStream")
    @Test
    void updateBinaryStream2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBinaryStream(0, null, 0L);
        verify(this.delegateResultSet).updateBinaryStream(0, null, 0L);
    }

    @DisplayName("updateBinaryStream")
    @Test
    void updateBinaryStream3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBinaryStream(null, null);
        verify(this.delegateResultSet).updateBinaryStream(null, null);
    }

    @DisplayName("updateBinaryStream")
    @Test
    void updateBinaryStream4() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBinaryStream(null, null, 0);
        verify(this.delegateResultSet).updateBinaryStream(null, null, 0);
    }

    @DisplayName("updateBinaryStream")
    @Test
    void updateBinaryStream5() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBinaryStream(null, null, 0L);
        verify(this.delegateResultSet).updateBinaryStream(null, null, 0L);
    }

    @DisplayName("updateBlob")
    @Test
    void updateBlob() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBlob(0, (Blob) null);
        verify(this.delegateResultSet).updateBlob(0, (Blob) null);
    }

    @DisplayName("updateBlob")
    @Test
    void updateBlob1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBlob(0, (InputStream) null);
        verify(this.delegateResultSet).updateBlob(0, (InputStream) null);
    }

    @DisplayName("updateBlob")
    @Test
    void updateBlob2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBlob(0, null, 0L);
        verify(this.delegateResultSet).updateBlob(0, null, 0L);
    }

    @DisplayName("updateBlob")
    @Test
    void updateBlob3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBlob(null, (Blob) null);
        verify(this.delegateResultSet).updateBlob(null, (Blob) null);
    }

    @DisplayName("updateBlob")
    @Test
    void updateBlob4() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBlob(null, (InputStream) null);
        verify(this.delegateResultSet).updateBlob(null, (InputStream) null);
    }

    @DisplayName("updateBlob")
    @Test
    void updateBlob5() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBlob(null, null, 0L);
        verify(this.delegateResultSet).updateBlob(null, null, 0L);
    }

    @DisplayName("updateBoolean")
    @Test
    void updateBoolean() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBoolean(0, false);
        verify(this.delegateResultSet).updateBoolean(0, false);
    }

    @DisplayName("updateBoolean")
    @Test
    void updateBoolean1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBoolean(null, false);
        verify(this.delegateResultSet).updateBoolean(null, false);
    }

    @DisplayName("updateByte")
    @Test
    void updateByte() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateByte(0, (byte) 0);
        verify(this.delegateResultSet).updateByte(0, (byte) 0);
    }

    @DisplayName("updateByte")
    @Test
    void updateByte1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateByte(null, (byte) 0);
        verify(this.delegateResultSet).updateByte(null, (byte) 0);
    }

    @DisplayName("updateBytes")
    @Test
    void updateBytes() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBytes(0, null);
        verify(this.delegateResultSet).updateBytes(0, null);
    }

    @DisplayName("updateBytes")
    @Test
    void updateBytes1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateBytes(null, null);
        verify(this.delegateResultSet).updateBytes(null, null);
    }

    @DisplayName("updateCharacterStream")
    @Test
    void updateCharacterStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateCharacterStream(0, null);
        verify(this.delegateResultSet).updateCharacterStream(0, null);
    }

    @DisplayName("updateCharacterStream")
    @Test
    void updateCharacterStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateCharacterStream(0, null, 0);
        verify(this.delegateResultSet).updateCharacterStream(0, null, 0);
    }

    @DisplayName("updateCharacterStream")
    @Test
    void updateCharacterStream2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateCharacterStream(0, null, 0L);
        verify(this.delegateResultSet).updateCharacterStream(0, null, 0L);
    }

    @DisplayName("updateCharacterStream")
    @Test
    void updateCharacterStream3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateCharacterStream(null, null);
        verify(this.delegateResultSet).updateCharacterStream(null, null);
    }

    @DisplayName("updateCharacterStream")
    @Test
    void updateCharacterStream4() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateCharacterStream(null, null, 0);
        verify(this.delegateResultSet).updateCharacterStream(null, null, 0);
    }

    @DisplayName("updateCharacterStream")
    @Test
    void updateCharacterStream5() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateCharacterStream(null, null, 0L);
        verify(this.delegateResultSet).updateCharacterStream(null, null, 0L);
    }

    @DisplayName("updateClob")
    @Test
    void updateClob() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateClob(0, (Clob) null);
        verify(this.delegateResultSet).updateClob(0, (Clob) null);
    }

    @DisplayName("updateClob")
    @Test
    void updateClob1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateClob(0, (Reader) null);
        verify(this.delegateResultSet).updateClob(0, (Reader) null);
    }

    @DisplayName("updateClob")
    @Test
    void updateClob2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateClob(0, null, 0L);
        verify(this.delegateResultSet).updateClob(0, null, 0L);
    }

    @DisplayName("updateClob")
    @Test
    void updateClob3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateClob(null, (Reader) null);
        verify(this.delegateResultSet).updateClob(null, (Reader) null);
    }

    @DisplayName("updateClob")
    @Test
    void updateClob4() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateClob(null, (Clob) null);
        verify(this.delegateResultSet).updateClob(null, (Clob) null);
    }

    @DisplayName("updateClob")
    @Test
    void updateClob5() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateClob(null, null, 0L);
        verify(this.delegateResultSet).updateClob(null, null, 0L);
    }

    @DisplayName("updateDate")
    @Test
    void updateDate() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateDate(0, null);
        verify(this.delegateResultSet).updateDate(0, null);
    }

    @DisplayName("updateDate")
    @Test
    void updateDate1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateDate(null, null);
        verify(this.delegateResultSet).updateDate(null, null);
    }

    @DisplayName("updateDouble")
    @Test
    void updateDouble() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateDouble(0, 0.0d);
        verify(this.delegateResultSet).updateDouble(0, 0.0d);
    }

    @DisplayName("updateDouble")
    @Test
    void updateDouble1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateDouble(null, 0.0d);
        verify(this.delegateResultSet).updateDouble(null, 0.0d);
    }

    @DisplayName("updateFloat")
    @Test
    void updateFloat() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateFloat(0, 0.0f);
        verify(this.delegateResultSet).updateFloat(0, 0.0f);
    }

    @DisplayName("updateFloat")
    @Test
    void updateFloat1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateFloat(null, 0.0f);
        verify(this.delegateResultSet).updateFloat(null, 0.0f);
    }

    @DisplayName("updateInt")
    @Test
    void updateInt() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateInt(0, 0);
        verify(this.delegateResultSet).updateInt(0, 0);
    }

    @DisplayName("updateInt")
    @Test
    void updateInt1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateInt(null, 0);
        verify(this.delegateResultSet).updateInt(null, 0);
    }

    @DisplayName("updateLong")
    @Test
    void updateLong() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateLong(0, 0L);
        verify(this.delegateResultSet).updateLong(0, 0L);
    }

    @DisplayName("updateLong")
    @Test
    void updateLong1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateLong(null, 0L);
        verify(this.delegateResultSet).updateLong(null, 0L);
    }

    @DisplayName("updateNCharacterStream")
    @Test
    void updateNCharacterStream() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNCharacterStream(0, null);
        verify(this.delegateResultSet).updateNCharacterStream(0, null);
    }

    @DisplayName("updateNCharacterStream")
    @Test
    void updateNCharacterStream1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNCharacterStream(0, null, 0L);
        verify(this.delegateResultSet).updateNCharacterStream(0, null, 0L);
    }

    @DisplayName("updateNCharacterStream")
    @Test
    void updateNCharacterStream2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNCharacterStream(null, null);
        verify(this.delegateResultSet).updateNCharacterStream(null, null);
    }

    @DisplayName("updateNCharacterStream")
    @Test
    void updateNCharacterStream3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNCharacterStream(null, null, 0L);
        verify(this.delegateResultSet).updateNCharacterStream(null, null, 0L);
    }

    @DisplayName("updateNClob")
    @Test
    void updateNClob() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNClob(0, (Reader) null);
        verify(this.delegateResultSet).updateNClob(0, (Reader) null);
    }

    @DisplayName("updateNClob")
    @Test
    void updateNClob1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNClob(0, (NClob) null);
        verify(this.delegateResultSet).updateNClob(0, (NClob) null);
    }

    @DisplayName("updateNClob")
    @Test
    void updateNClob2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNClob(0, null, 0L);
        verify(this.delegateResultSet).updateNClob(0, null, 0L);
    }

    @DisplayName("updateNClob")
    @Test
    void updateNClob3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNClob(null, (Reader) null);
        verify(this.delegateResultSet).updateNClob(null, (Reader) null);
    }

    @DisplayName("updateNClob")
    @Test
    void updateNClob4() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNClob(null, (NClob) null);
        verify(this.delegateResultSet).updateNClob(null, (NClob) null);
    }

    @DisplayName("updateNClob")
    @Test
    void updateNClob5() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNClob(null, null, 0L);
        verify(this.delegateResultSet).updateNClob(null, null, 0L);
    }

    @DisplayName("updateNString")
    @Test
    void updateNString() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNString(0, null);
        verify(this.delegateResultSet).updateNString(0, null);
    }

    @DisplayName("updateNString")
    @Test
    void updateNString1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNString(null, null);
        verify(this.delegateResultSet).updateNString(null, null);
    }

    @DisplayName("updateNull")
    @Test
    void updateNull() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNull(0);
        verify(this.delegateResultSet).updateNull(0);
    }

    @DisplayName("updateNull")
    @Test
    void updateNull1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateNull(null);
        verify(this.delegateResultSet).updateNull(null);
    }

    @DisplayName("updateObject")
    @Test
    void updateObject() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateObject(0, null);
        verify(this.delegateResultSet).updateObject(0, null);
    }

    @DisplayName("updateObject")
    @Test
    void updateObject1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateObject(0, null, 0);
        verify(this.delegateResultSet).updateObject(0, null, 0);
    }

    @DisplayName("updateObject")
    @Test
    void updateObject2() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateObject(0, null, null);
        verify(this.delegateResultSet).updateObject(0, null, null);
    }

    @DisplayName("updateObject")
    @Test
    void updateObject3() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateObject(0, null, null, 0);
        verify(this.delegateResultSet).updateObject(0, null, null, 0);
    }

    @DisplayName("updateObject")
    @Test
    void updateObject4() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateObject(null, null);
        verify(this.delegateResultSet).updateObject(null, null);
    }

    @DisplayName("updateObject")
    @Test
    void updateObject5() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateObject(null, null, 0);
        verify(this.delegateResultSet).updateObject(null, null, 0);
    }

    @DisplayName("updateObject")
    @Test
    void updateObject6() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateObject(null, null, null);
        verify(this.delegateResultSet).updateObject(null, null, null);
    }

    @DisplayName("updateObject")
    @Test
    void updateObject7() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateObject(null, null, null, 0);
        verify(this.delegateResultSet).updateObject(null, null, null, 0);
    }

    @DisplayName("updateRef")
    @Test
    void updateRef() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateRef(0, null);
        verify(this.delegateResultSet).updateRef(0, null);
    }

    @DisplayName("updateRef")
    @Test
    void updateRef1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateRef(null, null);
        verify(this.delegateResultSet).updateRef(null, null);
    }

    @DisplayName("updateRow")
    @Test
    void updateRow() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateRow();
        verify(this.delegateResultSet).updateRow();
    }

    @DisplayName("updateRowId")
    @Test
    void updateRowId() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateRowId(0, null);
        verify(this.delegateResultSet).updateRowId(0, null);
    }

    @DisplayName("updateRowId")
    @Test
    void updateRowId1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateRowId(null, null);
        verify(this.delegateResultSet).updateRowId(null, null);
    }

    @DisplayName("updateSQLXML")
    @Test
    void updateSQLXML() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateSQLXML(0, null);
        verify(this.delegateResultSet).updateSQLXML(0, null);
    }

    @DisplayName("updateSQLXML")
    @Test
    void updateSQLXML1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateSQLXML(null, null);
        verify(this.delegateResultSet).updateSQLXML(null, null);
    }

    @DisplayName("updateShort")
    @Test
    void updateShort() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateShort(0, (short) 0);
        verify(this.delegateResultSet).updateShort(0, (short) 0);
    }

    @DisplayName("updateShort")
    @Test
    void updateShort1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateShort(null, (short) 0);
        verify(this.delegateResultSet).updateShort(null, (short) 0);
    }

    @DisplayName("updateString")
    @Test
    void updateString() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateString(0, null);
        verify(this.delegateResultSet).updateString(0, null);
    }

    @DisplayName("updateString")
    @Test
    void updateString1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateString(null, null);
        verify(this.delegateResultSet).updateString(null, null);
    }

    @DisplayName("updateTime")
    @Test
    void updateTime() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateTime(0, null);
        verify(this.delegateResultSet).updateTime(0, null);
    }

    @DisplayName("updateTime")
    @Test
    void updateTime1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateTime(null, null);
        verify(this.delegateResultSet).updateTime(null, null);
    }

    @DisplayName("updateTimestamp")
    @Test
    void updateTimestamp() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateTimestamp(0, null);
        verify(this.delegateResultSet).updateTimestamp(0, null);
    }

    @DisplayName("updateTimestamp")
    @Test
    void updateTimestamp1() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.updateTimestamp(null, null);
        verify(this.delegateResultSet).updateTimestamp(null, null);
    }

    @DisplayName("wasNull")
    @Test
    void wasNull() throws SQLException {
        JfrResultSet resultSet = new JfrResultSet(this.delegateResultSet);
        resultSet.wasNull();
        verify(this.delegateResultSet).wasNull();
    }


}