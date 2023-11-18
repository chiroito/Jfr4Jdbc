package dev.jfr4jdbc.internal;

import com.sun.org.apache.xpath.internal.operations.Bool;
import dev.jfr4jdbc.*;
import jdk.jfr.consumer.RecordedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JfrResourceMonitorTest {

    private static final AtomicInteger id = new AtomicInteger(0);

    @Test
    void defaultDatasourceName() throws Exception {

        MockJDBC db = new MockJDBC();
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance();

        JfrDataSource dataSource = new JfrDataSource(db.getDataSource(1));

        long count = manager.getMonitors().stream().filter(m -> m.getLabel().value.startsWith("DataSource#")).count();
        assertTrue(count > 0); // Data source may have been created before this test was run
        assertEquals(0, dataSource.getResourceMetrics().usage);
    }

    @Test
    void defaultConnectionName() throws Exception {
        MockJDBC db = new MockJDBC();
        ResourceMonitorManager manager = ResourceMonitorManager.getInstance();
        JfrConnection con = new JfrConnection(Mockito.mock(Connection.class));
        con.close();

        long count = manager.getMonitors().stream().filter(m -> m.getLabel().value.equals("userManagedConnection")).count();
        assertEquals(1, count);
    }

    @Test
    void datasourceUsageCount() throws Exception {
        int labelId = id.incrementAndGet();
        String labelName = "ds" + labelId;
        MockJDBC db = new MockJDBC();
        JfrDataSource dataSource = new JfrDataSource(db.getDataSource(1), labelName);

        ResourceMetrics beforeGetMetrics = dataSource.getResourceMetrics();
        Connection con1 = dataSource.getConnection();
        ResourceMetrics afterGetMetrics = dataSource.getResourceMetrics();
        con1.close();
        ResourceMetrics afterCloseMetrics = dataSource.getResourceMetrics();

        assertEquals(0, beforeGetMetrics.usage);
        assertEquals(1, afterGetMetrics.usage);
        assertEquals(0, afterCloseMetrics.usage);
    }

    @Test
    void connectionUsageCount() throws Exception {
        int labelId = id.incrementAndGet();
        String labelName = "con" + labelId;
        MockJDBC db = new MockJDBC();
        db.initDriver("jdbc:mock");

        JfrConnection con1 = new JfrConnection(Mockito.mock(Connection.class), labelName);
        ResourceMetrics afterGetMetrics = con1.getResourceMetrics();
        con1.close();
        ResourceMetrics afterCloseMetrics = con1.getResourceMetrics();

        assertEquals(1, afterGetMetrics.usage);
        assertEquals(0, afterCloseMetrics.usage);
    }

    @Test
    @DisplayName("Whether data sources and connections are monitored separately")
    void datasourceAndConnectionUsageCount() throws Exception {
        int labelId = id.incrementAndGet();
        String label1 = "ds" + labelId;
        String label2 = "con" + labelId;
        MockJDBC db = new MockJDBC();
        db.initDriver("jdbc:mock");


        JfrDataSource dataSource = new JfrDataSource(db.getDataSource(1), label1);
        ResourceMetrics resourceMetrics1 = dataSource.getResourceMetrics();
        JfrConnection con = new JfrConnection(Mockito.mock(Connection.class), label2);
        Connection dsCon = dataSource.getConnection();
        ResourceMetrics resourceMetrics12 = dataSource.getResourceMetrics();
        ResourceMetrics resourceMetrics22 = con.getResourceMetrics();
        con.close();
        dsCon.close();
        ResourceMetrics resourceMetrics13 = dataSource.getResourceMetrics();
        ResourceMetrics resourceMetrics23 = con.getResourceMetrics();

        assertEquals(0, resourceMetrics1.usage);
        assertEquals(1, resourceMetrics12.usage);
        assertEquals(1, resourceMetrics22.usage);
        assertEquals(0, resourceMetrics13.usage);
        assertEquals(0, resourceMetrics23.usage);
    }

    @Test
    @DisplayName("Waiting for data source connection assignment")
    void datasourceWaitCount() throws Exception {
        int labelId = id.incrementAndGet();
        String label1 = "ds" + labelId;
        MockJDBC db = new MockJDBC();
        JfrDataSource dataSource = new JfrDataSource(db.getDataSource(1), label1);
        Connection con1 = dataSource.getConnection();
        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Future<?> f1 = executorService.submit(new GetConnectionTask(dataSource));
        while (dataSource.getResourceMetrics().wait != 1) ;

        ResourceMetrics resourceMetrics1 = dataSource.getResourceMetrics();
        con1.close();
        while (!f1.isDone()) ;
        ResourceMetrics resourceMetrics12 = dataSource.getResourceMetrics();

        assertEquals(1, resourceMetrics1.wait);
        assertEquals(0, resourceMetrics12.wait);
    }

    @Test
    void eachDatasourceWaitCount() throws Exception {
        int labelId = id.incrementAndGet();
        String label1 = "ds" + labelId;
        String label2 = "ds" + labelId + "_2";
        MockJDBC db = new MockJDBC();
        JfrDataSource dataSource = new JfrDataSource(db.getDataSource(1), label1);
        JfrDataSource dataSource2 = new JfrDataSource(db.getDataSource(1), label2);
        Connection con1 = dataSource.getConnection();
        Connection con2 = dataSource2.getConnection();
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future<?> f1 = executorService.submit(new GetConnectionTask(dataSource));
        Future<?> f2 = executorService.submit(new GetConnectionTask(dataSource2));
        while (dataSource.getResourceMetrics().wait != 1) ;
        while (dataSource2.getResourceMetrics().wait != 1) ;
        ResourceMetrics resourceMetrics1 = dataSource.getResourceMetrics();
        ResourceMetrics resourceMetrics2 = dataSource2.getResourceMetrics();
        con1.close();
        con2.close();
        while (!f1.isDone()) ;
        while (!f2.isDone()) ;
        ResourceMetrics resourceMetrics12 = dataSource.getResourceMetrics();
        ResourceMetrics resourceMetrics22 = dataSource2.getResourceMetrics();

        assertEquals(1, resourceMetrics1.wait);
        assertEquals(0, resourceMetrics12.wait);
        assertEquals(1, resourceMetrics2.wait);
        assertEquals(0, resourceMetrics22.wait);
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
