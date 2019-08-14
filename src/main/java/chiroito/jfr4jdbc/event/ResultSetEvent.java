package chiroito.jfr4jdbc.event;

public interface ResultSetEvent extends JdbcEvent {

    public void setConnectionId(int connectionId);

    public void setStatementId(int statementId);

    public void setResultSetClass(Class<?> clazz);

    public void setResultSetId(int resultSetId);

    public void setRowNo(int rowNo);
}
