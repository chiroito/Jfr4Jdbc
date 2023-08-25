package dev.jfr4jdbc.event;

public interface SavepointEvent extends JdbcEvent {
		void setId(int id);

		void setName(String name);
}
