package dev.jfr4jdbc;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(2)
class JfrCallableStatementTest {

    @Mock
    private CallableStatement delegateCstate;

    @BeforeAll
    static void initClass() throws Exception {
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("getArray")
    @Test
    void getArray() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getArray(0);
        verify(this.delegateCstate).getArray(0);
    }

    @DisplayName("getArray")
    @Test
    void getArray1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getArray(null);
        verify(this.delegateCstate).getArray(null);
    }

    @DisplayName("getBigDecimal")
    @Test
    void getBigDecimal() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBigDecimal(0);
        verify(this.delegateCstate).getBigDecimal(0);
    }

    @DisplayName("getBigDecimal")
    @Test
    void getBigDecimal1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBigDecimal(0, 0);
        verify(this.delegateCstate).getBigDecimal(0, 0);
    }

    @DisplayName("getBigDecimal")
    @Test
    void getBigDecimal2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBigDecimal(null);
        verify(this.delegateCstate).getBigDecimal(null);
    }

    @DisplayName("getBlob")
    @Test
    void getBlob() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBlob(0);
        verify(this.delegateCstate).getBlob(0);
    }

    @DisplayName("getBlob")
    @Test
    void getBlob1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBlob(null);
        verify(this.delegateCstate).getBlob(null);
    }

    @DisplayName("getBoolean")
    @Test
    void getBoolean() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBoolean(0);
        verify(this.delegateCstate).getBoolean(0);
    }

    @DisplayName("getBoolean")
    @Test
    void getBoolean1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBoolean(null);
        verify(this.delegateCstate).getBoolean(null);
    }

    @DisplayName("getByte")
    @Test
    void getByte() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getByte(0);
        verify(this.delegateCstate).getByte(0);
    }

    @DisplayName("getByte")
    @Test
    void getByte1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getByte(null);
        verify(this.delegateCstate).getByte(null);
    }

    @DisplayName("getBytes")
    @Test
    void getBytes() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBytes(0);
        verify(this.delegateCstate).getBytes(0);
    }

    @DisplayName("getBytes")
    @Test
    void getBytes1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getBytes(null);
        verify(this.delegateCstate).getBytes(null);
    }

    @DisplayName("getCharacterStream")
    @Test
    void getCharacterStream() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getCharacterStream(0);
        verify(this.delegateCstate).getCharacterStream(0);
    }

    @DisplayName("getCharacterStream")
    @Test
    void getCharacterStream1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getCharacterStream(null);
        verify(this.delegateCstate).getCharacterStream(null);
    }

    @DisplayName("getClob")
    @Test
    void getClob() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getClob(0);
        verify(this.delegateCstate).getClob(0);
    }

    @DisplayName("getClob")
    @Test
    void getClob1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getClob(null);
        verify(this.delegateCstate).getClob(null);
    }

    @DisplayName("getDate")
    @Test
    void getDate() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getDate(0);
        verify(this.delegateCstate).getDate(0);
    }

    @DisplayName("getDate")
    @Test
    void getDate1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getDate(0, null);
        verify(this.delegateCstate).getDate(0, null);
    }

    @DisplayName("getDate")
    @Test
    void getDate2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getDate(null);
        verify(this.delegateCstate).getDate(null);
    }

    @DisplayName("getDate")
    @Test
    void getDate3() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getDate(null, null);
        verify(this.delegateCstate).getDate(null, null);
    }

    @DisplayName("getDouble")
    @Test
    void getDouble() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getDouble(0);
        verify(this.delegateCstate).getDouble(0);
    }

    @DisplayName("getDouble")
    @Test
    void getDouble1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getDouble(null);
        verify(this.delegateCstate).getDouble(null);
    }

    @DisplayName("getFloat")
    @Test
    void getFloat() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getFloat(0);
        verify(this.delegateCstate).getFloat(0);
    }

    @DisplayName("getFloat")
    @Test
    void getFloat1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getFloat(null);
        verify(this.delegateCstate).getFloat(null);
    }

    @DisplayName("getInt")
    @Test
    void getInt() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getInt(0);
        verify(this.delegateCstate).getInt(0);
    }

    @DisplayName("getInt")
    @Test
    void getInt1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getInt(null);
        verify(this.delegateCstate).getInt(null);
    }

    @DisplayName("getLong")
    @Test
    void getLong() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getLong(0);
        verify(this.delegateCstate).getLong(0);
    }

    @DisplayName("getLong")
    @Test
    void getLong1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getLong(null);
        verify(this.delegateCstate).getLong(null);
    }

    @DisplayName("getNCharacterStream")
    @Test
    void getNCharacterStream() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getNCharacterStream(0);
        verify(this.delegateCstate).getNCharacterStream(0);
    }

    @DisplayName("getNCharacterStream")
    @Test
    void getNCharacterStream1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getNCharacterStream(null);
        verify(this.delegateCstate).getNCharacterStream(null);
    }

    @DisplayName("getNClob")
    @Test
    void getNClob() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getNClob(0);
        verify(this.delegateCstate).getNClob(0);
    }

    @DisplayName("getNClob")
    @Test
    void getNClob1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getNClob(null);
        verify(this.delegateCstate).getNClob(null);
    }

    @DisplayName("getNString")
    @Test
    void getNString() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getNString(0);
        verify(this.delegateCstate).getNString(0);
    }

    @DisplayName("getNString")
    @Test
    void getNString1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getNString(null);
        verify(this.delegateCstate).getNString(null);
    }

    @DisplayName("getObject")
    @Test
    void getObject() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getObject(0);
        verify(this.delegateCstate).getObject(0);
    }

    @DisplayName("getObject")
    @Test
    void getObject1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getObject(0, (Map<String, Class<?>>) null);
        verify(this.delegateCstate).getObject(0, (Map<String, Class<?>>) null);
    }

    @DisplayName("getObject")
    @Test
    void getObject2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getObject(0, (Class<Object>) null);
        verify(this.delegateCstate).getObject(0, (Class<Object>) null);
    }

    @DisplayName("getObject")
    @Test
    void getObject3() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getObject(null);
        verify(this.delegateCstate).getObject(null);
    }

    @DisplayName("getObject")
    @Test
    void getObject4() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getObject(null, (Map<String, Class<?>>) null);
        verify(this.delegateCstate).getObject(null, (Map<String, Class<?>>) null);
    }

    @DisplayName("getObject")
    @Test
    void getObject5() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getObject(null, (Class<Object>) null);
        verify(this.delegateCstate).getObject(null, (Class<Object>) null);
    }

    @DisplayName("getRef")
    @Test
    void getRef() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getRef(0);
        verify(this.delegateCstate).getRef(0);
    }

    @DisplayName("getRef")
    @Test
    void getRef1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getRef(null);
        verify(this.delegateCstate).getRef(null);
    }

    @DisplayName("getRowId")
    @Test
    void getRowId() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getRowId(0);
        verify(this.delegateCstate).getRowId(0);
    }

    @DisplayName("getRowId")
    @Test
    void getRowId1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getRowId(null);
        verify(this.delegateCstate).getRowId(null);
    }

    @DisplayName("getSQLXML")
    @Test
    void getSQLXML() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getSQLXML(0);
        verify(this.delegateCstate).getSQLXML(0);
    }

    @DisplayName("getSQLXML")
    @Test
    void getSQLXML1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getSQLXML(null);
        verify(this.delegateCstate).getSQLXML(null);
    }

    @DisplayName("getShort")
    @Test
    void getShort() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getShort(0);
        verify(this.delegateCstate).getShort(0);
    }

    @DisplayName("getShort")
    @Test
    void getShort1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getShort(null);
        verify(this.delegateCstate).getShort(null);
    }

    @DisplayName("getString")
    @Test
    void getString() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getString(0);
        verify(this.delegateCstate).getString(0);
    }

    @DisplayName("getString")
    @Test
    void getString1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getString(null);
        verify(this.delegateCstate).getString(null);
    }

    @DisplayName("getTime")
    @Test
    void getTime() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getTime(0);
        verify(this.delegateCstate).getTime(0);
    }

    @DisplayName("getTime")
    @Test
    void getTime1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getTime(0, null);
        verify(this.delegateCstate).getTime(0, null);
    }

    @DisplayName("getTime")
    @Test
    void getTime2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getTime(null);
        verify(this.delegateCstate).getTime(null);
    }

    @DisplayName("getTime")
    @Test
    void getTime3() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getTime(null, null);
        verify(this.delegateCstate).getTime(null, null);
    }

    @DisplayName("getTimestamp")
    @Test
    void getTimestamp() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getTimestamp(0);
        verify(this.delegateCstate).getTimestamp(0);
    }

    @DisplayName("getTimestamp")
    @Test
    void getTimestamp1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getTimestamp(0, null);
        verify(this.delegateCstate).getTimestamp(0, null);
    }

    @DisplayName("getTimestamp")
    @Test
    void getTimestamp2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getTimestamp(null);
        verify(this.delegateCstate).getTimestamp(null);
    }

    @DisplayName("getTimestamp")
    @Test
    void getTimestamp3() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getTimestamp(null, null);
        verify(this.delegateCstate).getTimestamp(null, null);
    }

    @DisplayName("getURL")
    @Test
    void getURL() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getURL(0);
        verify(this.delegateCstate).getURL(0);
    }

    @DisplayName("getURL")
    @Test
    void getURL1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.getURL(null);
        verify(this.delegateCstate).getURL(null);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(0, 0);
        verify(this.delegateCstate).registerOutParameter(0, 0);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(0, 0, 0);
        verify(this.delegateCstate).registerOutParameter(0, 0, 0);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(0, 0, null);
        verify(this.delegateCstate).registerOutParameter(0, 0, null);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter3() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(0, null);
        verify(this.delegateCstate).registerOutParameter(0, null);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter4() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(0, null, 0);
        verify(this.delegateCstate).registerOutParameter(0, null, 0);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter5() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(0, null, null);
        verify(this.delegateCstate).registerOutParameter(0, null, null);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter6() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(null, 0);
        verify(this.delegateCstate).registerOutParameter(null, 0);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter7() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(null, 0, 0);
        verify(this.delegateCstate).registerOutParameter(null, 0, 0);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter8() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(null, 0, null);
        verify(this.delegateCstate).registerOutParameter(null, 0, null);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter9() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(null, null);
        verify(this.delegateCstate).registerOutParameter(null, null);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter10() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(null, null, 0);
        verify(this.delegateCstate).registerOutParameter(null, null, 0);
    }

    @DisplayName("registerOutParameter")
    @Test
    void registerOutParameter11() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.registerOutParameter(null, null, null);
        verify(this.delegateCstate).registerOutParameter(null, null, null);
    }

    @DisplayName("setAsciiStream")
    @Test
    void setAsciiStream() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setAsciiStream(null, null);
        verify(this.delegateCstate).setAsciiStream(null, null);
    }

    @DisplayName("setAsciiStream")
    @Test
    void setAsciiStream1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setAsciiStream(null, null, 0);
        verify(this.delegateCstate).setAsciiStream(null, null, 0);
    }

    @DisplayName("setAsciiStream")
    @Test
    void setAsciiStream2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setAsciiStream(null, null, 0L);
        verify(this.delegateCstate).setAsciiStream(null, null, 0L);
    }

    @DisplayName("setBigDecimal")
    @Test
    void setBigDecimal() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBigDecimal(null, null);
        verify(this.delegateCstate).setBigDecimal(null, null);
    }

    @DisplayName("setBinaryStream")
    @Test
    void setBinaryStream() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBinaryStream(null, null);
        verify(this.delegateCstate).setBinaryStream(null, null);
    }

    @DisplayName("setBinaryStream")
    @Test
    void setBinaryStream1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBinaryStream(null, null, 0);
        verify(this.delegateCstate).setBinaryStream(null, null, 0);
    }

    @DisplayName("setBinaryStream")
    @Test
    void setBinaryStream2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBinaryStream(null, null, 0L);
        verify(this.delegateCstate).setBinaryStream(null, null, 0L);
    }

    @DisplayName("setBlob")
    @Test
    void setBlob() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBlob(null, (Blob) null);
        verify(this.delegateCstate).setBlob(null, (Blob) null);
    }

    @DisplayName("setBlob")
    @Test
    void setBlob1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBlob(null, (InputStream) null);
        verify(this.delegateCstate).setBlob(null, (InputStream) null);
    }

    @DisplayName("setBlob")
    @Test
    void setBlob2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBlob(null, null, 0L);
        verify(this.delegateCstate).setBlob(null, null, 0L);
    }

    @DisplayName("setBoolean")
    @Test
    void setBoolean() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBoolean(null, false);
        verify(this.delegateCstate).setBoolean(null, false);
    }

    @DisplayName("setByte")
    @Test
    void setByte() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setByte(null, (byte) 0);
        verify(this.delegateCstate).setByte(null, (byte) 0);
    }

    @DisplayName("setBytes")
    @Test
    void setBytes() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setBytes(null, null);
        verify(this.delegateCstate).setBytes(null, null);
    }

    @DisplayName("setCharacterStream")
    @Test
    void setCharacterStream() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setCharacterStream(null, null);
        verify(this.delegateCstate).setCharacterStream(null, null);
    }

    @DisplayName("setCharacterStream")
    @Test
    void setCharacterStream1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setCharacterStream(null, null, 0);
        verify(this.delegateCstate).setCharacterStream(null, null, 0);
    }

    @DisplayName("setCharacterStream")
    @Test
    void setCharacterStream2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setCharacterStream(null, null, 0L);
        verify(this.delegateCstate).setCharacterStream(null, null, 0L);
    }

    @DisplayName("setClob")
    @Test
    void setClob() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setClob(null, (Clob) null);
        verify(this.delegateCstate).setClob(null, (Clob) null);
    }

    @DisplayName("setClob")
    @Test
    void setClob1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setClob(null, (Reader) null);
        verify(this.delegateCstate).setClob(null, (Reader) null);
    }

    @DisplayName("setClob")
    @Test
    void setClob2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setClob(null, null, 0L);
        verify(this.delegateCstate).setClob(null, null, 0L);
    }

    @DisplayName("setDate")
    @Test
    void setDate() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setDate(null, null);
        verify(this.delegateCstate).setDate(null, null);
    }

    @DisplayName("setDate")
    @Test
    void setDate1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setDate(null, null, null);
        verify(this.delegateCstate).setDate(null, null, null);
    }

    @DisplayName("setDouble")
    @Test
    void setDouble() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setDouble(null, 0.0d);
        verify(this.delegateCstate).setDouble(null, 0.0d);
    }

    @DisplayName("setFloat")
    @Test
    void setFloat() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setFloat(null, 0.0f);
        verify(this.delegateCstate).setFloat(null, 0.0f);
    }

    @DisplayName("setInt")
    @Test
    void setInt() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setInt(null, 0);
        verify(this.delegateCstate).setInt(null, 0);
    }

    @DisplayName("setLong")
    @Test
    void setLong() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setLong(null, 0L);
        verify(this.delegateCstate).setLong(null, 0L);
    }

    @DisplayName("setNCharacterStream")
    @Test
    void setNCharacterStream() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setNCharacterStream(null, null);
        verify(this.delegateCstate).setNCharacterStream(null, null);
    }

    @DisplayName("setNCharacterStream")
    @Test
    void setNCharacterStream1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setNCharacterStream(null, null, 0L);
        verify(this.delegateCstate).setNCharacterStream(null, null, 0L);
    }

    @DisplayName("setNClob")
    @Test
    void setNClob() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setNClob(null, (NClob) null);
        verify(this.delegateCstate).setNClob(null, (NClob) null);
    }

    @DisplayName("setNClob")
    @Test
    void setNClob1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setNClob(null, (Reader) null);
        verify(this.delegateCstate).setNClob(null, (Reader) null);
    }

    @DisplayName("setNClob")
    @Test
    void setNClob2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setNClob(null, null, 0L);
        verify(this.delegateCstate).setNClob(null, null, 0L);
    }

    @DisplayName("setNString")
    @Test
    void setNString() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setNString(null, null);
        verify(this.delegateCstate).setNString(null, null);
    }

    @DisplayName("setNull")
    @Test
    void setNull() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setNull(null, 0);
        verify(this.delegateCstate).setNull(null, 0);
    }

    @DisplayName("setNull")
    @Test
    void setNull1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setNull(null, 0, null);
        verify(this.delegateCstate).setNull(null, 0, null);
    }

    @DisplayName("setObject")
    @Test
    void setObject() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setObject(null, null);
        verify(this.delegateCstate).setObject(null, null);
    }

    @DisplayName("setObject")
    @Test
    void setObject1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setObject(null, null, 0);
        verify(this.delegateCstate).setObject(null, null, 0);
    }

    @DisplayName("setObject")
    @Test
    void setObject2() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setObject(null, null, 0, 0);
        verify(this.delegateCstate).setObject(null, null, 0, 0);
    }

    @DisplayName("setObject")
    @Test
    void setObject3() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setObject(null, null, null);
        verify(this.delegateCstate).setObject(null, null, null);
    }

    @DisplayName("setObject")
    @Test
    void setObject4() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setObject(null, null, null, 0);
        verify(this.delegateCstate).setObject(null, null, null, 0);
    }

    @DisplayName("setRowId")
    @Test
    void setRowId() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setRowId(null, null);
        verify(this.delegateCstate).setRowId(null, null);
    }

    @DisplayName("setSQLXML")
    @Test
    void setSQLXML() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setSQLXML(null, null);
        verify(this.delegateCstate).setSQLXML(null, null);
    }

    @DisplayName("setShort")
    @Test
    void setShort() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setShort(null, (short) 0);
        verify(this.delegateCstate).setShort(null, (short) 0);
    }

    @DisplayName("setString")
    @Test
    void setString() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setString(null, null);
        verify(this.delegateCstate).setString(null, null);
    }

    @DisplayName("setTime")
    @Test
    void setTime() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setTime(null, null);
        verify(this.delegateCstate).setTime(null, null);
    }

    @DisplayName("setTime")
    @Test
    void setTime1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setTime(null, null, null);
        verify(this.delegateCstate).setTime(null, null, null);
    }

    @DisplayName("setTimestamp")
    @Test
    void setTimestamp() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setTimestamp(null, null);
        verify(this.delegateCstate).setTimestamp(null, null);
    }

    @DisplayName("setTimestamp")
    @Test
    void setTimestamp1() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setTimestamp(null, null, null);
        verify(this.delegateCstate).setTimestamp(null, null, null);
    }

    @DisplayName("setURL")
    @Test
    void setURL() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.setURL(null, null);
        verify(this.delegateCstate).setURL(null, null);
    }

    @DisplayName("wasNull")
    @Test
    void wasNull() throws SQLException {
        JfrCallableStatement statement = new JfrCallableStatement(this.delegateCstate, "sql");
        statement.wasNull();
        verify(this.delegateCstate).wasNull();
    }

}