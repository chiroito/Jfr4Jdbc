package dev.jfr4jdbc.sample;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Driver;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ServiceLoaderTest {
    @DisplayName("ServiceLoader loads Jfr4Jdbc")
    @Test
    void loadedTest() throws Exception {
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        long jfrDriverCount = loader.stream().filter(d -> d.get().getClass().getCanonicalName().equals("dev.jfr4jdbc.JfrServiceLoadedDriver")).count();

        assertEquals(1, jfrDriverCount);
    }
}
