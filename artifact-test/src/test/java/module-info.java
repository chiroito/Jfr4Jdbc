open module artifact.test {
    requires dev.jfr4jdbc;

    requires transitive org.junit.jupiter.engine;
    requires transitive org.junit.jupiter.api;

    uses java.sql.Driver;
}