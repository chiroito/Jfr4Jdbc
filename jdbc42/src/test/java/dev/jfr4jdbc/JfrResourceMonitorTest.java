package dev.jfr4jdbc;

import dev.jfr4jdbc.event.jfr.JfrConnectionResourceEvent;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
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
    void datasourceUsageCount() throws Exception {
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
    void connectionUsageCount() throws Exception {
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
    void datasourceAndConnectionUsageCount() throws Exception {
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

    @Test
    void datasourceWaitCount() throws Exception {
        int labelId = id.incrementAndGet();

        MockJDBC db = new MockJDBC();
        ResourceMonitor.stopRecording();
        FlightRecording fr = FlightRecording.start();
        fr.enable(JfrConnectionResourceEvent.class);

        DataSource dataSource = new Jfr4JdbcDataSource(db.getDataSource(1), "ds" + labelId);
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor monitor = manager.getMonitor("ds" + labelId);

        ResourceMonitor.recordResourceMonitor(manager);
        Connection con1 = dataSource.getConnection();

        Thread t1 = new Thread(new GetConnectionTask(dataSource), "datasourceWaitCount");
        t1.start();

        while (monitor.getWait() != 1) ;

        ResourceMonitor.recordResourceMonitor(manager);

        Thread t2 = new Thread(new GetConnectionTask(dataSource), "datasourceWaitCount");
        t2.start();

        while (monitor.getWait() != 2) ;

        ResourceMonitor.recordResourceMonitor(manager);
        con1.close();
        ResourceMonitor.recordResourceMonitor(manager);
        fr.stop();

        List<RecordedEvent> events = fr.getEvents("ConnectionResource", e -> e.getString("label").equals("ds" + labelId));
        assertEquals(4, events.size());
        RecordedEvent first = events.get(0);
        assertEquals(0, first.getInt("wait"));
        RecordedEvent second = events.get(1);
        assertEquals(1, second.getInt("wait"));
        RecordedEvent third = events.get(2);
        assertEquals(2, third.getInt("wait"));
        RecordedEvent fourth = events.get(3);
        assertTrue(fourth.getInt("wait") <= 2);

    }

    ;

    @Test
    void eachDatasourceWaitCount() throws Exception {
        int labelId = id.incrementAndGet();

        MockJDBC db = new MockJDBC();
        ResourceMonitor.stopRecording();
        FlightRecording fr = FlightRecording.start();
        fr.enable(JfrConnectionResourceEvent.class);

        DataSource dataSource = new Jfr4JdbcDataSource(db.getDataSource(1), "ds" + labelId);
        DataSource dataSource2 = new Jfr4JdbcDataSource(db.getDataSource(1), "ds" + labelId + "_2");
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance(ResourceMonitorKind.Connection);
        ResourceMonitor monitor = manager.getMonitor("ds" + labelId);
        ResourceMonitor monitor2 = manager.getMonitor("ds" + labelId + "_2");

        ResourceMonitor.recordResourceMonitor(manager);
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource2.getConnection();

        Thread t1 = new Thread(new GetConnectionTask(dataSource), "datasourceWaitCount");
        t1.start();
        while (monitor.getWait() != 1) ;
        ResourceMonitor.recordResourceMonitor(manager);

        Thread t2 = new Thread(new GetConnectionTask(dataSource), "datasourceWaitCount");
        t2.start();
        while (monitor.getWait() != 2) ;
        ResourceMonitor.recordResourceMonitor(manager);

        Thread t3 = new Thread(new GetConnectionTask(dataSource2), "datasourceWaitCount");
        t3.start();
        while (monitor2.getWait() != 1) ;
        ResourceMonitor.recordResourceMonitor(manager);

        con1.close();
        ResourceMonitor.recordResourceMonitor(manager);

        con2.close();
        ResourceMonitor.recordResourceMonitor(manager);

        fr.stop();

        List<RecordedEvent> events = fr.getEvents("ConnectionResource", e -> e.getString("label").equals("ds" + labelId));
        events.sort((o1, o2) -> o1.getStartTime().compareTo(o2.getStartTime()));
        assertEquals(6, events.size());
        RecordedEvent e1 = events.get(0);
        assertEquals(0, e1.getInt("wait"));
        RecordedEvent e2 = events.get(1);
        assertEquals(1, e2.getInt("wait"));
        RecordedEvent e3 = events.get(2);
        assertEquals(2, e3.getInt("wait"));
        RecordedEvent e4 = events.get(3);
        assertEquals(2, e4.getInt("wait"));
        RecordedEvent e5 = events.get(4);
        assertTrue(e5.getInt("wait") <= 2);
        RecordedEvent e6 = events.get(5);
        assertTrue(e6.getInt("wait") <= 2);

        List<RecordedEvent> events2 = fr.getEvents("ConnectionResource", e -> e.getString("label").equals("ds" + labelId + "_2"));
        assertEquals(6, events2.size());
        RecordedEvent f1 = events2.get(0);
        assertEquals(0, f1.getInt("wait"));
        RecordedEvent f2 = events2.get(1);
        assertEquals(0, f2.getInt("wait"));
        RecordedEvent f3 = events2.get(2);
        assertEquals(0, f3.getInt("wait"));
        RecordedEvent f4 = events2.get(3);
        assertEquals(1, f4.getInt("wait"));
        RecordedEvent f5 = events2.get(4);
        assertEquals(1, f5.getInt("wait"));
        RecordedEvent f6 = events2.get(5);
        assertTrue(f6.getInt("wait") <= 1);
    }

    class GetConnectionTask implements Runnable {

        private final DataSource datasource;

        public GetConnectionTask(DataSource datasource) {
            this.datasource = datasource;
        }

        @Override
        public void run() {
            try {
                Connection con = this.datasource.getConnection();
                // this code will wait here.
                con.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

}
