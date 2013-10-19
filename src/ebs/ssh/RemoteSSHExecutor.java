package ebs.ssh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by Aleksey Dubov.
 * Date: 7/2/11
 * Time: 4:43 PM
 * Copyright (c) 2011
 */
public class RemoteSSHExecutor {
	private static Logger logger = LoggerFactory.getLogger(RemoteSSHExecutor.class);

	public static <X> X execute(String host, String userName, RemoteSSHWorker<X> worker) {
		try {
			return worker.doWork(RemoteSSHSCPFileClient.getRemoteSSHSCPFileClient(host, userName));
		} catch (IOException e) {
			logger.warn("Can't create ssh client", e);
		}
		return null;
	}

	public static <X> X executeUnsafe(String host, String userName, RemoteSSHWorker<X> worker) throws IOException {
		return worker.doWork(RemoteSSHSCPFileClient.getRemoteSSHSCPFileClient(host, userName));
	}
}
