package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.DataSourceContext;
import dev.jfr4jdbc.interceptor.MockInterceptor;
import dev.jfr4jdbc.interceptor.MockInterceptorFactory;
import dev.jfr4jdbc.internal.ConnectionInfo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(0)
class JfrDataSourceTest {

    @Mock
    DataSource delegatedDs;

    @Mock
    Connection delegatedCon;

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("getConnection")
    @Test
    void getConnection() throws Exception {
        when(delegatedDs.getConnection()).thenReturn(delegatedCon);

        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        try (Connection con = dataSource.getConnection()) {
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        verify(delegatedDs).getConnection();
    }

    @DisplayName("getConnection to create ConnectionEvent")
    @Test
    void getConnectionConnectEvent() throws Exception {
        when(delegatedDs.getConnection()).thenReturn(delegatedCon);

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrDataSource dataSource = new JfrDataSource(delegatedDs, mockInterceptorFactory, "ds");

        try (Connection con = dataSource.getConnection()) {
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // Connection Event
        MockInterceptor<DataSourceContext> interceptor = mockInterceptorFactory.createDataSourceInterceptor();
        List<DataSourceContext> events = interceptor.getAllPostEvents();
        assertEquals(1, events.size());

        DataSourceContext event = events.get(0);
        assertNotNull(event.dataSource);
        assertTrue(event.dataSourceId > 0);
        assertNotNull(event.dataSource);
        assertNull(event.getUsername());
        assertNull(event.getPassword());
        assertNotNull(event.getConnection());
        assertEquals("ds", event.getConnectionInfo().dataSourceLabel);
        assertEquals(1, event.getConnectionInfo().conId);
        assertEquals(1, event.getConnectionInfo().wrappedConId);
    }

    @DisplayName("getConnection to create ConnectionEvent throw exception as expected")
    @Test
    void getConnectionConnectEventThrowSQLException() throws Exception {
        when(delegatedDs.getConnection()).thenThrow(new SQLException());

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrDataSource dataSource = new JfrDataSource(delegatedDs, mockInterceptorFactory, "ds");

        try (Connection con = dataSource.getConnection()) {
            fail();
        } catch (SQLException e) {

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // Connection Event
        MockInterceptor<DataSourceContext> interceptor = mockInterceptorFactory.createDataSourceInterceptor();
        List<DataSourceContext> events = interceptor.getAllPostEvents();
        assertEquals(1, events.size());

        DataSourceContext event = events.get(0);
        assertTrue(event.dataSourceId > 0);
        assertNotNull(event.dataSource);
        assertNull(event.getUsername());
        assertNull(event.getPassword());
        assertNull(event.getConnection());
        assertEquals("ds", event.getConnectionInfo().dataSourceLabel);
        assertEquals(1, event.getConnectionInfo().conId);
        assertEquals(0, event.getConnectionInfo().wrappedConId);
    }

    @DisplayName("getConnection to create ConnectionEvent throw exception as unexpected")
    @Test
    void getConnectionConnectEventThrowRuntimeException() throws Exception {
        when(delegatedDs.getConnection()).thenThrow(new RuntimeException());

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrDataSource dataSource = new JfrDataSource(delegatedDs, mockInterceptorFactory, "ds");

        try (Connection con = dataSource.getConnection()) {
            fail();
        } catch (RuntimeException e) {

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // Connection Event
        MockInterceptor<DataSourceContext> interceptor = mockInterceptorFactory.createDataSourceInterceptor();
        List<DataSourceContext> events = interceptor.getAllPostEvents();
        assertEquals(1, events.size());

        DataSourceContext event = events.get(0);
        assertTrue(event.dataSourceId > 0);
        assertNotNull(event.dataSource);
        assertNull(event.getUsername());
        assertNull(event.getPassword());
        assertNull(event.getConnection());
        assertNotNull(event.getConnectionInfo());
    }

    @DisplayName("getConnection with User/Password")
    @Test
    void getConnectionUserPass() throws Exception {
        when(delegatedDs.getConnection(any(), any())).thenReturn(delegatedCon);
        final String user = "UserX";
        final String password = "PasswordY";

        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        try (Connection con = dataSource.getConnection(user, password)) {
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        verify(delegatedDs).getConnection(user, password);
    }


    @DisplayName("getConnection to create ConnectionEvent with User/Password")
    @Test
    void getConnectionUserPassConnectEvent() throws Exception {
        when(delegatedDs.getConnection(any(), any())).thenReturn(delegatedCon);
        final String user = "UserX";
        final String password = "PasswordY";
        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrDataSource dataSource = new JfrDataSource(delegatedDs, mockInterceptorFactory);

        try (Connection con = dataSource.getConnection(user, password)) {
        } catch (Exception e) {
            fail();
        }

        MockInterceptor<DataSourceContext> interceptor = mockInterceptorFactory.createDataSourceInterceptor();
        List<DataSourceContext> events = interceptor.getAllPostEvents();
        assertEquals(1, events.size());
        DataSourceContext event = events.get(0);
        assertTrue(event.dataSourceId > 0);
        assertNotNull(event.dataSource);
        assertEquals(user, event.getUsername());
        assertEquals(password, event.getPassword());
        assertNotNull(event.getConnection());
        assertNotNull(event.getConnectionInfo());
        assertNotEquals(ConnectionInfo.NO_INFO, event.getConnectionInfo());
        assertNull(event.getException());
    }

    @DisplayName("getConnection to create ConnectionEvent with User/Password throw exception as expected")
    @Test
    void getConnectionUserPassConnectEventThrowSQLException() throws Exception {
        when(delegatedDs.getConnection(any(), any())).thenThrow(new SQLException());
        final String user = "UserX";
        final String password = "PasswordY";

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrDataSource dataSource = new JfrDataSource(delegatedDs, mockInterceptorFactory);
        Exception exception = null;
        try (Connection con = dataSource.getConnection(user, password)) {
            fail();
        } catch (SQLException e) {
            exception = e;
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        MockInterceptor<DataSourceContext> interceptor = mockInterceptorFactory.createDataSourceInterceptor();
        List<DataSourceContext> events = interceptor.getAllPostEvents();
        assertEquals(1, events.size());
        DataSourceContext event = events.get(0);
        assertTrue(event.dataSourceId > 0);
        assertNotNull(event.dataSource);
        assertEquals(user, event.getUsername());
        assertEquals(password, event.getPassword());
        assertNull(event.getConnection());
        assertNotNull(event.getConnectionInfo());
        assertNotEquals(ConnectionInfo.NO_INFO, event.getConnectionInfo());
        assertNotNull(event.getException());
        assertEquals(SQLException.class, exception.getClass());
    }

    @DisplayName("getConnection to create ConnectionEvent with User/Password throw exception as unexpected")
    @Test
    void getConnectionUserPassConnectEventThrowRuntimeException() throws Exception {
        when(delegatedDs.getConnection(any(), any())).thenThrow(new RuntimeException());
        final String user = "UserX";
        final String password = "PasswordY";

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        JfrDataSource dataSource = new JfrDataSource(delegatedDs, mockInterceptorFactory);
        Exception exception = null;
        try (Connection con = dataSource.getConnection(user, password)) {
            fail();
        } catch (RuntimeException e) {
            exception = e;
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        MockInterceptor<DataSourceContext> interceptor = mockInterceptorFactory.createDataSourceInterceptor();
        List<DataSourceContext> events = interceptor.getAllPostEvents();
        assertEquals(1, events.size());
        DataSourceContext event = events.get(0);
        assertTrue(event.dataSourceId > 0);
        assertNotNull(event.dataSource);
        assertEquals(user, event.getUsername());
        assertEquals(password, event.getPassword());
        assertNull(event.getConnection());
        assertNotNull(event.getConnectionInfo());
        assertNotEquals(ConnectionInfo.NO_INFO, event.getConnectionInfo());
        assertNotNull(event.getException());
        assertEquals(RuntimeException.class, exception.getClass());
    }

    @DisplayName("getLogWriter")
    @Test
    void getLogWriter() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.getLogWriter();

        verify(delegatedDs).getLogWriter();
    }


    @Test
    void setLogWriter() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.setLogWriter(null);

        verify(delegatedDs).setLogWriter(null);
    }

    @Test
    void setLoginTimeout() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.setLoginTimeout(0);

        verify(delegatedDs).setLoginTimeout(0);
    }

    @Test
    void getLoginTimeout() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.getLoginTimeout();

        verify(delegatedDs).getLoginTimeout();
    }

    @Test
    void getParentLogger() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.getParentLogger();

        verify(delegatedDs).getParentLogger();
    }

    @Test
    void unwrap() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.unwrap(null);

        verify(delegatedDs).unwrap(null);
    }

    @Test
    void isWrapperFor() throws Exception {
        JfrDataSource dataSource = new JfrDataSource(delegatedDs);
        dataSource.isWrapperFor(null);

        verify(delegatedDs).isWrapperFor(null);
    }
}