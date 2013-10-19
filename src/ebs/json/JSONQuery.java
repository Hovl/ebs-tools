package ebs.json;

import com.google.gson.JsonArray;
import ebs.context.Context;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksey
 * Date: Oct 13, 2009
 * Time: 6:40:08 PM
 * Company: EBS (c) 2009-2011
 */

public interface JSONQuery {
	public JsonArray process(Connection connection, Context context) throws SQLException;
}
