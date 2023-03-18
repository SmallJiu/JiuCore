package cat.jiu.sql;

import java.util.Map.Entry;
import java.util.StringJoiner;
import java.util.TreeMap;

public class SQLTableType {
	private final TreeMap<String, SQLType> types = new TreeMap<>();
	
	public SQLTableType put(String key, SQLType type) {
		this.types.put(key, type);
		return this;
	}
	public SQLTableType remove(String key) {
		this.types.remove(key);
		return this;
	}
	public SQLTableType set(String key, SQLType type) {
		this.types.replace(key, type);
		return this;
	}
	public boolean has(String key) {
		return this.types.containsKey(key);
	}
	public void clear() {
		this.types.clear();
	}
	
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(", ", "(", ")");
		for(Entry<String, SQLType> value : this.types.entrySet()) {
			sj.add(value.getKey() + " " + value.getValue().toString());
		}
		return sj.toString();
	}
}
