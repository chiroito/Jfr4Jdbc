package dev.jfr4jdbc.event;

public interface RollbackEvent extends JdbcEvent {

    public void setConnectionId(int connectionId);
}
