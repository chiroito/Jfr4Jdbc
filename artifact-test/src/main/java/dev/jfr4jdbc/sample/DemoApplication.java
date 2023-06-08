package dev.jfr4jdbc.sample;

import dev.jfr4jdbc.JfrDataSource;
import dev.jfr4jdbc.interceptor.impl.period.PeriodInterceptorFactory;
import jdk.jfr.Configuration;
import jdk.jfr.Recording;
import org.postgresql.ds.PGPoolingDataSource;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
java -XX:StartFlightRecording=disk=true,dumponexit=true,duration=60s,filename=DemoApp.jfr
 */
public class DemoApplication {

    private static final int TASK_NUM = 10;

    public static void main(String[] args) throws Exception {

        Configuration c = Configuration.getConfiguration("default");
        Recording r = new Recording(c);
        r.start();

        PGPoolingDataSource postgreDs = new PGPoolingDataSource();

        String hostname = System.getenv("POSTGRES_HOST");
        String password = System.getenv("POSTGRES_PASSWORD");

        System.out.println("echo POSTGRES_HOST = " + hostname);
        System.out.println("echo POSTGRES_PASSWORD = " + password);

        postgreDs.setUrl("jdbc:postgresql://" + hostname + ":5432/postgres");
        postgreDs.setUser("postgres");
        postgreDs.setPassword(password);

        JfrDataSource jfrDs = new JfrDataSource(postgreDs, new PeriodInterceptorFactory());
        List<Task> taskList = new ArrayList<>(TASK_NUM);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < TASK_NUM; i++) {
            Task t = new Task(jfrDs);
            taskList.add(t);
            executorService.submit(t);
        }

        while (taskList.stream().filter(Task::isCompleted).count() != TASK_NUM) {
            TimeUnit.MILLISECONDS.sleep(100);
        }
        executorService.shutdownNow();

        r.stop();
        r.dump(Files.createFile(Paths.get("DemoApp.jfr")));
    }
}

class Task implements Runnable {

    private volatile boolean completed = false;

    private final DataSource ds;

    public Task(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void run() {
        try (Connection con = this.ds.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT datname FROM pg_database");
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            System.out.println(rs.getString("datname"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            completed = true;
        }
    }

    public boolean isCompleted() {
        return completed;
    }
}
