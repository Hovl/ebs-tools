package ebs.web;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksey
 * Date: Oct 19, 2009
 * Time: 9:52:58 PM
 * Company: EBS (c) 2009-2011
 */

public abstract class BasicJSONDbServlet extends BasicJSONServlet {
	protected abstract JsonElement handleRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		prepareResponse(resp);

		JsonWriter jsonWriter = new JsonWriter(resp.getWriter());
		jsonWriter.setHtmlSafe(true);
		try {
			JsonElement result = handleRequest(req, resp);
			if (result != null) {
				new GsonBuilder().disableHtmlEscaping().create().toJson(result, resp.getWriter());
			}
		} catch (SQLException e) {
			new GsonBuilder().disableHtmlEscaping().create().toJson(writeFailure(e), resp.getWriter());
		} finally {
			jsonWriter.close();
		}
	}

	public final void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doGet(req, res);
	}
}
