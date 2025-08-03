package cat.jiu.sql;

public enum SQLDatabaseDriver {
	SQLite("jdbc:sqlite", "org.sqlite.JDBC"),
	MYSQL("jdbc:mysql", "com.mysql.cj.jdbc.Driver"),
	SQLServer("jdbc:sqlserver", "com.microsoft.sqlserver.jdbc.SQLServerDriver"),
	Oracle("jdbc:oracle", "oracle.jdbc.driver.OracleDriver"),
	Microsoft_Access_2010("jdbc:Access", "com.hxtt.sql.access.AccessDriver"),
	Microsoft_Access_ODBC("jdbc:odbc", "sun.jdbc.odbc.JdbcOdbcDriver"),
	IBM_DB2("jdbc:db2", "com.ibm.db2.jcc.DB2Driver"),
	PostgreSQL("jdbc:postgresql", "org.postgresql.Driver"),
	Sybase("jdbc:sybase", "com.sybase.jdbc2.jdbc.SybDriver"),
	Informix_NORMAL("jdbc:informix-sql", "com.informix.jdbc.IfxDriver"),
	Informix_DRDA("jdbc:ids", "com.ibm.db2.jcc.DB2Driver"),
	H2("jdbc:h2", "org.h2.Driver"),
	H2_Memory("jdbc:h2:mem", "org.h2.Driver"),
	UNKNOWN("jdbc:unknown", "unknown.jdbc.Driver");

	public final String prefix, driver;
	SQLDatabaseDriver(String prefix, String driver) {
		this.prefix = prefix.endsWith(":") ? prefix : prefix+":";
		this.driver = driver;
	}

	public void loadDriver() {
		try {
			Class.forName(this.driver);
		} catch (Exception ignored) {
		}
	}
	public String url(String url) {
		return this.prefix + url;
	}
}
