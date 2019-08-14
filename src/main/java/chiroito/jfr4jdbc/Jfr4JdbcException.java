package chiroito.jfr4jdbc;

public class Jfr4JdbcException extends Exception {

    private static final long serialVersionUID = 1399094602315268521L;

    public Jfr4JdbcException(String message) {
        super(message);
    }

    public Jfr4JdbcException(Throwable cause) {
        super(cause);
    }

    public Jfr4JdbcException(String message, Throwable cause) {
        super(message, cause);
    }

    public Jfr4JdbcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
