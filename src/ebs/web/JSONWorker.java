package ebs.web;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Aleksey Dubov
 * Date: 4/13/12
 * Time: 1:37 PM
 * Copyright (c) 2012
 */
public class JSONWorker {
	protected static Gson gson = new Gson();

	public static JsonObject getErrorResult(String description) {
		JsonObject result = new JsonObject();
		result.addProperty("result", "error");
		result.addProperty("description", description);
		return result;
	}

	public static JsonObject getOkResult() {
		JsonObject result = new JsonObject();
		result.addProperty("result", "ok");
		return result;
	}
}
