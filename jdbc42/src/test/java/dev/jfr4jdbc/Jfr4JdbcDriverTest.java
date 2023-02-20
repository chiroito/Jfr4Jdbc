package dev.jfr4jdbc;

import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class Jfr4JdbcDriverTest {

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
        assertEquals(Jfr4JdbcDriver.class, driver.getClass());
    }

    @DisplayName("getConnection")
    @Test
    void getConnection() throws Exception {
        when(delegateDriver.connect(DELEGATE_URL, null)).thenReturn(delegatedCon);

        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        try (Connection con = driver.connect(URL, null)) {
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

        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = driver.connect(URL, null)) {
            // do
            assertEquals(JfrConnection.class, con.getClass());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            fr.stop();
        }

        // Connection Event
        List<RecordedEvent> connectionEvents = fr.getEvents("Connection");
        assertEquals(1, connectionEvents.size());
        RecordedEvent connectionEvent = connectionEvents.get(0);
        assertEquals(connectionEvent.getString("dataSourceClass"), null);
        assertTrue(connectionEvent.getString("connectionClass") != null);
        assertTrue(connectionEvent.getInt("connectionId") > 0);
        assertEquals(DELEGATE_URL, connectionEvent.getString("url"));
    }

    @DisplayName("getConnection to create ConnectionEvent throw exception as expected")
    @Test
    void getConnectionConnectEventThrowSQLException() throws Exception {
        when(delegateDriver.connect(DELEGATE_URL, null)).thenThrow(new SQLException());

        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = driver.connect(URL, null)) {
            fail();
        } catch (SQLException e) {

        } catch (Exception e) {
            fail();
        } finally {
            fr.stop();
        }

        // Connection Event
        List<RecordedEvent> connectionEvents = fr.getEvents("Connection");
        assertEquals(1, connectionEvents.size());
        RecordedEvent connectionEvent = connectionEvents.get(0);
        assertEquals(connectionEvent.getString("dataSourceClass"), null);
        assertEquals(connectionEvent.getString("connectionClass"), null);
        assertEquals(connectionEvent.getInt("connectionId"), 0);
        assertEquals(DELEGATE_URL, connectionEvent.getString("url"));
    }

    @DisplayName("getConnection to create ConnectionEvent throw exception as unexpected")
    @Test
    void getConnectionConnectEventThrowRuntimeException() throws Exception {
        when(delegateDriver.connect(DELEGATE_URL, null)).thenThrow(new RuntimeException());

        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = driver.connect(URL, null)) {
            fail();
        } catch (RuntimeException e) {

        } catch (Exception e) {
            fail();
        } finally {
            fr.stop();
        }

        // Connection Event
        List<RecordedEvent> connectionEvents = fr.getEvents("Connection");
        assertEquals(1, connectionEvents.size());
        RecordedEvent connectionEvent = connectionEvents.get(0);
        assertEquals(connectionEvent.getString("dataSourceClass"), null);
        assertEquals(connectionEvent.getString("connectionClass"), null);
        assertEquals(connectionEvent.getInt("connectionId"), 0);
        assertEquals(DELEGATE_URL, connectionEvent.getString("url"));
    }

    @DisplayName("notGetConnection")
    @Test
    void notGetConnection() {
        assertThrows(SQLException.class, () -> DriverManager.getDriver("jdbc:yyy"));
        assertThrows(SQLException.class, () -> DriverManager.getDriver(NOT_FOUND_URL));
    }


    @Test
    void acceptsURL() throws Exception {
        assertTrue(new Jfr4JdbcDriver().acceptsURL(URL));
    }

    @Test
    void getPropertyInfo() throws Exception {
        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        driver.getPropertyInfo(URL, null);

        verify(this.delegateDriver).getPropertyInfo(DELEGATE_URL, null);
    }

    @Test
    void getMajorVersion() throws Exception {
        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        driver.getMajorVersion();

        verify(this.delegateDriver).getMajorVersion();
    }

    @Test
    void getMinorVersion() throws Exception {
        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        driver.getMinorVersion();

        verify(this.delegateDriver).getMinorVersion();
    }

    @Test
    void jdbcCompliant() throws Exception {
        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        assertTrue(driver.jdbcCompliant());
    }

    @Test
    void getParentLogger() throws Exception {
        Driver driver = new Jfr4JdbcDriver(delegateDriver);
        driver.getParentLogger();

        verify(this.delegateDriver).getParentLogger();
    }
}