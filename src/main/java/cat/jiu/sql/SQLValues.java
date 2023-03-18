package cat.jiu.sql;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeMap;

public class SQLValues {
	private final TreeMap<String, Object> values = new TreeMap<>();
	public SQLValues() {}
	public SQLValues(Map<String, Object> values) {
		this.values.putAll(values);
	}
	public SQLValues(SQLValues values) {
		this.values.putAll(values.values);
	}
	
	public int size() {return this.values.size();}
    public boolean isEmpty() {return this.values.isEmpty();}
	public Object get(String key) {return this.values.get(key);}
	public void remove(String key) {this.values.remove(key);}
	public void clear() {this.values.clear();}
	public boolean containsKey(String key) {return this.values.containsKey(key);}
	
	public SQLValues putNull(String key) {
		this.values.put(key, null); 
		return this;
	}
	public SQLValues put(String key, Object value) {
        this.values.put(key, value);
		return this;
	}
	
	public String getAsString(String key) {
		Object o = this.values.get(key);
		if(o instanceof String) {
			return (String)o;
		}
		return o!=null ? o.toString() : null;
	}
	
	public Number getAsNumber(String key) {
		Object o = this.values.get(key);
		if(o instanceof Number) {
			return (Number)o;
		}
		return null;
	}

	public Byte getAsByte(String key) {
		Number num = this.getAsNumber(key);
		if(num!=null) {
			if(num instanceof Byte) {
				return (Byte) num;
			}
			return num.byteValue();
		}else {
			Object o = this.values.get(key);
			if(o instanceof CharSequence) {
				try {
					return Byte.valueOf(o.toString());
				}catch(Exception e) {}
			}
		}
		return null;
	}
	public Short getAsShort(String key) {
		Number num = this.getAsNumber(key);
		if(num!=null) {
			if(num instanceof Short) {
				return (Short) num;
			}
			return num.shortValue();
		}else {
			Object o = this.values.get(key);
			if(o instanceof CharSequence) {
				try {
					return Short.valueOf(o.toString());
				}catch(Exception e) {}
			}
		}
		return null;
	}
	public Integer getAsInt(String key) {
		Number num = this.getAsNumber(key);
		if(num!=null) {
			if(num instanceof Integer) {
				return (Integer) num;
			}
			return num.intValue();
		}else {
			Object o = this.values.get(key);
			if(o instanceof CharSequence) {
				try {
					return Integer.valueOf(o.toString());
				}catch(Exception e) {}
			}
		}
		return null;
	}
	public Long getAsLong(String key) {
		Number num = this.getAsNumber(key);
		if(num!=null) {
			if(num instanceof Long) {
				return (Long) num;
			}
			return num.longValue();
		}else {
			Object o = this.values.get(key);
			if(o instanceof CharSequence) {
				try {
					return Long.valueOf(o.toString());
				}catch(Exception e) {}
			}
		}
		return null;
	}
	public Float getAsFloat(String key) {
		Number num = this.getAsNumber(key);
		if(num!=null) {
			if(num instanceof Float) {
				return (Float) num;
			}
			return num.floatValue();
		}else {
			Object o = this.values.get(key);
			if(o instanceof CharSequence) {
				try {
					return Float.valueOf(o.toString());
				}catch(Exception e) {}
			}
		}
		return null;
	}
	public Double getAsDouble(String key) {
		Number num = this.getAsNumber(key);
		if(num!=null) {
			if(num instanceof Double) {
				return (Double) num;
			}
			return num.doubleValue();
		}else {
			Object o = this.values.get(key);
			if(o instanceof CharSequence) {
				try {
					return Double.valueOf(o.toString());
				}catch(Exception e) {}
			}
		}
		return null;
	}
	public Boolean getAsBoolean(String key) {
		Object o = this.values.get(key);
		if(o instanceof Boolean) {
			return (Boolean) o;
		}else {
			if(o instanceof CharSequence) {
				try {
					return Boolean.valueOf(o.toString()) || "1".equals(o);
				}catch(Exception e) {}
			}else if(o instanceof Number) {
				return this.getAsInt(key) != 0;
			}
		}
		return null;
	}
	
	public byte[] getAsByteArray(String key) {
		Object o = this.values.get(key);
		if(o instanceof byte[]) {
			return (byte[]) o;
		}
		return null;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public ArrayList<String> getAsStringList(String key){
		Object o = this.values.get(key);
		if(o instanceof ArrayList) {
			ArrayList list = (ArrayList) o;
			for(Object obj : list) {
				if(obj instanceof String) return list;
				break;
			}
		}
		return null;
	}
	
	public Set<Entry<String, Object>> valueSet() {
		return this.values.entrySet();
	}
	public Set<String> keySet() {
		return this.values.keySet();
	}
	
	public String toTable(String table) {
		StringBuilder sb = new StringBuilder();
		sb.append(table);
		
		StringJoiner keys = new StringJoiner(",", "(", ")");
		StringJoiner values = new StringJoiner(",", "(", ")");
		for(Entry<String, Object> value : this.valueSet()) {
			keys.add(value.getKey());
			values.add(String.valueOf(value.getValue()));
		}
		sb.append(keys.toString())
			.append(" values")
			.append(values.toString())
			.append(";");
		
		return sb.toString();
	}
	
	public String toUpdata() {
		return this.toString(",");
	}
	
	private String toString(String de) {
		StringBuilder sb = new StringBuilder();
        for (String name : this.values.keySet()) {
            if (sb.length() > 0) sb.append(de);
            sb.append(name + "=" + String.valueOf(this.get(name)));
        }
        return sb.toString();
	}
	
	@Override
    public String toString() {
        return this.toString(" ");
    }
	
	@Override
	public int hashCode() {
		return values.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		SQLValues other = (SQLValues) obj;
		if(values == null) {
			if(other.values != null) return false;
		}else if(!values.equals(other.values)) return false;
		return true;
	}
}
