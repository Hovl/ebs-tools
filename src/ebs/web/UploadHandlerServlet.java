package ebs.web;

import com.codemagi.servlets.upload.util.MonitoredDiskFileItemFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Dubov Aleksey
 * Date: Oct 22, 2009
 * Time: 3:11:43 PM
 * Company: EBS (c) 2009-2012
 */

public abstract class UploadHandlerServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			String id = getID(req);
			if(id == null) {
				onError(req, resp, null);
				return;
			}

			Long contentLength = (long) req.getContentLength();

			String contentLengthStr = req.getHeader("Content-Length");
			if(contentLengthStr != null) {
				try {
					contentLength = Long.parseLong(contentLengthStr);
				} catch (NumberFormatException e) {
					//do nothing :)
				}
			}

			UploadListener listener = new UploadListener(id, contentLength);
			req.setAttribute("LISTENER", listener);
			List list = new ServletFileUpload(new MonitoredDiskFileItemFactory(listener)).parseRequest(req);

			for(Object o : list) {
				FileItem item = (FileItem) o;
				if(!processFileItem(id, item)) {
					LoggerFactory.getLogger(UploadHandlerServlet.class).warn("Error saving file {}", item.getName());
					onError(req, resp, "Cannot save file " + item.getName());
					return;
				}
			}
		} catch (Exception e) {
			LoggerFactory.getLogger(UploadHandlerServlet.class).warn("File upload error", e);
			onError(req, resp, "Internal error" + e.getMessage());
			return;
		}

		onComplete(req, resp);
	}

	private boolean processFileItem(String id, FileItem item) throws Exception {
		File baseDir = getBaseDir(id);
		if(baseDir == null) {
			return false;
		}

		if(!baseDir.exists()) {
			baseDir.mkdirs();
		}

		String fileName = getFileName(item);
		if(fileName != null) {
			File file = new File(baseDir, fileName);
			if(file.exists()) {
				file.delete();
			}

			LoggerFactory.getLogger(UploadHandlerServlet.class).info("Saving file: " + file.getAbsolutePath());

			item.write(file);
		}

		return true;
	}

	protected abstract String getFileName(FileItem item);

	protected abstract String getID(HttpServletRequest req);

	protected abstract File getBaseDir(String id);

	protected abstract void onError(HttpServletRequest req, HttpServletResponse resp, String internalErrorMessage);

	protected abstract void onComplete(HttpServletRequest req, HttpServletResponse resp);
}
