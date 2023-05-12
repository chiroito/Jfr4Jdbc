package dev.jfr4jdbc.interceptor.impl.legacy;

import dev.jfr4jdbc.EventFactory;
import dev.jfr4jdbc.interceptor.*;

public class LegacyInterceptorFactory implements InterceptorFactory {

    private EventFactory eventFactory;

    public LegacyInterceptorFactory(EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    public LegacyInterceptorFactory() {
        this.eventFactory = EventFactory.getDefaultEventFactory();
    }

    @Override
    public Interceptor<DataSourceContext> createDataSourceInterceptor() {
        return new LegacyDataSourceInterceptor(this.eventFactory);
    }

    @Override
    public Interceptor<DriverContext> createDriverInterceptor() {
        return new LegacyDriverInterceptor(this.eventFactory);
    }

    @Override
    public Interceptor<CommitContext> createCommitInterceptor() {
        return new LegacyCommitInterceptor(this.eventFactory);
    }

    @Override
    public Interceptor<RollbackContext> createRollbackInterceptor() {
        return new LegacyRollbackInterceptor(this.eventFactory);
    }

    @Override
    public Interceptor<CloseContext> createCloseInterceptor() {
        return new LegacyCloseInterceptor(this.eventFactory);
    }

    @Override
    public Interceptor<StatementContext> createStatementInterceptor() {
        return new LegacyStatementInterceptor(this.eventFactory);
    }

    @Override
    public Interceptor<CancelContext> createCancelInterceptor() {
        return new LegacyCancelInterceptor(this.eventFactory);
    }

    @Override
    public Interceptor<ResultSetContext> createResultSetInterceptor() {
        return new LegacyResultSetInterceptor(this.eventFactory);
    }
}
