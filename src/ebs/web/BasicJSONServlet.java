package ebs.web;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import ebs.context.RequestContext;
import ebs.db.DbService;
import ebs.db.DbUtil;
import ebs.db.DbWorker;
import ebs.json.JSONQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dubov Aleksey
 * Date: Oct 13, 2009
 * Time: 4:39:28 PM
 * Company: EBS (c) 2009-2012
 */

public abstract class BasicJSONServlet extends HttpServlet {
	private static Logger logger = LoggerFactory.getLogger(BasicJSONServlet.class);

	public static void writeAnswer(HttpServletResponse resp, JsonElement result) {
		try {
			if (result != null) {
				JsonWriter jsonWriter = new JsonWriter(resp.getWriter());
				jsonWriter.setHtmlSafe(true);
				try {
					new GsonBuilder().disableHtmlEscaping().create().toJson(result, jsonWriter);
				} catch (Exception e) {
					new GsonBuilder().disableHtmlEscaping().create().toJson(writeFailure(e), jsonWriter);
				} finally {
					jsonWriter.close();
				}
			}
		} catch (IOException e) {
			logger.warn("Cannot respond to the request!", e);
		}
	}

	public static void prepareResponse(HttpServletResponse resp) {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
	}

	public static JsonElement writeFailure(Exception e) throws IOException {
		LoggerFactory.getLogger(BasicJSONServlet.class).warn("Servlet Exception", e);

		JsonObject object = new JsonObject();
		object.addProperty("result", "error");
		object.addProperty("code", "-100");
		object.addProperty("description", e.getLocalizedMessage());
		object.addProperty("control", "retry");
		return object;
	}

	public static JsonElement writeError(int code, String description, int control) throws IOException {
		JsonObject object = new JsonObject();
		object.addProperty("result", "error");
		object.addProperty("code", code);
		object.addProperty("description", description);
		object.addProperty("control", control);
		return object;
	}

	public static String getRequiredParameter(HttpServletRequest req, String name) throws ServletException {
		String ret = req.getParameter(name);
		if (ret == null) {
			throw new ServletException("Parameter missed: " + name);
		}
		return ret;
	}

	public static JsonElement writeOk() throws IOException {
		JsonObject object = new JsonObject();
		object.addProperty("result", "ok");
		return object;
	}

	public static JsonElement writeDbQueryResult(final HttpServletRequest req, final JSONQuery query)
			throws IOException {
		try {
			return DbUtil.doWork(new DbWorker<JsonElement>() {
				public JsonElement work(DbService service) throws SQLException {
					return query.process(service.getConnection(), new RequestContext(req));
				}
			});
		} catch (SQLException e) {
			LoggerFactory.getLogger(BasicJSONServlet.class).warn("Writing Db Query", e);
			return writeFailure(e);
		}
	}

	public static JsonElement writeMultiDbQueryResult(final HttpServletRequest req,
													  final Map<String, JSONQuery> queryMap) throws IOException {
		try {
			return DbUtil.doWork(new DbWorker<JsonElement>() {
				public JsonElement work(DbService service) throws SQLException {
					JsonArray array = new JsonArray();

					Set<Map.Entry<String, JSONQuery>> set = queryMap.entrySet();
					RequestContext context = new RequestContext(req);
					for (Map.Entry<String, JSONQuery> entry : set) {
						JsonObject object = new JsonObject();
						object.add(entry.getKey(), entry.getValue().process(service.getConnection(), context));
					}
					return array;
				}
			});
		} catch (SQLException e) {
			LoggerFactory.getLogger(BasicJSONServlet.class).warn("Writing Multi Db Query", e);
			return writeFailure(e);
		}
	}

	public static Integer getParameterAsInteger(HttpServletRequest req, String id) {
		return getParameterAsInteger(req, id, 0);
	}

	public static Integer getParameterAsInteger(HttpServletRequest req, String id, Integer defaultValue) {
		String idStr = req.getParameter(id);
		if(idStr != null) {
			try {
				return Integer.parseInt(idStr);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public static String getParameterAsString(HttpServletRequest req, String id, String defaultValue) {
		String idStr = req.getParameter(id);
		if(idStr != null) {
			try {
				idStr = URLDecoder.decode(idStr, "UTF-8");
				if(idStr != null) {
					return idStr;
				}
			} catch (UnsupportedEncodingException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public static String getParameterAsString(HttpServletRequest req, String id) {
		return getParameterAsString(req, id, null);
	}

	public static Boolean getParameterAsBoolean(HttpServletRequest req, String id) {
		String idStr = req.getParameter(id);
		if(idStr != null) {
			return Boolean.parseBoolean(idStr);
		}
		return null;
	}

	public static Boolean getParameterAsBoolean(HttpServletRequest req, String id, Boolean defaultValue) {
		String idStr = req.getParameter(id);
		if(idStr != null) {
			return Boolean.parseBoolean(idStr);
		}
		return defaultValue;
	}

	public static Float getParameterAsFloat(HttpServletRequest req, String id) {
		return getParameterAsFloat(req, id, null);
	}

	public static Float getParameterAsFloat(HttpServletRequest req, String id, Float defaultValue) {
		String idStr = req.getParameter(id);
		if(idStr != null) {
			try {
				return Float.parseFloat(idStr);
			} catch (NumberFormatException e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}
}
