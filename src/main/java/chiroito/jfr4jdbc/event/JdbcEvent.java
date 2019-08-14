package chiroito.jfr4jdbc.event;

public interface JdbcEvent {
    public void begin();

    public void commit();
}
