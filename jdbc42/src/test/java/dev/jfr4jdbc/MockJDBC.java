package dev.jfr4jdbc;

import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class MockJDBC {

    private String url;
    private Consumer<PreparedStatement> preparedStatementExtention;

    public MockJDBC() {
    }

    public DataSource getDataSource(int maxConnection) throws SQLException {
        // Creating a connection pool
        BlockingQueue<Connection> pool = new LinkedBlockingQueue<>(maxConnection);
        for (int i = 0; i < maxConnection; i++) {
            Connection con = Mockito.mock(Connection.class);
            pool.add(con);
            this.initConnection(con);
        }

        // using a pooled connection and returning the connection to the pool
        DataSource delegateDataSource = Mockito.mock(DataSource.class);
        when(delegateDataSource.getConnection()).thenAnswer(i -> pool.take());
        for (Connection con : pool) {
            Mockito.lenient().doAnswer((m) -> {
                pool.put(con);
                return null;
            }).when(con).close();
        }

        return delegateDataSource;
    }

    public void initDriver(String url) throws SQLException {
        synchronized (this) {
            // if driver is already initialized, throw a exception
            if (this.url != null) {
                throw new Jfr4JdbcTestRuntimeException("Driver in this Database is already initialized");
            }

            this.url = url;

            // Mocking driver
            Driver delegateDriver = Mockito.mock(Driver.class);
            Mockito.lenient().when(delegateDriver.acceptsURL(anyString())).thenAnswer(i -> {
                return url.equals(i.getArgument(0));
            });
            Mockito.lenient().when(delegateDriver.connect(url, null)).thenAnswer(i -> {
                Connection con = Mockito.mock(Connection.class);
                initConnection(con);
                return con;
            });

            // Registering the mocked driver into DriverManager
            DriverManager.registerDriver(delegateDriver);
        }
    }

    private void initConnection(Connection con) throws SQLException {

        Mockito.lenient().when(con.prepareStatement(anyString())).thenAnswer(i -> {
            PreparedStatement stmt = Mockito.mock(PreparedStatement.class);
            initPreparedStatement(stmt);
            when(stmt.getConnection()).thenReturn(con);
            return stmt;
        });
    }

    private void initPreparedStatement(PreparedStatement stmt) throws SQLException {

        Mockito.lenient().doAnswer((m) -> {
            ResultSet resultSet = Mockito.mock(ResultSet.class);
            initResultSet(resultSet);
            when(resultSet.getStatement()).thenReturn(stmt);
            return resultSet;
        }).when(stmt).executeQuery();

        if (preparedStatementExtention != null) {
            preparedStatementExtention.accept(stmt);
        }
    }

    private void initResultSet(ResultSet rs) throws SQLException {
    }

    public void extendPreparedStatement(Consumer<PreparedStatement> c) {
        this.preparedStatementExtention = c;
    }

    public String getUrl() {
        return url;
    }
}