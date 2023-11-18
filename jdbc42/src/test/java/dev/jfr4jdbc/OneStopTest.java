package dev.jfr4jdbc;

import dev.jfr4jdbc.interceptor.*;
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

    // For registering JfrServiceLoadedDriver to DriverManager;
//    private static JfrServiceLoadedDriver driver = new JfrServiceLoadedDriver();

//    @BeforeAll
//    static void registerJfrDriver() {
//        try {
//            DriverManager.registerDriver(driver);
//        } catch (SQLException e) {
//            throw new Jfr4JdbcTestRuntimeException(e);
//        }
//    }

    @DisplayName("One stop test from data source")
    @Test
    void fromDataSource() throws Exception {

        MockJDBC db = new MockJDBC();

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        DataSource ds = new JfrDataSource(db.getDataSource(1), mockInterceptorFactory);
        try (Connection con = ds.getConnection()) {
            OneStopTest(con);
        }

        List<DataSourceContext> dsEvents = mockInterceptorFactory.createDataSourceInterceptor().getAllPostEvents();
        assertEquals(1, dsEvents.size());

        OneStopVerify(mockInterceptorFactory);
    }

    @DisplayName("One stop test from driver")
    @Test
    void fromDriver() throws Exception {

        MockJDBC db = new MockJDBC();
        db.initDriver("jdbc:mock");

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        InterceptorManager.setDefaultInterceptorFactory(mockInterceptorFactory);

        Driver driver = DriverManager.getDriver("jdbc:jfr:mock");
        try (Connection con = driver.connect("jdbc:jfr:mock", null)) {
            OneStopTest(con);
        }

        List<DriverContext> dEvents = mockInterceptorFactory.createDriverInterceptor().getAllPostEvents();
        assertEquals(1, dEvents.size());

        OneStopVerify(mockInterceptorFactory);
    }

    @DisplayName("One stop test from driver manager")
    @Test
    void fromDriverManager() throws Exception {

        MockJDBC db = new MockJDBC();
        db.initDriver("jdbc:mock");

        MockInterceptorFactory mockInterceptorFactory = new MockInterceptorFactory();
        InterceptorManager.setDefaultInterceptorFactory(mockInterceptorFactory);

        try (Connection con = DriverManager.getConnection("jdbc:jfr:mock", null)) {
            OneStopTest(con);
        }

        List<DriverContext> dEvents = mockInterceptorFactory.createDriverInterceptor().getAllPostEvents();
        assertEquals(1, dEvents.size());

        OneStopVerify(mockInterceptorFactory);
    }

    void OneStopTest(Connection con) throws Exception {
        try (PreparedStatement stmt = con.prepareStatement("SELECT 1 FROM dual"); ResultSet resultSet = stmt.executeQuery();) {
            resultSet.next();
            con.commit();
            con.rollback();
        }
    }

    void OneStopVerify(MockInterceptorFactory mockInterceptorFactory) throws Exception {

        List<StatementContext> statementEvents = mockInterceptorFactory.createStatementInterceptor().getAllPostEvents();
        assertEquals(1, statementEvents.size());
        List<CloseContext> closeEvents = mockInterceptorFactory.createCloseInterceptor().getAllPostEvents();
        assertEquals(1, closeEvents.size());
        List<CommitContext> commitEvents = mockInterceptorFactory.createCommitInterceptor().getAllPostEvents();
        assertEquals(1, commitEvents.size());
        List<RollbackContext> rollbackEvents = mockInterceptorFactory.createRollbackInterceptor().getAllPostEvents();
        assertEquals(1, rollbackEvents.size());
        List<ResultSetContext> resultSetEvents = mockInterceptorFactory.createResultSetInterceptor().getAllPostEvents();
        assertEquals(1, resultSetEvents.size());
    }
}
