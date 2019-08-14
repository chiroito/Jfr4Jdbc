package chiroito.jfr4jdbc.event;

public interface ConnectEvent extends JdbcEvent {

    public void setConnectionId(int connectionId);

    public void setUrl(String delegateUrl);

    public void setConnectionClass(Class<?> clazz);

    public void setDataSourceId(int datasourceId);

    public void setDataSourceClass(Class<?> clazz);

    public void setUserName(String userName);

    public void setPassword(String password);
}
