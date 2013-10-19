package ebs.web;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import ebs.hb.HbUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksey
 * Date: Oct 19, 2009
 * Time: 9:54:32 PM
 * Company: EBS (c) 2009-2011
 */

public abstract class BasicJSONHbServlet extends BasicJSONServlet {
	protected abstract JsonElement handleRequest(HttpServletRequest req, HttpServletResponse resp, Session session)
			throws ServletException, IOException, SQLException;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");

		req.setCharacterEncoding("UTF-8");

		boolean responseSent = false;
		try {
			JsonElement result = handleRequest(req, resp, HbUtil.getSessionWithTransaction());
			if(result != null) {
				new GsonBuilder().disableHtmlEscaping().create().toJson(result, resp.getWriter());
				responseSent = true;
			}

			HbUtil.commit();
		} catch (Exception e) {
			HbUtil.rollback();

			if(!responseSent) {
				new GsonBuilder().disableHtmlEscaping().create().toJson(writeFailure(e), resp.getWriter());
			}
		} finally {
			HbUtil.closeSession();
		}
	}

	public final void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
}
