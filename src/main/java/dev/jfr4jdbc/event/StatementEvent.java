package dev.jfr4jdbc.event;

public interface StatementEvent extends JdbcEvent {

    public void setConnectionId(int connectionId);

    public void setStatementId(int statementId);

    public void setStatementClass(Class<?> clazz);

    public void setSql(String sql);

    public void setClosed(boolean closed);

    public void setAutoCommit(boolean autoCommit);

    public void setPoolable(boolean poolable);
}
