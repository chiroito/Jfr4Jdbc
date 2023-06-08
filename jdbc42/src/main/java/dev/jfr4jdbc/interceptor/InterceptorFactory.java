package dev.jfr4jdbc.interceptor;

public interface InterceptorFactory {
    Interceptor<DataSourceContext> createDataSourceInterceptor();

    Interceptor<DriverContext> createDriverInterceptor();

    Interceptor<CommitContext> createCommitInterceptor();

    Interceptor<RollbackContext> createRollbackInterceptor();

    Interceptor<CloseContext> createCloseInterceptor();

    Interceptor<StatementContext> createStatementInterceptor();

    Interceptor<CancelContext> createCancelInterceptor();

    Interceptor<ResultSetContext> createResultSetInterceptor();

    Interceptor<ResourceMonitorContext> createResourceMonitorInterceptor();
}
