package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.interceptor.InterceptorManager;
import dev.jfr4jdbc.interceptor.StatementContext;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.OperationInfo;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class JfrPreparedStatement extends JfrStatement implements PreparedStatement {

    private PreparedStatement jdbcStatement;
    private String sql;
    private Set<Parameter> parameters = new TreeSet<>();

    public JfrPreparedStatement(PreparedStatement p, String sql) {
        this(p, sql, InterceptorManager.getDefaultInterceptorFactory());
    }

    public JfrPreparedStatement(PreparedStatement p, String sql, InterceptorFactory factory) {
        super(p, factory);
        this.sql = sql;
        this.jdbcStatement = p;
    }

    JfrPreparedStatement(PreparedStatement p, String sql, InterceptorFactory factory, ConnectionInfo connectionInfo, OperationInfo operationInfo) {
        super(p, factory, connectionInfo, operationInfo);
        this.sql = sql;
        this.jdbcStatement = p;
    }


    private String parameterToString() {
        String parameterStr = this.parameters.stream().map(p -> String.format("%d=%s", p.index, (p.value == null) ? "null" : p.value.toString())).collect(Collectors.joining(", "));
        return parameterStr;
    }

    @Override
    public ResultSet executeQuery() throws SQLException {

        Interceptor<StatementContext> interceptor = interceptorFactory.createStatementInterceptor();
        StatementContext context = this.createContext(sql, true);
        context.setInquiryParameter(this.parameterToString());

        this.parameters.clear();

        ResultSet rs = null;
        try {
            interceptor.preInvoke(context);
            rs = this.jdbcStatement.executeQuery();
        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
        }

        return super.createResultSet(rs);
    }

    @Override
    public int executeUpdate() throws SQLException {

        Interceptor<StatementContext> interceptor = interceptorFactory.createStatementInterceptor();
        StatementContext context = this.createContext(sql, true);
        context.setInquiryParameter(this.parameterToString());

        this.parameters.clear();

        int ret = 0;
        try {
            interceptor.preInvoke(context);
            ret = this.jdbcStatement.executeUpdate();
        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
        }

        return ret;
    }

    @Override
    public boolean execute() throws SQLException {

        Interceptor<StatementContext> interceptor = interceptorFactory.createStatementInterceptor();
        StatementContext context = this.createContext(sql, true);
        context.setInquiryParameter(this.parameterToString());

        this.parameters.clear();

        boolean ret = false;
        try {
            interceptor.preInvoke(context);
            ret = this.jdbcStatement.execute();
        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
        }

        return ret;
    }

    @Override
    public void addBatch() throws SQLException {
        this.jdbcStatement.addBatch();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.jdbcStatement.setAsciiStream(parameterIndex, x, length);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.jdbcStatement.setUnicodeStream(parameterIndex, x, length);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException {
        this.jdbcStatement.setBinaryStream(parameterIndex, x, length);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void clearParameters() throws SQLException {
        this.jdbcStatement.clearParameters();
        this.parameters.clear();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException {
        this.jdbcStatement.setObject(parameterIndex, x, targetSqlType);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException {
        this.jdbcStatement.setCharacterStream(parameterIndex, reader, length);
        this.parameters.add(new Parameter(parameterIndex, reader));
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException {
        this.jdbcStatement.setArray(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return this.jdbcStatement.getMetaData();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException {
        this.jdbcStatement.setDate(parameterIndex, x, cal);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException {
        this.jdbcStatement.setTime(parameterIndex, x, cal);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException {
        this.jdbcStatement.setTimestamp(parameterIndex, x, cal);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException {
        this.jdbcStatement.setNull(parameterIndex, sqlType, typeName);
        this.parameters.add(new Parameter(parameterIndex, typeName));
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException {
        this.jdbcStatement.setURL(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException {
        return this.jdbcStatement.getParameterMetaData();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException {
        this.jdbcStatement.setRowId(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException {
        this.jdbcStatement.setNString(parameterIndex, value);
        this.parameters.add(new Parameter(parameterIndex, value));
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException {
        this.jdbcStatement.setNCharacterStream(parameterIndex, value, length);
        this.parameters.add(new Parameter(parameterIndex, value));
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException {
        this.jdbcStatement.setNClob(parameterIndex, value);
        this.parameters.add(new Parameter(parameterIndex, value));
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException {
        this.jdbcStatement.setClob(parameterIndex, reader, length);
        this.parameters.add(new Parameter(parameterIndex, reader));
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException {
        this.jdbcStatement.setBlob(parameterIndex, inputStream, length);
        this.parameters.add(new Parameter(parameterIndex, inputStream));
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException {
        this.jdbcStatement.setNClob(parameterIndex, reader, length);
        this.parameters.add(new Parameter(parameterIndex, reader));
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException {
        this.jdbcStatement.setSQLXML(parameterIndex, xmlObject);
        this.parameters.add(new Parameter(parameterIndex, xmlObject));
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException {

        this.jdbcStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException {
        this.jdbcStatement.setAsciiStream(parameterIndex, x, length);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException {
        this.jdbcStatement.setBinaryStream(parameterIndex, x, length);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException {
        this.jdbcStatement.setCharacterStream(parameterIndex, reader, length);
        this.parameters.add(new Parameter(parameterIndex, reader));
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException {
        this.jdbcStatement.setAsciiStream(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException {
        this.jdbcStatement.setBinaryStream(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException {
        this.jdbcStatement.setCharacterStream(parameterIndex, reader);
        this.parameters.add(new Parameter(parameterIndex, reader));
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException {
        this.jdbcStatement.setNCharacterStream(parameterIndex, value);
        this.parameters.add(new Parameter(parameterIndex, value));
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException {
        this.jdbcStatement.setClob(parameterIndex, reader);
        this.parameters.add(new Parameter(parameterIndex, reader));
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException {
        this.jdbcStatement.setBlob(parameterIndex, inputStream);
        this.parameters.add(new Parameter(parameterIndex, inputStream));
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException {
        this.jdbcStatement.setNClob(parameterIndex, reader);
        this.parameters.add(new Parameter(parameterIndex, reader));
    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException {
        this.jdbcStatement.setNull(parameterIndex, sqlType);
        this.parameters.add(new Parameter(parameterIndex, sqlType));
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException {
        this.jdbcStatement.setBoolean(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException {
        this.jdbcStatement.setByte(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException {
        this.jdbcStatement.setShort(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException {
        this.jdbcStatement.setInt(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException {
        this.jdbcStatement.setLong(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException {
        this.jdbcStatement.setFloat(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException {
        this.jdbcStatement.setDouble(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException {
        this.jdbcStatement.setBigDecimal(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException {
        this.jdbcStatement.setString(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException {
        this.jdbcStatement.setDate(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException {
        this.jdbcStatement.setTime(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException {
        this.jdbcStatement.setTimestamp(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException {
        this.jdbcStatement.setObject(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException {
        this.jdbcStatement.setRef(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException {
        this.jdbcStatement.setBlob(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException {
        this.jdbcStatement.setClob(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException {
        this.jdbcStatement.setBytes(parameterIndex, x);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType, int scaleOrLength) throws SQLException {
        this.jdbcStatement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public void setObject(int parameterIndex, Object x, SQLType targetSqlType) throws SQLException {
        this.jdbcStatement.setObject(parameterIndex, x, targetSqlType);
        this.parameters.add(new Parameter(parameterIndex, x));
    }

    @Override
    public long executeLargeUpdate() throws SQLException {
        return this.jdbcStatement.executeLargeUpdate();
    }

    class Parameter implements Comparable<Parameter> {

        final int index;
        final Object value;

        public Parameter(int index, Object value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Parameter parameter = (Parameter) o;
            return index == parameter.index;
        }

        @Override
        public int hashCode() {
            return Objects.hash(index);
        }

        @Override
        public int compareTo(Parameter o) {
            return this.index - o.index;
        }
    }
}