package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.DriverContext;
import dev.jfr4jdbc.interceptor.MockInterceptor;
import dev.jfr4jdbc.interceptor.MockInterceptorFactory;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(0)
class JfrDriverTest {

    public static final String URL = "jdbc:jfr:xxx";
    public static final String DELEGATE_URL = "jdbc:xxx";
    private static final String NOT_FOUND_URL = "jdbc:jfr:yyy";

    @Mock
    private Driver delegateDriver;

    @Mock
    private Connection delegatedCon;

    @BeforeAll
    static void initClass() throws Exception {
        try {
            Driver mockDriver = Mockito.mock(Driver.class);
            when(mockDriver.acceptsURL(DELEGATE_URL)).thenReturn(true);
            when(mockDriver.acceptsURL(URL)).thenReturn(false);
            DriverManager.registerDriver(mockDriver);
        } catch (SQLException e) {
            throw new RuntimeException("Could not register Jfr4Jdbc.", e);
        }
    }

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("getDriver")
    @Test
    void getDriver() throws Exception {
        Driver driver = DriverManager.getDriver(URL);
        assertEquals(JfrServiceLoadedDriver.class, driver.getClass());
    }

    @DisplayName("getConnection")
    @Test
    void getConnection() throws Exception {
        when(delegateDriver.connect(DELEGATE_URL, null)).thenReturn(delegatedCon);

        Driver driver = new JfrDriver(delegateDriver);
        try (Connection con = driver.connect(DELEGATE_URL, null)) {
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        verify(delegateDriver).connect(DELEGATE_URL, null);
    }

    @DisplayName("getConnection to create ConnectionEvent")
    @Test
    void getConnectionConnectEvent() throws Exception {
        when(delegateDriver.connect(DELEGATE_URL, null)).thenReturn(delegatedCon);


        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        Driver driver = new JfrDriver(delegateDriver, mockInterceptorFactory);

        try (Connection con = driver.connect(DELEGATE_URL, null)) {
            // do
            assertEquals(JfrConnection.class, con.getClass());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        // Connection Event
        MockInterceptor<DriverContext> interceptor = mockInterceptorFactory.createDriverInterceptor();
        List<DriverContext> connectionEvents = interceptor.getAllPostEvents();
        assertEquals(1, connectionEvents.size());

        DriverContext event = connectionEvents.get(0);
        assertNotNull(event.getConnection());
        assertEquals(DELEGATE_URL, event.getConnectionInfo().dataSourceLabel);
        assertEquals(1, event.getConnectionInfo().conId);
        assertEquals(0, event.getConnectionInfo().wrappedConId);
        assertEquals(DELEGATE_URL, event.url);
    }

    @DisplayName("getConnection to create ConnectionEvent throw exception as expected")
    @Test
    void getConnectionConnectEventThrowSQLException() throws Exception {
        when(delegateDriver.connect(DELEGATE_URL, null)).thenThrow(new SQLException());

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        Driver driver = new JfrDriver(delegateDriver, mockInterceptorFactory);

        try (Connection con = driver.connect(DELEGATE_URL, null)) {
            fail();
        } catch (SQLException e) {

        } catch (Exception e) {
            fail();
        }

        // Connection Event
        MockInterceptor<DriverContext> interceptor = mockInterceptorFactory.createDriverInterceptor();
        List<DriverContext> connectionEvents = interceptor.getAllPostEvents();
        assertEquals(1, connectionEvents.size());

        DriverContext event = connectionEvents.get(0);
        assertNull(event.getConnection());
        assertEquals(DELEGATE_URL, event.getConnectionInfo().dataSourceLabel);
        assertEquals(1, event.getConnectionInfo().conId);
        assertEquals(0, event.getConnectionInfo().wrappedConId);
        assertEquals(DELEGATE_URL, event.url);
    }

    @DisplayName("getConnection to create ConnectionEvent throw exception as unexpected")
    @Test
    void getConnectionConnectEventThrowRuntimeException() throws Exception {
        when(delegateDriver.connect(DELEGATE_URL, null)).thenThrow(new RuntimeException());

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        Driver driver = new JfrDriver(delegateDriver, mockInterceptorFactory);

        try (Connection con = driver.connect(DELEGATE_URL, null)) {
            fail();
        } catch (RuntimeException e) {

        } catch (Exception e) {
            fail();
        }

        // Connection Event
        MockInterceptor<DriverContext> interceptor = mockInterceptorFactory.createDriverInterceptor();
        List<DriverContext> connectionEvents = interceptor.getAllPostEvents();
        assertEquals(1, connectionEvents.size());

        DriverContext event = connectionEvents.get(0);
        assertNull(event.getConnection());
        assertEquals(DELEGATE_URL, event.getConnectionInfo().dataSourceLabel);
        assertEquals(1, event.getConnectionInfo().conId);
        assertEquals(0, event.getConnectionInfo().wrappedConId);
        assertEquals(DELEGATE_URL, event.url);
    }

    @DisplayName("notGetConnection")
    @Test
    void notGetConnection() {
        assertThrows(SQLException.class, () -> DriverManager.getDriver("jdbc:yyy"));
        assertThrows(SQLException.class, () -> DriverManager.getDriver(NOT_FOUND_URL));
    }


    @Test
    void acceptsURL() throws Exception {
        assertTrue(new JfrServiceLoadedDriver().acceptsURL(URL));
    }

    @Test
    void getPropertyInfo() throws Exception {
        Driver driver = new JfrDriver(delegateDriver);
        driver.getPropertyInfo(DELEGATE_URL, null);

        verify(this.delegateDriver).getPropertyInfo(DELEGATE_URL, null);
    }

    @Test
    void getMajorVersion() throws Exception {
        Driver driver = new JfrDriver(delegateDriver);
        driver.getMajorVersion();

        verify(this.delegateDriver).getMajorVersion();
    }

    @Test
    void getMinorVersion() throws Exception {
        Driver driver = new JfrDriver(delegateDriver);
        driver.getMinorVersion();

        verify(this.delegateDriver).getMinorVersion();
    }

    @Test
    void jdbcCompliant() throws Exception {
        Driver driver = new JfrDriver(delegateDriver);
        driver.jdbcCompliant();

        verify(this.delegateDriver).jdbcCompliant();
    }

    @Test
    void getParentLogger() throws Exception {
        Driver driver = new JfrDriver(delegateDriver);
        driver.getParentLogger();

        verify(this.delegateDriver).getParentLogger();
    }
}