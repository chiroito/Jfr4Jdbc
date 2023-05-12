package dev.jfr4jdbc.interceptor;

import dev.jfr4jdbc.interceptor.impl.legacy.LegacyInterceptorFactory;

public interface InterceptorFactory {
    Interceptor<DataSourceContext> createDataSourceInterceptor();

    Interceptor<DriverContext> createDriverInterceptor();

    Interceptor<CommitContext> createCommitInterceptor();

    Interceptor<RollbackContext> createRollbackInterceptor();

    Interceptor<CloseContext> createCloseInterceptor();

    Interceptor<StatementContext> createStatementInterceptor();

    Interceptor<CancelContext> createCancelInterceptor();

    Interceptor<ResultSetContext> createResultSetInterceptor();

    static InterceptorFactory getDefaultInterceptorFactory() {
        return new LegacyInterceptorFactory();
    }
}
