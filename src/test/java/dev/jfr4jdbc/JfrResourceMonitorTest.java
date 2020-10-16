package dev.jfr4jdbc;

import dev.jfr4jdbc.event.jfr.JfrConnectionResourceEvent;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JfrResourceMonitorTest {

    private static final AtomicInteger id = new AtomicInteger(0);

    @Test
    void weatherWriting() throws Exception {
        ResourceMonitor.stopRecording();
        FlightRecording fr = FlightRecording.start();
        fr.enable(JfrConnectionResourceEvent.class);

        JfrConnection con = new JfrConnection(null);
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor.recordResourceMonitor(manager);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("ConnectionResource");
        assertTrue(events.size() > 0);
    }

    @Test
    void defaultDatasourceName() throws Exception {

        MockJDBC db = new MockJDBC();
        ResourceMonitor.stopRecording();
        FlightRecording fr = FlightRecording.start();
        fr.enable(JfrConnectionResourceEvent.class);

        DataSource dataSource = new Jfr4JdbcDataSource(db.getDataSource(1));
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor.recordResourceMonitor(manager);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("ConnectionResource", e -> e.getString("label").startsWith("Data"));
        assertTrue(events.size() > 0);
    }

    @Test
    void defaultConnectionName() throws Exception {

        ResourceMonitor.stopRecording();
        FlightRecording fr = FlightRecording.start();
        fr.enable(JfrConnectionResourceEvent.class);

        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor.recordResourceMonitor(manager);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("ConnectionResource", e -> e.getString("label").startsWith("Connection"));
        assertTrue(events.size() > 0);
    }

    @Test
    void datasourceCount() throws Exception {
        int labelId = id.incrementAndGet();

        MockJDBC db = new MockJDBC();
        ResourceMonitor.stopRecording();
        FlightRecording fr = FlightRecording.start();
        fr.enable(JfrConnectionResourceEvent.class);

        DataSource dataSource = new Jfr4JdbcDataSource(db.getDataSource(1), "ds" + labelId);
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor.recordResourceMonitor(manager);
        Connection con1 = dataSource.getConnection();
        ResourceMonitor.recordResourceMonitor(manager);
        con1.close();
        ResourceMonitor.recordResourceMonitor(manager);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("ConnectionResource", e -> e.getString("label").equals("ds" + labelId));
        assertEquals(3, events.size());
        RecordedEvent first = events.get(0);
        assertEquals(0, first.getInt("usage"));
        RecordedEvent second = events.get(1);
        assertEquals(1, second.getInt("usage"));
        RecordedEvent third = events.get(2);
        assertEquals(0, third.getInt("usage"));
    }

    @Test
    void connectionCount() throws Exception {
        int labelId = id.incrementAndGet();

        MockJDBC db = new MockJDBC();
        db.initDriver("jdbc:mock");
        ResourceMonitor.stopRecording();
        FlightRecording fr = FlightRecording.start();
        fr.enable(JfrConnectionResourceEvent.class);

        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        JfrConnection con1 = new JfrConnection(Mockito.mock(Connection.class), "con" + labelId);
        ResourceMonitor.recordResourceMonitor(manager);
        con1.close();
        ResourceMonitor.recordResourceMonitor(manager);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("ConnectionResource", e -> e.getString("label").equals("con" + labelId));
        assertEquals(2, events.size());
        RecordedEvent first = events.get(0);
        assertEquals(1, first.getInt("usage"));
        RecordedEvent second = events.get(1);
        assertEquals(0, second.getInt("usage"));
    }

    @Test
    void datasourceAndConnectionCount() throws Exception {
        int labelId = id.incrementAndGet();

        MockJDBC db = new MockJDBC();
        db.initDriver("jdbc:mock");
        ResourceMonitor.stopRecording();
        FlightRecording fr = FlightRecording.start();
        fr.enable(JfrConnectionResourceEvent.class);

        DataSource dataSource = new Jfr4JdbcDataSource(db.getDataSource(1), "ds" + labelId);
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor.recordResourceMonitor(manager);
        JfrConnection con = new JfrConnection(Mockito.mock(Connection.class), "con" + labelId);
        Connection dsCon = dataSource.getConnection();
        ResourceMonitor.recordResourceMonitor(manager);
        con.close();
        dsCon.close();
        ResourceMonitor.recordResourceMonitor(manager);
        fr.stop();

        List<RecordedEvent> conEvents = fr.getEvents("ConnectionResource", e -> e.getString("label").equals("con" + labelId));
        assertEquals(2, conEvents.size());
        RecordedEvent first = conEvents.get(0);
        assertEquals(1, first.getInt("usage"));
        RecordedEvent second = conEvents.get(1);
        assertEquals(0, second.getInt("usage"));
        List<RecordedEvent> dsEvents = fr.getEvents("ConnectionResource", e -> e.getString("label").equals("ds" + labelId));
        assertEquals(3, dsEvents.size());
        RecordedEvent dsFirst = dsEvents.get(0);
        assertEquals(0, dsFirst.getInt("usage"));
        RecordedEvent dsSecond = dsEvents.get(1);
        assertEquals(1, dsSecond.getInt("usage"));
        RecordedEvent dsThird = dsEvents.get(2);
        assertEquals(0, dsThird.getInt("usage"));
    }
}
