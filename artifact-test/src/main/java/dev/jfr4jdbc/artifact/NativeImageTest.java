package dev.jfr4jdbc.artifact;

import dev.jfr4jdbc.JfrDataSource;
import org.postgresql.ds.PGPoolingDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class NativeImageTest {
    public static void main(String[] args) throws Exception {

        PGPoolingDataSource postgreDs = new PGPoolingDataSource();

        String hostname = System.getenv("POSTGRES_HOST");
        String password = System.getenv("POSTGRES_PASSWORD");

        System.out.println("echo POSTGRES_HOST = " + hostname);
        System.out.println("echo POSTGRES_PASSWORD = " + password);

        postgreDs.setUrl("jdbc:postgresql://" + hostname + ":5432/postgres");
        postgreDs.setUser("postgres");
        postgreDs.setPassword(password);

        JfrDataSource jfrDs = new JfrDataSource(postgreDs);

        try (Connection con = jfrDs.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT datname FROM pg_database");
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            System.out.println(rs.getString("datname"));
        }
    }
}
