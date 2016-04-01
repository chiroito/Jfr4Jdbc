package chiroito.jfr4jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;

import chiroito.jfr4jdbc.event.ConnectEvent;

public class Jfr4jdbcDataSource implements DataSource {

	private final DataSource datasource;
	private final int datasourceId;
	private final EventFactory factory;

	public Jfr4jdbcDataSource(DataSource datasource) {
		this(datasource, EventFactory.getDefaultEventFactory());
	}

	public Jfr4jdbcDataSource(DataSource datasource, EventFactory factory) {
		super();
		this.datasource = datasource;
		this.datasourceId = System.identityHashCode(datasource);
		this.factory = factory;
	}
	
	@Override
	public Connection getConnection() throws SQLException {

		ConnectEvent event = factory.createConnectEvent();

		event.begin();
		event.setDataSourceId(this.datasourceId);
		event.setDataSourceClass(this.datasource.getClass());

		Connection delegatedCon = null;
		try {
			delegatedCon = this.datasource.getConnection();
			if (delegatedCon != null) {
				
				event.setConnectionClass(delegatedCon.getClass());
				event.setConnectionId(System.identityHashCode(delegatedCon));
			}

		} catch (SQLException | RuntimeException e) {
			throw e;
		} finally {
			event.commit();
		}

		return new JfrConnection(delegatedCon);
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		ConnectEvent event = factory.createConnectEvent();

		event.begin();
		event.setUserName(username);
		event.setPassword(password);
		if (this.datasource != null) {
			event.setDataSourceId(this.datasourceId);
			event.setDataSourceClass(this.datasource.getClass());
		}

		Connection delegatedCon = null;
		try {
			delegatedCon = this.datasource.getConnection(username, password);
			if (delegatedCon != null) {
				event.setConnectionClass(delegatedCon.getClass());
				event.setConnectionId(System.identityHashCode(delegatedCon));
			}

		} catch (SQLException | RuntimeException e) {
			throw e;
		} finally {
			event.commit();
		}

		return new JfrConnection(delegatedCon);
	}

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		return this.datasource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		this.datasource.setLogWriter(out);
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		this.datasource.setLoginTimeout(seconds);
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		return this.datasource.getLoginTimeout();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return this.datasource.getParentLogger();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.datasource.unwrap(iface);
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.datasource.isWrapperFor(iface);
	}
}
