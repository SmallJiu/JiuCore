package cat.jiu.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.StringJoiner;

public class SQLDatabase implements AutoCloseable {
	protected final Connection con;
	protected final SQLDatabaseType database;
	
	public SQLDatabase(Connection con) {
		this.con = con;
		this.database = this.getDatabaseType(con.getClass().getName());
	}
	public SQLDatabase(String url) throws SQLException {
		this(DriverManager.getConnection(url));
	}
	public SQLDatabase(String url, String username, String password) throws SQLException {
		this(DriverManager.getConnection(url, username, password));
	}
	public SQLDatabase(String url, Properties info) throws SQLException {
		this(DriverManager.getConnection(url, info));
	}
	
	public int insert(String table, SQLValues values) throws SQLException {
		try(PreparedStatement stmt = this.con.prepareStatement(new StringBuilder()
				.append("insert into ")
				.append(values.toTable(table))
				.toString())) {
			return stmt.executeUpdate();
		}
	}
	
	public int delete(String table, SQLWhere where) throws SQLException {
		StringBuilder sb = new StringBuilder()
				.append("delete from ")
				.append(table);
		if(where!=null) {
			sb.append(" where ")
				.append(where.toString());
		}
		sb.append(";");
		try(PreparedStatement stmt = this.getPreparedStatement(sb.toString(), where!=null ? where.getArgs() : null)){
			return stmt.executeUpdate();
		}
	}
	
	public int update(String table, SQLValues values, SQLWhere where) throws SQLException {
		StringBuilder sb = new StringBuilder()
				.append("update ")
				.append(table)
				.append(" set ")
				.append(values.toUpdata());
		if(where!=null) {
			sb.append(" where ")
				.append(where.toString());
		}
		sb.append(";");
		
		try(PreparedStatement stmt = this.getPreparedStatement(sb.toString(), where!=null ? where.getArgs() : null)){
			return stmt.executeUpdate();
		}
	}
	
	protected static final String[] keys = new String[] {"*"};
	public ResultSet select(String table, SQLWhere where) throws SQLException {
		return this.select(table, keys, where);
	}
	
	public ResultSet select(String table, String[] keys, SQLWhere where) throws SQLException {
		StringJoiner key = new StringJoiner(",");
		for(int i = 0; i < keys.length; i++) {
			key.add(keys[i]);
		}
		
		StringBuilder sb = new StringBuilder()
				.append("select ")
				.append(key)
				.append(" from ")
				.append(table);
		
		if(where!=null) {
			sb.append(" where ")
				.append(where.toString());
		}
		sb.append(";");
		
		return this.getPreparedStatement(sb.toString(), where!=null ? where.getArgs() : null).executeQuery();
	}
	
	public boolean dropIndex(String table, String index) throws SQLException {
		switch(this.database) {
			case MYSQL: return this.execute("alter table " + table + " drop index " + index);
			case SQLSERVER: return this.execute("drop index " + table + "." + index); 
			default:
				break;
		}
		return false;
	}
	
	public boolean dropTable(String name) throws SQLException {
		return this.execute("drop table if exists " + name);
	}
	public boolean dropTableAllValues(String name) throws SQLException {
		return this.execute("delete from " + name);
	}
	public boolean dropDatabase(String name) throws SQLException {
		return this.execute("drop database if exists " + name);
	}
	
	public boolean execute(String sql) throws SQLException {
		try(PreparedStatement stmt = this.con.prepareStatement(sql)) {
			return stmt.execute();
		}
	}
	public boolean execute(String sql, Object[] whereArgs) throws SQLException {
		try(PreparedStatement stmt = this.getPreparedStatement(sql, whereArgs)) {
			return stmt.execute();
		}
	}
	
	public PreparedStatement getPreparedStatement(String sql, Object[] whereArgs) throws SQLException {
		System.out.println(sql);
		PreparedStatement stmt = this.con.prepareStatement(sql);
		for(int i = 1; whereArgs!=null && i < whereArgs.length+1; i++) {
			stmt.setString(i, String.valueOf(whereArgs[i-1]));
		}
		return stmt;
	}
	
	public boolean createTable(String name, SQLTableType table) throws SQLException {
		StringJoiner sql = new StringJoiner(" ", "", ";")
				.add("create table if not exists")
				.add(name + table.toString());
		return this.execute(sql.toString());
	}
	
	public SQLType createType(JDBCType type) {
		return new SQLType(this.database, type);
	}

	@Override
	public void close() throws Exception {
		this.con.close();
	}
	
	protected SQLDatabaseType getDatabaseType(String name) {
		name = name.toLowerCase();
		if(name.startsWith("org.sqlite")) 				return SQLDatabaseType.SQLITE;
		if(name.startsWith("com.mysql")) 				return SQLDatabaseType.MYSQL;
		if(name.startsWith("com.microsoft.sqlserver"))  return SQLDatabaseType.SQLSERVER;
		if(name.startsWith("oracle.jdbc")) 				return SQLDatabaseType.ORACLE;
		if(name.startsWith("sun.jdbc.odbc")) 			return SQLDatabaseType.MICROSOFT_ACCESS;
		if(name.startsWith("com.ibm.db2")) 				return SQLDatabaseType.IBM_DB2;
		if(name.startsWith("org.postgresql")) 			return SQLDatabaseType.POSTGRE_SQL;
		
		return SQLDatabaseType.UNKNOW;
	}
}
