package dev.jfr4jdbc.event;

public interface JdbcEvent {
    public void begin();

    public void commit();

    public default void end() {
    }

    public default boolean shouldCommit() {
        return true;
    }
}
