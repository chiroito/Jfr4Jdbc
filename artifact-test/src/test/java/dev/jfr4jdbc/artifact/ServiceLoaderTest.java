package dev.jfr4jdbc.artifact;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Driver;
import java.util.ServiceLoader;

public class ServiceLoaderTest {
    @DisplayName("ServiceLoader loads Jfr4Jdbc")
    @Test
    void loadedTest() throws Exception {
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        long jfrDriverCount = loader.stream().filter(d -> d.get().getClass().getCanonicalName().equals("dev.jfr4jdbc.Jfr4JdbcDriver")).count();

        assertEquals(1, jfrDriverCount);
    }
}
