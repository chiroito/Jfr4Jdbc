package chiroito.jfr4jdbc.event.jfr;

import chiroito.jfr4jdbc.event.JdbcEvent;
import jdk.jfr.Category;
import jdk.jfr.Event;

@Category("jdbc")
public class JfrJdbcEvent extends Event implements JdbcEvent {
}
