package cat.jiu.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

public class SQLWhere {
	protected final ArrayList<ISQLWhere> wheres = new ArrayList<>();
	protected final Object[] args;
	public SQLWhere(Object... whereArgs) {
		this.args = whereArgs;
	}
	
	public SQLWhere add(ISQLWhere where) {
		this.wheres.add(where);
		return this;
	}
	public SQLWhere remove(int index) {
		this.wheres.remove(index);
		return this;
	}
	public SQLWhere set(int index, ISQLWhere where) {
		this.wheres.set(index, where);
		return this;
	}
	public Object[] getArgs() {
		return Arrays.copyOf(this.args, this.args.length);
	}
	
	@Override
	public String toString() {
		StringJoiner sj = new StringJoiner(" ");
		ISQLWhere pre = null;
		for(int i = 0; i < this.wheres.size(); i++) {
			if(pre!=null) {
				sj.add(pre.getNextOperator().name().toLowerCase());
			}
			ISQLWhere where = this.wheres.get(i);
			sj.add(where.toSQL());
			pre = where;
		}
		return sj.toString();
	}
	
	public static class Where implements ISQLWhere {
		protected final String id;
		protected final SQLOperator operator;
		protected final String wildcard;
		protected final SQLLogicOperator nextOperator;
		/**
		 * will like 'id>=?'
		 * @param id the sql value id
		 * @param operator the operator
		 * @param nextOperator the next where logic operator, if this is last where, can be null
		 */
		public Where(String id, SQLOperator operator, String wildcard, SQLLogicOperator nextOperator) {
			this.id = id;
			this.operator = operator;
			this.wildcard = this.operator == SQLOperator.IS_NULL ? "" : wildcard;
			this.nextOperator = nextOperator;
		}
		public String getId() {return id;}
		public SQLOperator getOperator() {return operator;}
		public SQLLogicOperator getNextOperator() {return nextOperator;}
		public String toSQL() {
			StringJoiner sb = new StringJoiner(" ");
			
			sb.add(this.id);
			sb.add(this.operator.sql);
			
			if(this.operator == SQLOperator.LIKE
			|| this.operator == SQLOperator.NOT_LIKE) {
				sb.add(new StringBuilder()
						.append("'")
						.append(this.wildcard)
						.append("'"));
			}else {
				if(this.operator != SQLOperator.IS_NULL
				|| this.operator != SQLOperator.EXISTS
				|| this.operator != SQLOperator.NOT_EXISTS) sb.add(this.wildcard);
			}
			
			return sb.toString();
		}
	}

	public static class Wheres implements ISQLWhere {
		protected final SQLLogicOperator nextOperator;
		protected final ArrayList<ISQLWhere> wheres = new ArrayList<>();
		/**
		 * will like '(id=? and coin>=? or ((score<=? or balance>=?) and (name=? and nickname=?)))'
		 * @param nextOperator the next where logic operator, if this is last where, can be null
		 */
		public Wheres(SQLLogicOperator nextOperator) {
			this.nextOperator = nextOperator;
		}
		/**
		 * add sub to list
		 * @param where sub where
		 */
		public Wheres add(ISQLWhere where) {
			this.wheres.add(where);
			return this;
		}
		public Wheres remove(int index) {
			this.wheres.remove(index);
			return this;
		}
		public Wheres set(int index, ISQLWhere where) {
			this.wheres.set(index, where);
			return this;
		}
		public String getId() {return null;}
		public SQLOperator getOperator() {return null;}
		public SQLLogicOperator getNextOperator() {return nextOperator;}
		@Override
		public String toSQL() {
			StringJoiner sj = new StringJoiner(" ", "(", ")");
			ISQLWhere pre = null;
			for(int i = 0; i < this.wheres.size(); i++) {
				if(pre!=null) {
					sj.add(pre.getNextOperator().name().toLowerCase());
				}
				ISQLWhere where = this.wheres.get(i);
				sj.add(where.toSQL());
				pre = where;
			}
			return sj.toString();
		}
	}
	
	public static class InnerSelect extends SQLWhere {
		protected final String table;
		protected final String[] keys;
		public InnerSelect(String table, String[] keys) {
			this.table = table;
			this.keys = keys;
		}
		public InnerSelect add(ISQLWhere where) {
			this.wheres.add(where);
			return this;
		}
		public InnerSelect remove(int index) {
			this.wheres.remove(index);
			return this;
		}
		public InnerSelect set(int index, ISQLWhere where) {
			this.wheres.set(index, where);
			return this;
		}
		public String toString() {
			StringJoiner key = new StringJoiner(",");
			for(int i = 0; i<this.keys.length; i++){
				key.add(this.keys[i]);
			}
			return "(select "+ key + " from " + this.table + " where " + super.toString() + ")";
		}
	}
	
	interface ISQLWhere {
		String getId();
		SQLOperator getOperator();
		SQLLogicOperator getNextOperator();
		String toSQL();
	}
}
