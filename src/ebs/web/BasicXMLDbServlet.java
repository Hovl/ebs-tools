package ebs.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksei
 * Date: Oct 12, 2007
 * Time: 5:39:42 PM
 * Company: EBS (c) 2007
 */

public abstract class BasicXMLDbServlet extends BasicXMLServlet {
	protected abstract void handleRequest(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, SQLException;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/xml");
        resp.setCharacterEncoding("UTF-8");
		try {
			handleRequest(req, resp);
		} catch (SQLException e) {
			writeFailure(resp, e);
		}
	}

	public final void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
}
