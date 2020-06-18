package dev.jfr4jdbc;

import dev.jfr4jdbc.event.*;
import dev.jfr4jdbc.event.jfr.*;

public abstract class EventFactory {

    private static EventFactory s_factory = new JfrEventFactory();

    public static EventFactory getDefaultEventFactory() {
        return s_factory;
    }

    public abstract StatementEvent createStatementEvent();

    public abstract CancelEvent createCancelEvent();

    public abstract CloseEvent createCloseEvent();

    public abstract CommitEvent createCommitEvent();

    public abstract ConnectEvent createConnectEvent();

    public abstract ResultSetEvent createResultSetEvent();

    public abstract RollbackEvent createRollbackEvent();
}

class JfrEventFactory extends EventFactory {

    @Override
    public StatementEvent createStatementEvent() {
        return new JfrStatementEvent();
    }

    @Override
    public CancelEvent createCancelEvent() {
        return new JfrCancelEvent();
    }

    @Override
    public CloseEvent createCloseEvent() {
        return new JfrCloseEvent();
    }

    @Override
    public CommitEvent createCommitEvent() {
        return new JfrCommitEvent();
    }

    @Override
    public ConnectEvent createConnectEvent() {
        return new JfrConnectionEvent();
    }

    @Override
    public ResultSetEvent createResultSetEvent() {
        return new JfrResultSetEvent();
    }

    @Override
    public RollbackEvent createRollbackEvent() {
        return new JfrRollbackEvent();
    }
}