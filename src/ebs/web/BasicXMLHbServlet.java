package ebs.web;

import ebs.hb.HbUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksei
 * Date: Feb 6, 2008
 * Time: 1:16:58 AM
 * Company: EBS (c) 2007
 */
public abstract class BasicXMLHbServlet extends BasicXMLServlet {
	protected abstract void handleRequest(HttpServletRequest req, HttpServletResponse resp, Session session)
			throws ServletException, IOException, SQLException;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("text/xml");
		resp.setCharacterEncoding("UTF-8");

		req.setCharacterEncoding("UTF-8");

		try {
			handleRequest(req, resp, HbUtil.getSessionWithTransaction());
			HbUtil.commit();
		} catch (Exception e) {
			HbUtil.rollback();
			writeFailure(resp, e);
		} finally {
			HbUtil.closeSession();
		}
	}

	public final void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
}
