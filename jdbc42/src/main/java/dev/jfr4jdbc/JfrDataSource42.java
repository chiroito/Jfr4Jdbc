package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.DataSourceContext;
import dev.jfr4jdbc.interceptor.Interceptor;
import dev.jfr4jdbc.interceptor.InterceptorFactory;
import dev.jfr4jdbc.interceptor.InterceptorManager;
import dev.jfr4jdbc.internal.ConnectionInfo;
import dev.jfr4jdbc.internal.Label;
import dev.jfr4jdbc.internal.ResourceMonitor;
import dev.jfr4jdbc.internal.ResourceMonitorManager;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

abstract public class JfrDataSource42 implements DataSource {

    private static final AtomicInteger defaultLabelCounter = new AtomicInteger(0);

    private static final AtomicInteger dataSourceIdCounter = new AtomicInteger(1);

    private final AtomicInteger connectionCounter = new AtomicInteger(1);

    private final AtomicInteger wrappedConnectionCounter = new AtomicInteger(1);

    protected final DataSource datasource;

    @Deprecated
    private final int datasourceId;

    protected final String label;

    private final InterceptorFactory interceptorFactory;

    private final ResourceMonitor resourceMonitor;

    protected JfrDataSource42(DataSource datasource) {
        this(datasource, InterceptorManager.getDefaultInterceptorFactory());
    }

    protected JfrDataSource42(DataSource datasource, String label) {
        this(datasource, InterceptorManager.getDefaultInterceptorFactory(), label);
    }

    protected JfrDataSource42(DataSource datasource, InterceptorFactory interceptorFactory) {
        this(datasource, interceptorFactory, "DataSource#" + defaultLabelCounter.incrementAndGet());
    }

    protected JfrDataSource42(DataSource datasource, InterceptorFactory interceptorFactory, String label) {
        super();
        if (datasource == null) {
            throw new Jfr4JdbcRuntimeException("No delegate DataSource");
        }
        this.datasource = datasource;
        this.datasourceId = dataSourceIdCounter.getAndIncrement();
        this.label = label;
        this.interceptorFactory = interceptorFactory;

        this.resourceMonitor = ResourceMonitorManager.getInstance().getOrCreateResourceMonitor(new Label(label));
    }

    public ResourceMetrics getResourceMetrics() {
        return this.resourceMonitor.getMetrics();
    }

    Map<Integer, Integer> wrappedConnectionIds = new HashMap<>();

    private int getWrappedConnectionId(Connection con) {

        int objectId = 0;

        if (Proxy.isProxyClass(con.getClass())) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(con);
            objectId = System.identityHashCode(invocationHandler);
        } else {
            objectId = System.identityHashCode(con);
        }
        Integer wrappedConnectionId = wrappedConnectionIds.computeIfAbsent(objectId, (k) -> wrappedConnectionCounter.getAndIncrement());

        return wrappedConnectionId;
    }

    @Override
    public Connection getConnection() throws SQLException {

        int connectionId = this.connectionCounter.getAndIncrement();
        Interceptor<DataSourceContext> interceptor = this.interceptorFactory.createDataSourceInterceptor();
        DataSourceContext context = new DataSourceContext(this.datasource, new ConnectionInfo(label, connectionId, 0), this.datasourceId);

        Connection delegatedCon = null;
        try {
            this.resourceMonitor.waitAssigningResource();

            interceptor.preInvoke(context);
            delegatedCon = this.datasource.getConnection();
            context.setConnection(delegatedCon, getWrappedConnectionId(delegatedCon));
        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
            this.resourceMonitor.assignedResource();
        }

        return new JfrConnection(delegatedCon, this.interceptorFactory, this.resourceMonitor, context.getConnectionInfo());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {

        int connectionId = this.connectionCounter.getAndIncrement();
        Interceptor<DataSourceContext> interceptor = this.interceptorFactory.createDataSourceInterceptor();
        DataSourceContext context = new DataSourceContext(this.datasource, new ConnectionInfo(label, connectionId, 0), this.datasourceId);
        context.setAuth(username, password);

        Connection delegatedCon = null;
        try {
            this.resourceMonitor.waitAssigningResource();

            interceptor.preInvoke(context);
            delegatedCon = this.datasource.getConnection(username, password);
            context.setConnection(delegatedCon, getWrappedConnectionId(delegatedCon));

        } catch (SQLException | RuntimeException e) {
            context.setException(e);
            throw e;
        } finally {
            interceptor.postInvoke(context);
            this.resourceMonitor.assignedResource();
        }

        Label label = this.resourceMonitor.getLabel();
        return new JfrConnection(delegatedCon, this.interceptorFactory, this.resourceMonitor, new ConnectionInfo(this.label, connectionId, context.getConnectionInfo().wrappedConId));
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
