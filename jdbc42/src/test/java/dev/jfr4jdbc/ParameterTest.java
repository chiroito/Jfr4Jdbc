package dev.jfr4jdbc;

import dev.jfr4jdbc.event.jfr.JfrStatementEvent;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ParameterTest {

    @Mock
    private PreparedStatement delegatePstate;

    @BeforeEach
    void init() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("bind single parameter test")
    @Test
    void singleParameterByExecuteQuery() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        FlightRecording fr = FlightRecording.start();
        statement.setString(0, "a");
        statement.executeQuery();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals("0=a", event.getString("parameter"));
    }

    @DisplayName("bind multi parameter test")
    @Test
    void multiParameterByExecuteQuery() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        FlightRecording fr = FlightRecording.start();
        statement.setString(0, "a");
        statement.setInt(1, 100);
        statement.executeQuery();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals("0=a, 1=100", event.getString("parameter"));
    }

    @DisplayName("bind single parameter twice test")
    @Test
    void singleParameterTwiceByExecuteQuery() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        statement.setString(0, "a");
        statement.executeQuery();
        FlightRecording fr = FlightRecording.start();
        statement.setString(0, "b");
        statement.executeQuery();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals("0=b", event.getString("parameter"));
    }

    @DisplayName("clear parameter test")
    @Test
    void clearParameterByExecuteQuery() throws Exception {
        JfrPreparedStatement statement = new JfrPreparedStatement(this.delegatePstate, JfrStatementTest.SAMPLE_SQL);
        FlightRecording fr = FlightRecording.start();
        statement.setString(0, "a");
        statement.clearParameters();
        statement.setString(0, "b");
        statement.executeQuery();
        fr.stop();

        List<RecordedEvent> events = fr.getEvents().stream().filter(e -> e.getEventType().getName().equals(JfrStatementEvent.class.getName())).collect(Collectors.toList());
        assertEquals(1, events.size());
        RecordedEvent event = events.get(0);
        assertEquals("0=b", event.getString("parameter"));
    }
}