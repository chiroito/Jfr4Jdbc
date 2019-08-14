package chiroito.jfr4jdbc;

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
import java.util.List;
import java.util.stream.Collectors;

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

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Connection")).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getInt("dataSourceId") > 0);
        assertTrue(event.getString("dataSourceClass") != null);
        assertTrue(event.getString("connectionClass") != null);
        assertTrue(event.getInt("connectionId") > 0);
        assertEquals(event.getString("url"), null);
    }

    @DisplayName("getConnection to create CloseEvent")
    @Test
    void getConnectionCloseEvent() throws Exception {
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

        // Close Event
        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Close")).collect(Collectors.toList());
        assertEquals(events.size(), 1);
        RecordedEvent event = events.get(0);
        assertTrue(event.getInt("connectionId") > 0);
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

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Connection")).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertTrue(event.getInt("dataSourceId") > 0);
        assertTrue(event.getString("dataSourceClass") != null);
        assertTrue(event.getString("connectionClass") != null);
        assertTrue(event.getInt("connectionId") > 0);
        assertTrue(event.getString("userName").equals(user));
        assertTrue(event.getString("password").equals(password));
        assertEquals(event.getString("url"), null);
    }

    @DisplayName("getConnection to create CloseEvent with User/Password")
    @Test
    void getConnectionUserPassCloseEvent() throws Exception {
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

        // Close Event
        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getLabel().equals("Close")).collect(Collectors.toList());
        assertEquals(events.size(), 1);
        RecordedEvent event = events.get(0);
        assertTrue(event.getInt("connectionId") > 0);
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

    @Test
    void createConnectionBuilder() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.createConnectionBuilder();

        verify(delegatedDs).createConnectionBuilder();
    }

    @Test
    void createShardingKeyBuilder() throws Exception {
        Jfr4JdbcDataSource dataSource = new Jfr4JdbcDataSource(delegatedDs);
        dataSource.createShardingKeyBuilder();

        verify(delegatedDs).createShardingKeyBuilder();
    }
}