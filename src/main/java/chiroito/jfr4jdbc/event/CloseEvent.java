package chiroito.jfr4jdbc.event;

public interface CloseEvent extends JdbcEvent {
    public void setConnectionId(int connectionId);
}
