package dev.jfr4jdbc;

import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class Jfr4JdbcDataSourceTest {

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

        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
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

        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = dataSource.getConnection()) {
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
        assertTrue(connectionEvent.getInt("dataSourceId") > 0);
        assertTrue(connectionEvent.getString("dataSourceClass") != null);
        assertTrue(connectionEvent.getString("connectionClass") != null);
        assertTrue(connectionEvent.getInt("connectionId") > 0);
        assertEquals(connectionEvent.getString("url"), null);
    }

    @DisplayName("getConnection to create ConnectionEvent throw exception as expected")
    @Test
    void getConnectionConnectEventThrowSQLException() throws Exception {
        when(delegatedDs.getConnection()).thenThrow(new SQLException());

        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = dataSource.getConnection()) {
            fail();
        } catch (SQLException e) {

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
        assertTrue(connectionEvent.getInt("dataSourceId") > 0);
        assertTrue(connectionEvent.getString("dataSourceClass") != null);
        assertEquals(connectionEvent.getString("connectionClass"), null);
        assertEquals(connectionEvent.getInt("connectionId"), 0);
        assertEquals(connectionEvent.getString("url"), null);
    }

    @DisplayName("getConnection to create ConnectionEvent throw exception as unexpected")
    @Test
    void getConnectionConnectEventThrowRuntimeException() throws Exception {
        when(delegatedDs.getConnection()).thenThrow(new RuntimeException());

        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = dataSource.getConnection()) {
            fail();
        } catch (RuntimeException e) {

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
        assertTrue(connectionEvent.getInt("dataSourceId") > 0);
        assertTrue(connectionEvent.getString("dataSourceClass") != null);
        assertEquals(connectionEvent.getString("connectionClass"), null);
        assertEquals(connectionEvent.getInt("connectionId"), 0);
        assertEquals(connectionEvent.getString("url"), null);
    }

    @DisplayName("getConnection with User/Password")
    @Test
    void getConnectionUserPass() throws Exception {
        when(delegatedDs.getConnection(any(), any())).thenReturn(delegatedCon);
        final String user = "UserX";
        final String password = "PasswordY";

        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
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

        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = dataSource.getConnection(user, password)) {
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
        assertTrue(connectionEvent.getInt("dataSourceId") > 0);
        assertTrue(connectionEvent.getString("dataSourceClass") != null);
        assertTrue(connectionEvent.getString("connectionClass") != null);
        assertTrue(connectionEvent.getInt("connectionId") > 0);
        assertTrue(connectionEvent.getString("userName").equals(user));
        assertTrue(connectionEvent.getString("password").equals(password));
        assertEquals(connectionEvent.getString("url"), null);
    }

    @DisplayName("getConnection to create ConnectionEvent with User/Password throw exception as expected")
    @Test
    void getConnectionUserPassConnectEventThrowSQLException() throws Exception {
        when(delegatedDs.getConnection(any(), any())).thenThrow(new SQLException());
        final String user = "UserX";
        final String password = "PasswordY";

        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = dataSource.getConnection(user, password)) {
            fail();
        } catch (SQLException e) {

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
        assertTrue(connectionEvent.getInt("dataSourceId") > 0);
        assertTrue(connectionEvent.getString("dataSourceClass") != null);
        assertEquals(connectionEvent.getString("connectionClass"), null);
        assertEquals(connectionEvent.getInt("connectionId"), 0);
        assertTrue(connectionEvent.getString("userName").equals(user));
        assertTrue(connectionEvent.getString("password").equals(password));
        assertEquals(connectionEvent.getString("url"), null);
    }

    @DisplayName("getConnection to create ConnectionEvent with User/Password throw exception as unexpected")
    @Test
    void getConnectionUserPassConnectEventThrowRuntimeException() throws Exception {
        when(delegatedDs.getConnection(any(), any())).thenThrow(new RuntimeException());
        final String user = "UserX";
        final String password = "PasswordY";

        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        FlightRecording fr = FlightRecording.start();
        try (Connection con = dataSource.getConnection(user, password)) {
            fail();
        } catch (RuntimeException e) {

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
        assertTrue(connectionEvent.getInt("dataSourceId") > 0);
        assertTrue(connectionEvent.getString("dataSourceClass") != null);
        assertEquals(connectionEvent.getString("connectionClass"), null);
        assertEquals(connectionEvent.getInt("connectionId"), 0);
        assertTrue(connectionEvent.getString("userName").equals(user));
        assertTrue(connectionEvent.getString("password").equals(password));
        assertEquals(connectionEvent.getString("url"), null);
    }

    @DisplayName("getLogWriter")
    @Test
    void getLogWriter() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.getLogWriter();

        verify(delegatedDs).getLogWriter();
    }


    @Test
    void setLogWriter() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.setLogWriter(null);

        verify(delegatedDs).setLogWriter(null);
    }

    @Test
    void setLoginTimeout() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.setLoginTimeout(0);

        verify(delegatedDs).setLoginTimeout(0);
    }

    @Test
    void getLoginTimeout() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.getLoginTimeout();

        verify(delegatedDs).getLoginTimeout();
    }

    @Test
    void getParentLogger() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.getParentLogger();

        verify(delegatedDs).getParentLogger();
    }

    @Test
    void unwrap() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.unwrap(null);

        verify(delegatedDs).unwrap(null);
    }

    @Test
    void isWrapperFor() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.isWrapperFor(null);

        verify(delegatedDs).isWrapperFor(null);
    }
}