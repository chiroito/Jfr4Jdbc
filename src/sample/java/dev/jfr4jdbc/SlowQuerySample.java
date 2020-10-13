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
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SlowQuerySample {

    private static final int REQUEST_NUM = 100;
    private static final int THREAD_NUM = 10;
    private static final int CONNECTION_NUM = 10;
    private static final String sql = "SELECT * FROM access_t WHERE pref = ?";
    private static final String tokyoSql = "SELECT * FROM access_t WHERE pref = \"Tokyo\"";
    private static final boolean isFixed = true;
    private static final String[] prefs = new String[]{"Tokyo", "Tottori", "Osaka", "Kyoto", "Okinawa", "Hokkaido", "Fukuoka", "Hiroshima", "Aichi", "Okayama"};
    ThreadLocal<Duration> tl = new ThreadLocal<>();

    @DisplayName("bind single parameter test")
    @Test
    void singleParameterByExecuteQuery() throws Exception {

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

            Path dumpFilePath = Files.createFile(Paths.get("./slow_query.jfr")).toRealPath();
            recording.dump(dumpFilePath);
            recording.stop();

        } catch (Exception e) {
            throw new Jfr4jdbcTestException(e);
        }
    }

    private Consumer<PreparedStatement> createPreparedStatementExtension() {
        return stmt -> {
            try {
                tl.set(Duration.ofMillis(getQueryDuration("Tokyo")));

                when(stmt.executeQuery()).thenAnswer((m) -> {
                    // execute query
                    long queryDuration = tl.get().toMillis();
                    TimeUnit.MILLISECONDS.sleep(queryDuration);
                    ResultSet resultSet = Mockito.mock(ResultSet.class);
                    // re-initialize sql duration

                    long nextQueryDuration = this.getQueryDuration("Tokyo");
                    tl.set(Duration.ofMillis(nextQueryDuration));
                    return resultSet;
                });

                Mockito.lenient().doAnswer((m) -> {
                    // Specify SQL execution time by pref
                    String pref = m.getArgument(1);
                    int queryDuration = this.getQueryDuration(pref);
                    tl.set(Duration.ofMillis(queryDuration));
                    return null;
                }).when(stmt).setString(anyInt(), anyString());
            } catch (SQLException e) {
                throw new Jfr4JdbcTestRuntimeException("Fail to extend to Prepared Statement", e);
            }
        };
    }

    private Runnable process(DataSource ds) {
        return () -> {
            try (Connection con = ds.getConnection()) {

                think();

                String pref = getPref();
                String sql = getQuery(pref);
                PreparedStatement stmt = con.prepareStatement(sql);
                if (isFixed && "Tokyo".equals(pref)) {

                } else {
                    stmt.setString(1, pref);
                }
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

    private String getQuery(String pref) {
        if (isFixed) {
            return "Tokyo".equals(pref) ? tokyoSql : sql;
        } else {
            return sql;
        }
    }

    private int getQueryDuration(String pref) {

        double rnd = Math.random();
        if (isFixed) {
            return "Tokyo".equals(pref) ? 30 + (int) (rnd * 20) : 20 + (int) (rnd * 20);
        } else {
            return "Tokyo".equals(pref) ? 400 + (int) (rnd * 400) : 20 + (int) (rnd * 20);
        }
    }

    private String getPref() {
        int rnd = (int) (Math.random() * prefs.length);
        return prefs[rnd];
    }
}
