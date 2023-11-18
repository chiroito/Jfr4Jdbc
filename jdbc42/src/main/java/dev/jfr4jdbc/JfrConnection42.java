package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.*;
import dev.jfr4jdbc.internal.*;

import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

abstract public class JfrConnection42 implements Connection {

    private static final String LABEL_IS_NOT_SPECIFIED = "userManagedConnection";

    protected final Connection connection;

    private final ConnectionInfo connectionInfo;

    private final InterceptorFactory interceptorFactory;

    private final ResourceMonitor resourceMonitor;

    private final AtomicInteger operationCounter = new AtomicInteger(1);

    protected JfrConnection42(Connection con) {
        this(con, InterceptorManager.getDefaultInterceptorFactory(), LABEL_IS_NOT_SPECIFIED, ConnectionInfo.NO_INFO);
    }

    protected JfrConnection42(Connection con, String label) {
        this(con, InterceptorManager.getDefaultInterceptorFactory(), label, ConnectionInfo.NO_INFO);
    }

    protected JfrConnection42(Connection con, InterceptorFactory factory) {
        this(con, factory, LABEL_IS_NOT_SPECIFIED, ConnectionInfo.NO_INFO);
    }

    protected JfrConnection42(Connection con, InterceptorFactory factory, String label) {
        this(con, factory, label, ConnectionInfo.NO_INFO);
    }

    private JfrConnection42(Connection con, InterceptorFactory factory, String label, ConnectionInfo connectionInfo) {
        super();
        this.connection = con;
        this.interceptorFactory = factory;
        this.resourceMonitor = ResourceMonitorManager.getInstance().getOrCreateResourceMonitor(new Label(label));
        this.connectionInfo = connectionInfo;

        this.resourceMonitor.useResource();
    }


    public JfrConnection42(Connection con, InterceptorFactory factory, ResourceMonitor resourceMonitor, ConnectionInfo connectionInfo) {
        super();
        this.connection = con;
        this.interceptorFactory = factory;
        this.resourceMonitor = resourceMonitor;
        this.connectionInfo = connectionInfo;

        this.resourceMonitor.useResource();
    }

    public ResourceMetrics getResourceMetrics() {
        return this.resourceMonitor.getMetrics();
    }

    private JfrStatement createStatement(Statement s) {
        return new JfrStatement(s, interceptorFactory, connectionInfo, new OperationInfo(operationCounter.getAndIncrement()));
    }

    private JfrPreparedStatement createPreparedStatement(PreparedStatement p, String sql) {
        return new JfrPreparedStatement(p, sql, interceptorFactory, connectionInfo, new OperationInfo(operationCounter.getAndIncrement()));
    }

    private JfrCallableStatement createCallableStatement(CallableStatement c, String sql) {
        return new JfrCallableStatement(c, sql, interceptorFactory, connectionInfo, new OperationInfo(operationCounter.getAndIncrement()));
    }

    public ConnectionInfo getConnectionInfo() {
        return this.connectionInfo;
    }

    @Override
    public void commit() throws SQLException {

        Interceptor<CommitContext> interceptor = interceptorFactory.createCommitInterceptor();
        CommitContext context = new CommitContext(this.connection, connectionInfo, new OperationInfo(operationCounter.getAndIncrement()));
        try {
            interceptor.preInvoke(context);
            this.connection.commit();
        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
        }
    }

    @Override
    public void rollback() throws SQLException {
        Interceptor<RollbackContext> interceptor = interceptorFactory.createRollbackInterceptor();
        RollbackContext context = new RollbackContext(this.connection, connectionInfo, new OperationInfo(operationCounter.getAndIncrement()));

        try {
            interceptor.preInvoke(context);
            this.connection.rollback();
        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
        }
    }

    @Override
    public void close() throws SQLException {
        Interceptor<CloseContext> interceptor = interceptorFactory.createCloseInterceptor();
        CloseContext context = new CloseContext(this.connection, connectionInfo, new OperationInfo(operationCounter.getAndIncrement()));
        try {
            interceptor.preInvoke(context);
            this.connection.close();
        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
            this.resourceMonitor.releaseResource();
        }
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        PreparedStatement delegatePstate = this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        return this.createPreparedStatement(delegatePstate, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        PreparedStatement delegatePstate = this.connection.prepareStatement(sql, autoGeneratedKeys);
        return this.createPreparedStatement(delegatePstate, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        PreparedStatement delegatePstate = this.connection.prepareStatement(sql, columnIndexes);
        return this.createPreparedStatement(delegatePstate, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        PreparedStatement delegatePstate = this.connection.prepareStatement(sql, columnNames);
        return this.createPreparedStatement(delegatePstate, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement delegatePstate = this.connection.prepareStatement(sql);
        return this.createPreparedStatement(delegatePstate, sql);
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        PreparedStatement delegatePstate = this.connection.prepareStatement(sql, resultSetType, resultSetConcurrency);
        return this.createPreparedStatement(delegatePstate, sql);
    }

    @Override
    public Statement createStatement() throws SQLException {
        Statement delegateState = this.connection.createStatement();
        return this.createStatement(delegateState);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        Statement delegateStates = this.connection.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        return this.createStatement(delegateStates);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement delegateState = this.connection.createStatement(resultSetType, resultSetConcurrency);
        return this.createStatement(delegateState);
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        CallableStatement delegateCstate = this.connection.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        return this.createCallableStatement(delegateCstate, sql);
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        CallableStatement delegateCstate = this.connection.prepareCall(sql);
        return this.createCallableStatement(delegateCstate, sql);

    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        CallableStatement delegateCstate = this.connection.prepareCall(sql, resultSetType, resultSetConcurrency);
        return this.createCallableStatement(delegateCstate, sql);
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.connection.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.connection.isWrapperFor(iface);
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return this.connection.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.connection.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.connection.getAutoCommit();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.connection.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return this.connection.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        this.connection.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return this.connection.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        this.connection.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return this.connection.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        this.connection.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.connection.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.connection.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        this.connection.clearWarnings();
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.connection.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        this.connection.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        this.connection.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return this.connection.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return this.connection.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return this.connection.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        this.connection.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        this.connection.releaseSavepoint(savepoint);
    }

    @Override
    public Clob createClob() throws SQLException {
        return this.connection.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return this.connection.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return this.connection.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return this.connection.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return this.connection.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        this.connection.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        this.connection.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return this.connection.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return this.connection.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return this.connection.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return this.connection.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        this.connection.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return this.connection.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        this.connection.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        this.connection.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return this.connection.getNetworkTimeout();
    }
}