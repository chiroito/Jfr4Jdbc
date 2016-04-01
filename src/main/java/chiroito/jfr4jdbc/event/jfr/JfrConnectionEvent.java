package chiroito.jfr4jdbc.event.jfr;

import com.oracle.jrockit.jfr.EventDefinition;
import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.ValueDefinition;

import chiroito.jfr4jdbc.event.ConnectEvent;

@SuppressWarnings({ "deprecation", "restriction" })
@EventDefinition(name = "Connection", path = "jdbc/Connection", thread = true)
public class JfrConnectionEvent extends JfrJdbcEvent implements ConnectEvent {

	public JfrConnectionEvent(EventToken paramEventToken) {
		super(paramEventToken);
	}

	@ValueDefinition(name = "URL")
	private String url;

	@ValueDefinition(name = "ConnectionClass")
	private String connectionClass;

	@ValueDefinition(name = "ConnectionId", relationKey = "ConnectionId")
	private int connectionId;

	@ValueDefinition(name = "DataSourceId", relationKey = "dataSourceId")
	private int dataSourceId;
	
	@ValueDefinition(name = "DataSourceClass")
	private String dataSourceClass;
	
	@ValueDefinition(name = "UserName")
	private String userName;
	
	@ValueDefinition(name = "Password")
	private String password;
	
	@Override
	public void setConnectionId(int connectionId) {
		this.connectionId = connectionId;
	}

	@Override
	public void setUrl(String delegateUrl) {
		this.url = delegateUrl;
	}

	@Override
	public void setConnectionClass(Class<?> clazz) {
		this.connectionClass = clazz.getCanonicalName();
	}

	@Override
	public void setDataSourceId(int datasourceId) {
		this.dataSourceId = datasourceId;
	}

	@Override
	public void setDataSourceClass(Class<?> clazz) {
		this.dataSourceClass = clazz.getCanonicalName();
	}

	@Override
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
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