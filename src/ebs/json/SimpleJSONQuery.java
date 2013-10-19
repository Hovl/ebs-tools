package ebs.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ebs.context.Context;
import ebs.db.NamedQuery;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksey
 * Date: Oct 13, 2009
 * Time: 7:08:16 PM
 * Company: EBS (c) 2009-2011
 */

public class SimpleJSONQuery extends AbstractJSONQuery implements Serializable {
    private String[] columns;

	public SimpleJSONQuery() {
		super();
	}

	public SimpleJSONQuery(NamedQuery query, String[] columns) {
        super(query);
        this.columns = columns;
    }

	@Override
	protected JsonElement writeContent(Connection connection, ResultSet resultSet, Context context) throws SQLException {
		JsonObject element = new JsonObject();
		for (String column : columns) {
			Object o = resultSet.getObject(column);
			if(o instanceof Number) {
				element.addProperty(column, (Number) o);
			} else if(o instanceof Boolean) {
				element.addProperty(column, (Boolean) o);
			} else {
				element.addProperty(column, resultSet.getString(column));
			}
		}
		return element;
	}
}
