package dev.jfr4jdbc.artifact;

import dev.jfr4jdbc.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class ModuleTest {

    Module module = Jfr4JdbcDataSource.class.getModule();

    @DisplayName("Module is named")
    @Test
    void isNamedTest() throws Exception {
        assertTrue(module.isNamed());
    }

    @DisplayName("Module Name")
    @Test
    void moduleNameTest() throws Exception {
        assertEquals("dev.jfr4jdbc", module.getName());
    }

    @DisplayName("Exported")
    @Test
    void isExported() throws Exception {
        assertTrue(module.isExported("dev.jfr4jdbc"));
        assertTrue(module.isExported("dev.jfr4jdbc.event"));
        assertFalse(module.isExported("dev.jfr4jdbc.internal"));
    }
}
