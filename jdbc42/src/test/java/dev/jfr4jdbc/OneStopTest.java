package dev.jfr4jdbc;

import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
@Order(10)
public class OneStopTest {

    @BeforeAll
    static void registerJfrDriver(){
        try {
            DriverManager.registerDriver(new Jfr4JdbcDriver());
        }catch (SQLException e){
            throw new Jfr4JdbcTestRuntimeException(e);
        }
    }

    @DisplayName("One stop test from data source")
    @Test
    void fromDataSource() throws Exception {

        MockJDBC db = new MockJDBC();

        FlightRecording fr = FlightRecording.start();

        DataSource ds = new Jfr4JdbcDataSource(db.getDataSource(1));
        try (Connection con = ds.getConnection()) {
            OneStopTest(con);
        }

        fr.stop();

        OneStopVerify(fr.getEvents());
    }

    @DisplayName("One stop test from driver")
    @Test
    void fromDriver() throws Exception {

        MockJDBC db = new MockJDBC();
        db.initDriver("jdbc:mock");

        FlightRecording fr = FlightRecording.start();

        Driver driver = DriverManager.getDriver("jdbc:jfr:mock");
        try (Connection con = driver.connect("jdbc:jfr:mock", null)) {
            OneStopTest(con);
        }

        fr.stop();

        OneStopVerify(fr.getEvents());
    }

    @DisplayName("One stop test from driver manager")
    @Test
    void fromDriverManager() throws Exception {

        MockJDBC db = new MockJDBC();
        db.initDriver("jdbc:mock");

        FlightRecording fr = FlightRecording.start();

        try (Connection con = DriverManager.getConnection("jdbc:jfr:mock", null)) {
            OneStopTest(con);
        }

        fr.stop();

        OneStopVerify(fr.getEvents());
    }

    void OneStopTest(Connection con) throws Exception {
        try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM dual"); ResultSet resultSet = stmt.executeQuery();) {
            resultSet.next();
            con.commit();
            con.rollback();
        }
    }

    void OneStopVerify(List<RecordedEvent> evnts) throws Exception {
        List<RecordedEvent> connectionEvents = evnts.stream().filter(e -> e.getEventType().getLabel().equals("Connection")).collect(Collectors.toList());
        assertEquals(1, connectionEvents.size());
        List<RecordedEvent> statementEvents = evnts.stream().filter(e -> e.getEventType().getLabel().equals("Statement")).collect(Collectors.toList());
        assertEquals(1, statementEvents.size());
        List<RecordedEvent> closeEvents = evnts.stream().filter(e -> e.getEventType().getLabel().equals("Close")).collect(Collectors.toList());
        assertEquals(1, closeEvents.size());
        List<RecordedEvent> commitEvents = evnts.stream().filter(e -> e.getEventType().getLabel().equals("Commit")).collect(Collectors.toList());
        assertEquals(1, commitEvents.size());
        List<RecordedEvent> rollbackEvents = evnts.stream().filter(e -> e.getEventType().getLabel().equals("Rollback")).collect(Collectors.toList());
        assertEquals(1, rollbackEvents.size());
        List<RecordedEvent> resultSetEvents = evnts.stream().filter(e -> e.getEventType().getLabel().equals("ResultSet")).collect(Collectors.toList());
        assertEquals(1, resultSetEvents.size());
    }
}
