module dev.jfr4jdbc {
    requires transitive jdk.jfr;
    requires transitive java.sql;

    exports dev.jfr4jdbc;
    exports dev.jfr4jdbc.event;

    provides java.sql.Driver with dev.jfr4jdbc.JfrServiceLoadedDriver;
}