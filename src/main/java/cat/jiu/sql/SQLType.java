package cat.jiu.sql;

import java.sql.JDBCType;
import java.util.StringJoiner;

public class SQLType {
	protected final SQLDatabaseType database;
	protected JDBCType type;
	protected Integer length;
	protected boolean isPrimaryKey;
	protected boolean isAutoIncrement;
	protected boolean isNotNull;
	protected Object default_value;
	
	public SQLType(SQLDatabaseType database, JDBCType type) {
		this.type = type;
		this.database = database;
	}
	
	public SQLType setPrimaryKey(boolean isPrimaryKey) {
		this.isPrimaryKey = isPrimaryKey;
		return this;
	}
	public SQLType setLength(int length) {
		this.length = length;
		return this;
	}
	public SQLType setAutoIncrement(boolean isAutoIncrement) {
		if(this.type == JDBCType.INTEGER)
			this.isAutoIncrement = isAutoIncrement;
		return this;
	}
	public SQLType setNotNull(boolean isNotNull) {
		this.isNotNull = isNotNull;
		return this;
	}
	public SQLType setDefaultValue(Object value) {
		this.default_value = value;
		return this;
	}
	public SQLType setType(JDBCType type) {
		if(type != null) this.type = type;
		return this;
	}
	
	@Override
	public String toString() {
		StringJoiner sb = new StringJoiner(" ");
		
		StringBuilder type = new StringBuilder(this.type.getName().toLowerCase());
		if(this.length!=null && !this.isAutoIncrement) {
			type.append("(")
				.append(this.length)
				.append(")");
		}
		sb.add(type);
		
		if(this.isPrimaryKey) 
			sb.add("primary key");
		if(this.isAutoIncrement) 
			sb.add(this.getAutoincrement());
		
		if(this.isNotNull) 
			sb.add("not null");
		if(this.default_value!=null) 
			sb.add("default");
			sb.add(String.valueOf(this.default_value));
		
		return sb.toString();
	}
	
	protected String getAutoincrement() {
		switch(this.database) {
			case MYSQL: return "auto_increment";
			case MICROSOFT_ACCESS:
			case SQLITE : return "autoincrement";
			case SQLSERVER: return "indentity";
			case ORACLE: return "increment";
			case IBM_DB2: return "generated always as identity";
			case POSTGRE_SQL: return "serial";
			default: return "";
		}
	}
}
