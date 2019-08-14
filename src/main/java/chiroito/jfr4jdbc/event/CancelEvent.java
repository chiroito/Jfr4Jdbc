package chiroito.jfr4jdbc.event;

public interface CancelEvent extends JdbcEvent {
    public void setConnectionId(int connectionId);

    public void setStatementId(int statementId);
}
