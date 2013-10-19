package ebs.context;

/**
 * Created by Dubov Aleksey
 * Date: Oct 12, 2007
 * Time: 8:35:49 PM
 * Company: EBS (c) 2007-2012
 */

import javax.servlet.http.HttpServletRequest;

public class RequestContext implements Context {
    private final HttpServletRequest request;

    public RequestContext(HttpServletRequest request) {
		this.request = request;
	}

    public String getArgument(String name) {
		return request.getParameter(name);
	}
}
