package ebs.web;

import ebs.tools.FileTools;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Dubov Aleksey
 * Date: Oct 22, 2009
 * Time: 3:53:32 PM
 * Company: EBS (c) 2009
 */

public abstract class GetFileServlet extends BasicXMLServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String fileName = getFileName(req);
		if(fileName != null) {
			resp.setHeader("Content-Disposition",
					new StringBuilder("attachment; filename=\"").append(fileName).append('\"').toString());
			resp.setContentType(FileTools.getMimeType(fileName));

			try {
				FileTools.copyFileToStream(getPath(req), resp.getOutputStream());
			} catch (IOException e) {
				onError(req, e);
				return;
			}

			onComplete(req);
		}
	}

	protected abstract String getFileName(HttpServletRequest req);

	protected abstract String getPath(HttpServletRequest req);

	protected abstract void onError(HttpServletRequest req, Exception e);

	protected abstract void onComplete(HttpServletRequest req);
}
