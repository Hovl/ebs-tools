package ebs.web;

import ebs.context.RequestContext;
import ebs.db.*;
import ebs.tools.XMLTools;
import ebs.xml.XMLQuery;
import org.jdom.Document;
import org.slf4j.LoggerFactory;
import org.xmlpull.mxp1_serializer.MXSerializer;
import org.xmlpull.v1.XmlSerializer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by Dubov Aleksey
 * Date: Oct 13, 2009
 * Time: 4:31:16 PM
 * Company: EBS (c) 2009
 */

public abstract class BasicXMLServlet extends HttpServlet {
    protected static void writeFailure(HttpServletResponse resp, Exception e) throws IOException {
		LoggerFactory.getLogger(BasicXMLServlet.class).warn("Servlet Exception", e);
        String message = e.getMessage();
        if(message == null) message = "";
        serializer(resp, "ERROR")
                .attribute(null, "code", "-100")
                .attribute(null, "description", message)
				.attribute(null, "control", "retry")
				.endDocument();
    }

	public static XmlSerializer serializer(HttpServletResponse resp, String result) throws IOException {
        XmlSerializer serializer = new MXSerializer();
        serializer.setOutput(resp.getWriter());
        return serializer.startTag(null, "resp").attribute(null, "result", result);
    }

    protected static void writeError(HttpServletResponse resp, int code, String description, int control)
			throws IOException
	{
        serializer(resp, "ERROR")
                .attribute(null, "code", Integer.toString(code))
				.attribute(null, "description", description)
                .attribute(null, "control", Integer.toString(control))
				.endDocument();
    }

    public static String getRequiredParameter(HttpServletRequest req, String name) throws ServletException {
        String ret = req.getParameter(name);
        if(ret == null) {
			throw new ServletException("Parameter missed: " + name);
		}
        return ret;
    }

	public static void writeDbQueryResult(final HttpServletRequest req, HttpServletResponse resp,
										  final String tagName, final XMLQuery query)
			throws IOException
	{
        final XmlSerializer serializer = serializer(resp, "ok");

		try {
			DbUtil.doWork(new DbWorker<Void>() {
				public Void work(DbService service) throws SQLException {
					try {
						serializer.startTag(null, tagName);
						query.process(serializer, service.getConnection(), new RequestContext(req));
					} catch (IOException e) {
						throw new SQLException(e.getMessage());
					}
					return null;
				}
			});
		} catch (SQLException e) {
			serializer.startTag(null, "ERROR").attribute(null, "type", e.getClass().getName()).text(e.getMessage());
			LoggerFactory.getLogger(BasicXMLServlet.class).warn("Writing Db Query", e);
		}

		serializer.endDocument();
	}

	public static void writeMultiDbQueryResult(final HttpServletRequest req, HttpServletResponse resp,
											   final HashMap<String, ? extends XMLQuery> queryMap)
			throws IOException
	{
		final XmlSerializer serializer = serializer(resp, "ok");

		try {
			DbUtil.doWork(new DbWorker<Void>() {
				public Void work(DbService service) throws SQLException {
					try {
						for (String s : queryMap.keySet()) {
							serializer.startTag(null, s);
							queryMap.get(s).process(serializer, service.getConnection(), new RequestContext(req));
							serializer.endTag(null, s);
						}
					} catch (IOException e) {
                        String message = e.getMessage();
                        if(message == null) message = "";
						throw new SQLException(message);
					}
					return null;
				}
			});
		} catch (SQLException e) {
			serializer.startTag(null, "ERROR").attribute(null, "type", e.getClass().getName()).text(e.getMessage());
			LoggerFactory.getLogger(BasicXMLServlet.class).warn("Writing Multi Db Query", e);
		}

		serializer.endDocument();
	}

	protected static void writeOk(HttpServletResponse resp) throws IOException {
		serializer(resp, "ok").endDocument();
	}

	protected static void writeXML(HttpServletResponse resp, Document doc) throws IOException {
		XMLTools.saveXML(doc, resp.getWriter());
	}
}
