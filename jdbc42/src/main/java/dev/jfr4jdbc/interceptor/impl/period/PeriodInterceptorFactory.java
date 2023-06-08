package dev.jfr4jdbc.interceptor.impl.period;

import dev.jfr4jdbc.interceptor.*;
import dev.jfr4jdbc.interceptor.impl.ResourceMonitorInterceptor;

public class PeriodInterceptorFactory implements InterceptorFactory {
    @Override
    public Interceptor<DataSourceContext> createDataSourceInterceptor() {
        return new PeriodDataSourceInterceptor();
    }

    @Override
    public Interceptor<DriverContext> createDriverInterceptor() {
        return new PeriodDriverInterceptor();
    }

    @Override
    public Interceptor<CommitContext> createCommitInterceptor() {
        return new PeriodCommitInterceptor();
    }

    @Override
    public Interceptor<RollbackContext> createRollbackInterceptor() {
        return new PeriodRollbackInterceptor();
    }

    @Override
    public Interceptor<CloseContext> createCloseInterceptor() {
        return new PeriodCloseInterceptor();
    }

    @Override
    public Interceptor<StatementContext> createStatementInterceptor() {
        return new PeriodStatementInterceptor();
    }

    @Override
    public Interceptor<CancelContext> createCancelInterceptor() {
        return new PeriodCancelInterceptor();
    }

    @Override
    public Interceptor<ResultSetContext> createResultSetInterceptor() {
        return new PeriodResultSetInterceptor();
    }

    @Override
    public Interceptor<ResourceMonitorContext> createResourceMonitorInterceptor() {
        return new ResourceMonitorInterceptor();
    }
}
