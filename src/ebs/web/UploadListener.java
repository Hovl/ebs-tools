package ebs.web;

import com.codemagi.servlets.upload.util.OutputStreamListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dubov Aleksey
 * Date: Oct 22, 2009
 * Time: 3:03:08 PM
 * Company: EBS (c) 2009-2011
 */

public class UploadListener implements OutputStreamListener {
	private static Map<String, UploadListener> listeners = new HashMap<String, UploadListener>(1000);

	private String id;
	private Long fileSize;
	private volatile Long fileRead;

	public UploadListener(String id, long fileSize) {
		this.id = id;
		this.fileSize = fileSize;
		this.fileRead = 0l;
	}

	public void start() {
		listeners.put(id, this);
	}

	public void bytesRead(int i) {
		fileRead += i;
	}

	public void error(String s) {
		listeners.remove(id);
	}

	public void done() {
		listeners.remove(id);
	}

	public String getId() {
		return id;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public Long getFileRead() {
		return fileRead;
	}

	public int getProgress() {
		return (int) ((fileRead * 100) / fileSize);
	}

	public static UploadListener getListener(String id) {
		return listeners.get(id);
	}

	public static Integer getProgress(String id) {
		UploadListener listener = getListener(id);
		if(listener == null) {
			return null;
		}
		return listener.getProgress();
	}

	public static Long getFileRead(String id) {
		UploadListener listener = getListener(id);
		if(listener == null) {
			return null;
		}
		return listener.fileRead;
	}

	public static Long getFileSize(String id) {
		UploadListener listener = getListener(id);
		if(listener == null) {
			return null;
		}
		return listener.fileSize;
	}

	public static String getProgress(String id, String div) {
		UploadListener listener = getListener(id);
		if(listener == null) {
			return null;
		}
		return listener.fileSize + div + listener.fileRead;
	}
}
