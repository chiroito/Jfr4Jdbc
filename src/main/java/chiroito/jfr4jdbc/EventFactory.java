package chiroito.jfr4jdbc;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;

import com.oracle.jrockit.jfr.EventToken;
import com.oracle.jrockit.jfr.InvalidEventDefinitionException;
import com.oracle.jrockit.jfr.InvalidValueException;
import com.oracle.jrockit.jfr.NoSuchEventException;
import com.oracle.jrockit.jfr.Producer;
import com.oracle.jrockit.jfr.client.EventSettingsBuilder;
import com.oracle.jrockit.jfr.client.FlightRecorderClient;
import com.oracle.jrockit.jfr.client.FlightRecordingClient;
import com.oracle.jrockit.jfr.management.NoSuchRecordingException;

import chiroito.jfr4jdbc.event.CancelEvent;
import chiroito.jfr4jdbc.event.CloseEvent;
import chiroito.jfr4jdbc.event.CommitEvent;
import chiroito.jfr4jdbc.event.ConnectEvent;
import chiroito.jfr4jdbc.event.ResultSetEvent;
import chiroito.jfr4jdbc.event.RollbackEvent;
import chiroito.jfr4jdbc.event.StatementEvent;
import chiroito.jfr4jdbc.event.jfr.JfrCancelEvent;
import chiroito.jfr4jdbc.event.jfr.JfrCloseEvent;
import chiroito.jfr4jdbc.event.jfr.JfrCommitEvent;
import chiroito.jfr4jdbc.event.jfr.JfrConnectionEvent;
import chiroito.jfr4jdbc.event.jfr.JfrResultSetEvent;
import chiroito.jfr4jdbc.event.jfr.JfrRollbackEvent;
import chiroito.jfr4jdbc.event.jfr.JfrStatementEvent;

@SuppressWarnings({ "deprecation", "restriction" })
public abstract class EventFactory {

	private static EventFactory s_factory = new JfrEventFactory();

	public static EventFactory getDefaultEventFactory() {
		return s_factory;
	}

	public abstract StatementEvent createStatementEvent();

	public abstract CancelEvent createCancelEvent();

	public abstract CloseEvent createCloseEvent();

	public abstract CommitEvent createCommitEvent();

	public abstract ConnectEvent createConnectEvent();

	public abstract ResultSetEvent createResultSetEvent();

	public abstract RollbackEvent createRollbackEvent();
}

@SuppressWarnings({ "unused", "deprecation", "restriction" })
class JfrEventFactory extends EventFactory {

	private static Producer PRODUCER = null;

	private static final String PRODUCER_NAME = "Jfr4Jdbc";
	private static final String PRODUCER_DESCRIPTION = "JDBC";
	private static final String PRODUCER_URL = "http://chiroito/Jfr4Jdbc/";
	private static final String EVENT_URL = PRODUCER_URL + "*";

	public static EventToken statementEventToken;
	public static EventToken connectionEventToken;
	public static EventToken closeEventToken;
	public static EventToken resultSetEventToken;
	public static EventToken commitEventToken;
	public static EventToken rollbackEventToken;
	public static EventToken cancelEventToken;

	static {
		try {
			JfrEventFactory.init();
		} catch (Jfr4JdbcException e) {
			throw new RuntimeException("Could not use Jfr4Jdbc.", e);
		}
	}

	public static void init() throws Jfr4JdbcException {

		try {

			// Add events of Jfr4Jdbc.
			Producer producer = new Producer(PRODUCER_NAME, PRODUCER_DESCRIPTION, PRODUCER_URL);

			statementEventToken = producer.addEvent(JfrStatementEvent.class);
			connectionEventToken = producer.addEvent(JfrConnectionEvent.class);
			closeEventToken = producer.addEvent(JfrCloseEvent.class);
			resultSetEventToken = producer.addEvent(JfrResultSetEvent.class);
			commitEventToken = producer.addEvent(JfrCommitEvent.class);
			rollbackEventToken = producer.addEvent(JfrRollbackEvent.class);
			cancelEventToken = producer.addEvent(JfrCancelEvent.class);

			producer.register();

			addSettings();

			PRODUCER = producer;

		} catch (URISyntaxException | NullPointerException | InvalidEventDefinitionException | InvalidValueException e) {
			throw new Jfr4JdbcException("Jfr4Jdbc could not be initialized.", e);
		}
	}

	private static void addSettings() throws Jfr4JdbcException {
		// Add settings to flight recorder.
		EventSettingsBuilder builder = new EventSettingsBuilder();
		try {
			builder.createSetting(EVENT_URL, true, true, 0L, 0L);

			// TODO : ここから下の要件を検討したい。
			FlightRecorderClient frClient = new FlightRecorderClient();
			List<CompositeData> addEventSettings = builder.createSettings(frClient);

			// Reflect the settings to recordings.
			List<FlightRecordingClient> recordings = frClient.getRecordingObjects();
			for (FlightRecordingClient recording : recordings) {
				ObjectName objctName = recording.getObjectName();
				List<CompositeData> currentEventSettings = frClient.getEventSettings(objctName);
				currentEventSettings.addAll(addEventSettings);
				frClient.updateEventSettings(objctName, currentEventSettings);
			}
		} catch (OpenDataException | NoSuchEventException | NoSuchRecordingException | URISyntaxException | InstanceNotFoundException | NullPointerException | IOException e) {
			throw new Jfr4JdbcException("Jfr4Jdbc could not be initialized.", e);
		}
	}

	@Override
	public StatementEvent createStatementEvent() {
		return new JfrStatementEvent(JfrEventFactory.statementEventToken);
	}

	@Override
	public CancelEvent createCancelEvent() {
		return new JfrCancelEvent(JfrEventFactory.cancelEventToken);
	}

	@Override
	public CloseEvent createCloseEvent() {
		return new JfrCloseEvent(JfrEventFactory.closeEventToken);
	}

	@Override
	public CommitEvent createCommitEvent() {
		return new JfrCommitEvent(JfrEventFactory.commitEventToken);
	}

	@Override
	public ConnectEvent createConnectEvent() {
		return new JfrConnectionEvent(JfrEventFactory.connectionEventToken);
	}

	@Override
	public ResultSetEvent createResultSetEvent() {
		return new JfrResultSetEvent(JfrEventFactory.resultSetEventToken);
	}

	@Override
	public RollbackEvent createRollbackEvent() {
		return new JfrRollbackEvent(JfrEventFactory.rollbackEventToken);
	}
}