package dev.jfr4jdbc.interceptor.impl.instant;

import dev.jfr4jdbc.interceptor.*;
import dev.jfr4jdbc.interceptor.impl.ResourceMonitorInterceptor;

public class InstantInterceptorFactory implements InterceptorFactory {
    @Override
    public Interceptor<DataSourceContext> createDataSourceInterceptor() {
        return new InstantDataSourceInterceptor();
    }

    @Override
    public Interceptor<DriverContext> createDriverInterceptor() {
        return new InstantDriverInterceptor();
    }

    @Override
    public Interceptor<CommitContext> createCommitInterceptor() {
        return new InstantCommitInterceptor();
    }

    @Override
    public Interceptor<RollbackContext> createRollbackInterceptor() {
        return new InstantRollbackInterceptor();
    }

    @Override
    public Interceptor<CloseContext> createCloseInterceptor() {
        return new InstantCloseInterceptor();
    }

    @Override
    public Interceptor<StatementContext> createStatementInterceptor() {
        return new InstantStatementInterceptor();
    }

    @Override
    public Interceptor<CancelContext> createCancelInterceptor() {
        return new InstantCancelInterceptor();
    }

    @Override
    public Interceptor<ResultSetContext> createResultSetInterceptor() {
        return new InstantResultSetInterceptor();
    }

    @Override
    public Interceptor<ResourceMonitorContext> createResourceMonitorInterceptor() {
        return new ResourceMonitorInterceptor();
    }
}
