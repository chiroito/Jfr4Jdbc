package chiroito.jfr4jdbc.event.jfr;

import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.TimedEvent;

import chiroito.jfr4jdbc.event.JdbcEvent;

@SuppressWarnings({ "deprecation", "restriction" })
public class JfrJdbcEvent extends TimedEvent implements JdbcEvent {

	public JfrJdbcEvent(EventToken eventToken) {
		super(eventToken);
	}
}
