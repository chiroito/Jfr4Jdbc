package dev.jfr4jdbc.interceptor;

public class MockInterceptorFactory implements InterceptorFactory{

    private MockInterceptor<CancelContext> cancelInterceptor = new MockInterceptor();

    private MockInterceptor<DataSourceContext> dataSourceInterceptor = new MockInterceptor();

    private MockInterceptor<DriverContext> driverInterceptor = new MockInterceptor();

    private MockInterceptor<CommitContext> commitInterceptor = new MockInterceptor();

    private MockInterceptor<RollbackContext> rollbackInterceptor = new MockInterceptor();

    private MockInterceptor<CloseContext> closeInterceptor = new MockInterceptor();

    private MockInterceptor<StatementContext> statementInterceptor = new MockInterceptor();

    private MockInterceptor<ResultSetContext> resultSetInterceptor = new MockInterceptor();

    private MockInterceptor<ResourceMonitorContext> resourceMonitorInterceptor = new MockInterceptor();

    @Override
    public MockInterceptor<DataSourceContext> createDataSourceInterceptor() {
        return dataSourceInterceptor;
    }

    @Override
    public MockInterceptor<DriverContext> createDriverInterceptor() {
        return driverInterceptor;
    }

    @Override
    public MockInterceptor<CommitContext> createCommitInterceptor() {
        return commitInterceptor;
    }

    @Override
    public MockInterceptor<RollbackContext> createRollbackInterceptor() {
        return rollbackInterceptor;
    }

    @Override
    public MockInterceptor<CloseContext> createCloseInterceptor() {
        return closeInterceptor;
    }

    @Override
    public MockInterceptor<StatementContext> createStatementInterceptor() {
        return statementInterceptor;
    }

    @Override
    public MockInterceptor<CancelContext> createCancelInterceptor() {
        return cancelInterceptor;
    }

    @Override
    public MockInterceptor<ResultSetContext> createResultSetInterceptor() {
        return resultSetInterceptor;
    }

    @Override
    public MockInterceptor<ResourceMonitorContext> createResourceMonitorInterceptor() {
        return resourceMonitorInterceptor;
    }
}
