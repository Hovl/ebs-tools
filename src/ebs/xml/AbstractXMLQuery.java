package ebs.xml;

import ebs.context.Context;
import ebs.db.NamedQuery;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksey
 * Date: Oct 10, 2007
 * Time: 10:23:14 PM
 * Company: EBS (c) 2007-2012
 */
public abstract class AbstractXMLQuery implements XMLQuery {
    private String tagName;
    private NamedQuery query;

	protected AbstractXMLQuery() {
	}

	protected AbstractXMLQuery(String tagName, NamedQuery query) {
        this.tagName = tagName;
        this.query = query;
    }

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public NamedQuery getQuery() {
		return query;
	}

	public void setQuery(NamedQuery query) {
		this.query = query;
	}

	public void process(XmlSerializer serializer, Connection connection, Context context)
			throws IOException, SQLException
	{	
        PreparedStatement statement = query.getPreparedStatement(connection, context);

        if (statement.execute()) {
            ResultSet rs = statement.getResultSet();
			try {
				while (rs.next()) {
					serializer.startTag(null, tagName);
					writeContent(serializer, connection, rs, context);
					serializer.endTag(null, tagName);
				}
			} finally {
				rs.close();
			}
        } else serializer.attribute(null, "updateCount", Integer.toString(statement.getUpdateCount()));
    }

    protected abstract void writeContent(XmlSerializer serializer, Connection connection, ResultSet resultSet,
										 Context context) throws IOException, SQLException;
}
