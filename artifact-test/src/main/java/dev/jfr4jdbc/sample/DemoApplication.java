package dev.jfr4jdbc.sample;

import dev.jfr4jdbc.JfrDataSource;
import jdk.jfr.Configuration;
import jdk.jfr.Recording;
import org.postgresql.ds.PGPoolingDataSource;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/*
java -XX:StartFlightRecording=disk=true,dumponexit=true,duration=60s,filename=DemoApp.jfr
 */
public class DemoApplication {
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

        JfrDataSource jfrDs = new JfrDataSource(postgreDs);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 10; i++) {
            Task t = new Task(jfrDs.getConnection());
            executorService.submit(t);
        }

        TimeUnit.SECONDS.sleep(10);
        executorService.shutdownNow();

        r.stop();
        r.dump(Files.createFile(Paths.get("DemoApp.jfr")));
    }
}

class Task implements Runnable {

    private final Connection con;

    public Task(Connection con) {
        this.con = con;
    }

    @Override
    public void run() {
        try (Connection con = this.con;
             PreparedStatement stmt = con.prepareStatement("SELECT datname FROM pg_database");
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            System.out.println(rs.getString("datname"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
