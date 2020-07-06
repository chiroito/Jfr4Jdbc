package dev.jfr4jdbc;

public class Jfr4JdbcTestRuntimeException extends RuntimeException {

    public Jfr4JdbcTestRuntimeException(String message, Exception e) {
        super(message, e);
    }

    public Jfr4JdbcTestRuntimeException(String message) {
        super(message);
    }
}
