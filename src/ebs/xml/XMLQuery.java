package ebs.xml;

import ebs.context.Context;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksei
 * Date: Oct 10, 2007
 * Time: 10:17:40 PM
 * Company: EBS (c) 2007
 */

public interface XMLQuery {
    void process(XmlSerializer serializer, Connection connection, Context context) throws IOException, SQLException;
}
