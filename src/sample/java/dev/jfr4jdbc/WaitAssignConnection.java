package dev.jfr4jdbc;

import jdk.jfr.Recording;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WaitAssignConnection {

    private static final int REQUEST_NUM = 100;
    private static final int THREAD_NUM = 10;
    private static final int CONNECTION_NUM = 10;

    private static final String sql = "SELECT 1 FROM dual";


    @DisplayName("bind single parameter test")
    @Test
    void test() throws Exception {

        MockJDBC jdbc = new MockJDBC();
        jdbc.extendPreparedStatement(createPreparedStatementExtension());

        try (Recording recording = new Recording();) {

            recording.start();

            DataSource ds = new Jfr4JdbcDataSource(jdbc.getDataSource(CONNECTION_NUM));

            ExecutorService executorService = Executors.newFixedThreadPool(THREAD_NUM);
            List<Future> responses = new ArrayList<>(REQUEST_NUM);

            for (int requestNum = 0; requestNum < REQUEST_NUM; requestNum++) {
                Future response = executorService.submit(process(ds));
                responses.add(response);
            }

            for (Future response : responses) {
                response.get();
            }

            executorService.shutdown();

            Path dumpFilePath = Files.createFile(Paths.get("./wait_assign_connection.jfr")).toRealPath();
            recording.dump(dumpFilePath);
            recording.stop();

        } catch (Exception e) {
            throw new Jfr4jdbcTestException(e);
        }
    }

    private Consumer<PreparedStatement> createPreparedStatementExtension() {
        return stmt -> {
            try {
                when(stmt.executeQuery()).thenAnswer((m) -> {
                    // execute query
                    int queryDuration = this.getQueryDuration();
                    TimeUnit.MILLISECONDS.sleep(queryDuration);
                    ResultSet resultSet = Mockito.mock(ResultSet.class);
                    return resultSet;
                });
            } catch (SQLException e) {
                throw new Jfr4JdbcTestRuntimeException("Fail to extend to Prepared Statement", e);
            }
        };
    }

    private Runnable process(DataSource ds) {
        return () -> {
            try (Connection con = ds.getConnection()) {

                think();

                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.executeQuery();

            } catch (SQLException e) {
                System.err.println("err");
            }
        };
    }

    private void think() {
        try {
            int thinkDuration = 10 + (int) (Math.random() * 15);
            TimeUnit.MILLISECONDS.sleep(thinkDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private int getQueryDuration() {

        double rnd = Math.random();
        return 20 + (int) (rnd * 20);
    }
}
