package ebs.web;

import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * Created by Aleksey Dubov.
 * Date: 10/26/11
 * Time: 4:06 PM
 * Copyright (c) 2011
 */
public class CertSSLSocketFactory implements ProtocolSocketFactory {
	private SSLSocketFactory factory;

	public CertSSLSocketFactory(String path) {
		factory = HttpsURLConnection.getDefaultSSLSocketFactory();

		File dir = new File(path);
		if (!dir.isDirectory()) {
			LoggerFactory.getLogger(CertSSLSocketFactory.class).warn("Cert folder does not exist!");
			return;
		}

		File[] files = dir.listFiles();
		if (files.length == 0) {
			LoggerFactory.getLogger(CertSSLSocketFactory.class).warn("Cert folder is empty!");
			return;
		}

		try {
			KeyStore store = KeyStore.getInstance("JKS");
			store.load(null, new char[]{});

			for (File file : files) {
				try {
					Certificate certificate =
							CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(file));
					store.setCertificateEntry(file.getName(), certificate);
				} catch (Exception e) {
					LoggerFactory.getLogger(CertSSLSocketFactory.class)
							.warn("Can not load certificate from cert folder!", e);
					return;
				}
			}

			SSLContext context = SSLContext.getInstance("SSL");

			TrustManagerFactory trustManagerFactory =
					TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(store);

			context.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

			factory = context.getSocketFactory();
		} catch (IOException e) {
			LoggerFactory.getLogger(CertSSLSocketFactory.class).warn("Can not create certificate!", e);
		} catch (GeneralSecurityException e) {
			LoggerFactory.getLogger(CertSSLSocketFactory.class).warn("Can not create certificate!", e);
		}
	}

	public Socket createSocket(String host, int port, InetAddress inetAddress, int inetPort) throws IOException {
		return factory == null ? null : factory.createSocket(host, port, inetAddress, inetPort);
	}

	public Socket createSocket(String host, int port, InetAddress inetAddress, int inetPort,
							   HttpConnectionParams httpConnectionParams)
			throws IOException {
		return factory == null ? null : factory.createSocket(host, port, inetAddress, inetPort);
	}

	public Socket createSocket(String host, int port) throws IOException {
		return factory == null ? null : factory.createSocket(host, port);
	}
}
