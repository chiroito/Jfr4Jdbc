package chiroito.jfr4jdbc.event;

public interface CommitEvent extends JdbcEvent {
    public void setConnectionId(int connectionId);
}
