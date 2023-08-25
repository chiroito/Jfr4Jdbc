package dev.jfr4jdbc.internal;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Objects;

final class JfrSavepoint implements Savepoint {
	  final Savepoint jdbcSavepoint;
	  final boolean isNamed;

		JfrSavepoint(Savepoint jdbcSavepoint, boolean isNamed) {
		    this.jdbcSavepoint = Objects.requireNonNull(jdbcSavepoint);
			  this.isNamed = isNamed;
		}

	  @Override
	  public int getSavepointId() throws SQLException {
	    	return jdbcSavepoint.getSavepointId();
	  }

	  @Override
	  public String getSavepointName() throws SQLException {
	    	return jdbcSavepoint.getSavepointName();
	  }
}
