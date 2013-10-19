package ebs.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import ebs.context.Context;
import ebs.db.NamedQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksey
 * Date: Oct 13, 2009
 * Time: 7:03:49 PM
 * Company: EBS (c) 2009
 */

public abstract class AbstractJSONQuery implements JSONQuery {
    private NamedQuery query;

	protected AbstractJSONQuery() {
	}

	protected AbstractJSONQuery(NamedQuery query) {
        this.query = query;
    }

	public JsonArray process(Connection connection, Context context) throws SQLException {
		JsonArray array = new JsonArray();

		PreparedStatement statement = query.getPreparedStatement(connection, context);
		if (statement.execute()) {
			ResultSet rs = statement.getResultSet();
			try {
				while (rs.next()) {
					array.add(writeContent(connection, rs, context));
				}
			} finally {
				rs.close();
			}
		} else {
			JsonObject updateResult = new JsonObject();
			updateResult.addProperty("updateCount", statement.getUpdateCount());
			array.add(updateResult);
		}

		return array;
	}

    protected abstract JsonElement writeContent(Connection connection, ResultSet resultSet, Context context) throws SQLException;
}
