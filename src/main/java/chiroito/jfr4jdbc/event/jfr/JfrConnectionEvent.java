package chiroito.jfr4jdbc.event.jfr;

import chiroito.jfr4jdbc.event.ConnectEvent;
import jdk.jfr.Label;

@Label("Connection")
public class JfrConnectionEvent extends JfrJdbcEvent implements ConnectEvent {

    @Label("url")
    private String url;

    @Label("connectionClass")
    private String connectionClass;

    @Label("connectionId")
    private int connectionId;

    @Label("dataSourceId")
    private int dataSourceId;

    @Label("dataSourceClass")
    private String dataSourceClass;

    @Label("userName")
    private String userName;

    @Label("password")
    private String password;

    public void setConnectionId(int connectionId) {
        this.connectionId = connectionId;
    }

    public void setUrl(String delegateUrl) {
        this.url = delegateUrl;
    }

    public void setConnectionClass(Class<?> clazz) {
        this.connectionClass = clazz.getCanonicalName();
    }

    public void setDataSourceId(int datasourceId) {
        this.dataSourceId = datasourceId;
    }

    public void setDataSourceClass(Class<?> clazz) {
        this.dataSourceClass = clazz.getCanonicalName();
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public String getConnectionClass() {
        return connectionClass;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public int getDataSourceId() {
        return dataSourceId;
    }

    public String getDataSourceClass() {
        return dataSourceClass;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}