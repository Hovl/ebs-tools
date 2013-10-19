package ebs.xml;

import ebs.context.Context;
import ebs.db.NamedQuery;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksey
 * Date: Nov 7, 2007
 * Time: 5:44:43 PM
 * Company: EBS (c) 2007-2012
 */
public class SimpleXMLQuery extends AbstractXMLQuery implements Serializable {
	private String[] columns;

	public SimpleXMLQuery() {
		super();
	}

	public SimpleXMLQuery(String tagName, NamedQuery query, String[] columns) {
		super(tagName, query);
		this.columns = columns;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	protected void writeContent(XmlSerializer serializer, Connection connection, ResultSet resultSet, Context context)
			throws IOException, SQLException {
		for (String column : columns) {
			writeStringResultValue(serializer, resultSet, column);
		}
	}

	protected static void writeStringResultValue(XmlSerializer serializer, ResultSet resultSet, String column)
			throws SQLException, IOException {
		String value = resultSet.getString(column);
		writeValueToSerializer(serializer, column, value);
	}

	protected static void writeValueToSerializer(XmlSerializer serializer, String column, String value)
			throws IOException {
		serializer.attribute(null, column, value == null ? "" : value);
	}
}
