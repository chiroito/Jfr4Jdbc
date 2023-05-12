package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.DataSourceContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.ResourceMonitor;
import dev.jfr4jdbc.internal.ResourceMonitorKind;
import dev.jfr4jdbc.internal.ResourceMonitorManager;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

abstract public class JfrDataSource42 implements DataSource {

    private static final AtomicInteger labelCounter = new AtomicInteger(0);

    private static final AtomicInteger dataSourceCounter = new AtomicInteger(1);

    private final AtomicInteger connectionCounter = new AtomicInteger(1);

    private final AtomicInteger wrappedConnectionCounter = new AtomicInteger(1);

    protected final DataSource datasource;

    @Deprecated
    private final int datasourceId;

    protected final String dataSourceLabel;

    private final InterceptorFactory interceptorFactory;

    private final ResourceMonitor connectionMonitor;

    protected JfrDataSource42(DataSource datasource) {
        this(datasource, InterceptorFactory.getDefaultInterceptorFactory());
    }

    protected JfrDataSource42(DataSource datasource, String monitorLabel) {
        this(datasource, InterceptorFactory.getDefaultInterceptorFactory(), monitorLabel);
    }

    protected JfrDataSource42(DataSource datasource, InterceptorFactory interceptorFactory) {
        this(datasource, interceptorFactory, "DataSource#" + labelCounter.incrementAndGet());
    }

    protected JfrDataSource42(DataSource datasource, InterceptorFactory interceptorFactory, String dataSourceLabel) {
        super();
        if (datasource == null) {
            throw new Jfr4JdbcRuntimeException("No delegate DataSource");
        }
        this.datasource = datasource;
        this.datasourceId = dataSourceCounter.getAndIncrement();
        this.dataSourceLabel = dataSourceLabel;
        this.interceptorFactory = interceptorFactory;

        this.connectionMonitor = new ResourceMonitor(dataSourceLabel);
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        manager.addMonitor(this.connectionMonitor);
    }

    public ResourceMonitor getResourceMonitor() {
        return this.connectionMonitor;
    }

    Map<Integer, Integer> wrappedConnectionIds = new HashMap<>();

    private int getWrappedConnectionId(Connection con) {
        int objectId = System.identityHashCode(con);
        Integer wrappedConnectionId = wrappedConnectionIds.computeIfAbsent(objectId, (k) -> wrappedConnectionCounter.getAndIncrement());

        return wrappedConnectionId;
    }

    @Override
    public Connection getConnection() throws SQLException {

        int connectionId = this.connectionCounter.getAndIncrement();
        Interceptor<DataSourceContext> interceptor = this.interceptorFactory.createDataSourceInterceptor();
        DataSourceContext context = new DataSourceContext(this.datasource, new ConnectionInfo(dataSourceLabel, connectionId, 0), this.datasourceId);

        Connection delegatedCon = null;
        try {
            this.connectionMonitor.waitAssigningResource();

            interceptor.preInvoke(context);
            delegatedCon = this.datasource.getConnection();
            context.setConnection(delegatedCon, getWrappedConnectionId(delegatedCon));

        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
            this.connectionMonitor.assignedResource();
        }

        return new JfrConnection(delegatedCon, this.interceptorFactory, this.getResourceMonitor(), new ConnectionInfo(dataSourceLabel, connectionId, context.getConnectionInfo().wrappedConId));
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {

        int connectionId = this.connectionCounter.getAndIncrement();
        Interceptor<DataSourceContext> interceptor = this.interceptorFactory.createDataSourceInterceptor();
        DataSourceContext context = new DataSourceContext(this.datasource, new ConnectionInfo(dataSourceLabel, connectionId, 0), this.datasourceId);
        context.setAuth(username, password);

        Connection delegatedCon = null;
        try {
            this.connectionMonitor.waitAssigningResource();

            interceptor.preInvoke(context);
            delegatedCon = this.datasource.getConnection(username, password);
            context.setConnection(delegatedCon, getWrappedConnectionId(delegatedCon));

        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
            this.connectionMonitor.assignedResource();
        }

        String label = this.connectionMonitor.getLabel();
        return new JfrConnection(delegatedCon, this.interceptorFactory, this.getResourceMonitor(), new ConnectionInfo(dataSourceLabel, connectionId, context.getConnectionInfo().wrappedConId));
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return this.datasource.getLogWriter();
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        this.datasource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        this.datasource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return this.datasource.getLoginTimeout();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return this.datasource.getParentLogger();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return this.datasource.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return this.datasource.isWrapperFor(iface);
    }
}
