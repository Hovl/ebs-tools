package ebs.web;

import ebs.ssh.FileSystemDirectory;
import net.schmizz.sshj.xfer.FileSystemFile;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by Aleksey Dubov.
 * Date: 10/26/11
 * Time: 3:55 PM
 * Copyright (c) 2011
 */
public class URLWorker {
	private static Logger logger = LoggerFactory.getLogger(URLWorker.class);

	private boolean https;
	private String certPath;
	private String certLocalPath;
	private HttpClient client = null;

	public URLWorker() {
		this.https = false;
		certPath = "";
		certLocalPath = "";
	}

	public URLWorker(String certPath, String certLocalPath) {
		this.https = true;
		this.certPath = certPath;
		this.certLocalPath = certLocalPath;
	}

	public String executeRequest(String url, boolean post) {
		return executeRequest(url, post, null);
	}

	public String executeRequest(String url, boolean post, Map<String, String> params) {
		init();

		HttpMethodBase method = post ? new PostMethod() : new GetMethod();
		try {
			method.setURI(new URI(url, false));
			if(params != null && post) {
				Set<Map.Entry<String, String>> entrySet = params.entrySet();
				for (Map.Entry<String, String> paramData : entrySet) {
					((PostMethod) method).setParameter(paramData.getKey(), paramData.getValue());
				}
			}
			client.executeMethod(method);

			return method.getResponseBodyAsString();
		} catch (HttpException e) {
			printErrorAndReleaseConnection(method, e);
		} catch (IOException e) {
			printErrorAndReleaseConnection(method, e);
		} finally {
			method.releaseConnection();
		}
		return null;
	}

	public static void printErrorAndReleaseConnection(HttpMethodBase method, Exception e) {
		printErrorAndReleaseConnection(method, e, null);
	}

	public static void printErrorAndReleaseConnection(HttpMethodBase method, Exception e, String answer) {
		logger.warn(new StringBuilder("Can not execute ").append(method.getQueryString()).append(" answer: ").append
				(answer).toString(), e);
	}

	public void init() {
		if (client == null) {
			client = new HttpClient(new MultiThreadedHttpConnectionManager());
			HttpClientParams params = client.getParams();
			params.setSoTimeout(300000);
			params.setConnectionManagerTimeout(300000L);
			client.setParams(params);

			if (https) {
				FileSystemDirectory certDirectory = new FileSystemDirectory(certLocalPath, ".cert");
				if (certDirectory.getFile().exists()) {
					try {
						boolean noCerts = true;

						Iterable<FileSystemFile> certs = certDirectory.getChildren(null);
						for (FileSystemFile cert : certs) {
							File localCertFile = new File(certPath, cert.getFile().getName());
							if (!localCertFile.exists()) {
								FileUtils.copyFile(cert.getFile(), localCertFile);
							}
							noCerts = false;
						}

						if (noCerts) {
							throw new RuntimeException("No certificate found for minotaurus server!");
						}
					} catch (IOException e) {
						throw new RuntimeException("Cannot copy certificate for minotaurus server!", e);
					}
				}

				Protocol.registerProtocol("https", new Protocol("https", new CertSSLSocketFactory(certPath), 443));
			}
		}
	}
}
