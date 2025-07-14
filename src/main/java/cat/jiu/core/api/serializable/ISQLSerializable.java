package cat.jiu.core.api.serializable;

import cat.jiu.sql.SQLValues;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ISQLSerializable {
	SQLValues write(SQLValues value);
	void read(ResultSet result) throws SQLException;
}
