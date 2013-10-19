package ebs.ssh;

import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.userauth.keyprovider.OpenSSHKeyFile;
import net.schmizz.sshj.xfer.FileSystemFile;
import net.schmizz.sshj.xfer.scp.SCPDownloadClient;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Aleksey Dubov.
 * Date: 2011-04-28
 * Time: 20:04
 * Copyright (c) 2011
 */
public class RemoteSSHSCPFileClient {
	private static Logger logger = LoggerFactory.getLogger(RemoteSSHSCPFileClient.class);

	private static OpenSSHKeyFile keyProvider;
	private static HashMap<String, RemoteSSHSCPFileClient> remoteSSHSCPFileClientsHashMap = new HashMap<String, RemoteSSHSCPFileClient>(10);

	private String host;
	private String userName;
	private SSHClient sshClient;

	static {
		keyProvider = new OpenSSHKeyFile();
		keyProvider.init(new File(new File(System.getProperty("user.home"), ".ssh"), "id_rsa"));
	}

	public static RemoteSSHSCPFileClient getRemoteSSHSCPFileClient(String host, String userName) throws IOException {
		RemoteSSHSCPFileClient client;

		String clientID = new StringBuilder(host).append(userName).toString();
		if(!remoteSSHSCPFileClientsHashMap.containsKey(clientID)) {
			client = new RemoteSSHSCPFileClient(host, userName);
			remoteSSHSCPFileClientsHashMap.put(clientID, client);
		} else {
			client = remoteSSHSCPFileClientsHashMap.get(clientID);
		}

		if(!client.sshClient.isConnected()) {
			client.sshClient.connect(host);
			if(!client.sshClient.isConnected()) {
				throw new IOException("Cannot connect to" + client.sshClient.getRemoteHostname());
			}
		}

		if(!client.sshClient.isAuthenticated()) {
			client.sshClient.authPublickey(userName, keyProvider);
			if(!client.sshClient.isAuthenticated()) {
				throw new IOException("Cannot auth to " + client.sshClient.getRemoteHostname() + " as " + userName);
			}
		}

		return client;
	}

	public RemoteSSHSCPFileClient(String host, String userName) throws IOException {
		this.host = host;
		this.userName = userName;

		sshClient = new SSHClient();
		sshClient.loadKnownHosts();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RemoteSSHSCPFileClient that = (RemoteSSHSCPFileClient) o;

		return host.equals(that.host) && userName.equals(that.userName);
	}

	@Override
	public int hashCode() {
		int result = host.hashCode();
		result = 31 * result + userName.hashCode();
		return result;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		close();
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("RemoteSSHSCPFileClient");
		sb.append("{host='").append(host).append('\'');
		sb.append(", userName='").append(userName).append('\'');
		sb.append('}');
		return sb.toString();
	}


	public String execute(String cmd) throws IOException {
		Session session = sshClient.startSession();
		try {
			Session.Command command = session.exec(cmd);
			command.join();
			return IOUtils.toString(command.getInputStream());
		} finally {
			session.close();
		}
	}

	public boolean delete(String path, boolean recursive) throws TransportException, ConnectionException {
		Session session = sshClient.startSession();
		try {
			Session.Command cmd = session.exec("rm " + (recursive ? "-r" : "") + ' ' + path);
			cmd.join();
			return cmd.getExitStatus() == 0;
		} finally {
			session.close();
		}
	}

	public boolean exists(String path, boolean folder) throws IOException {
		Session session = sshClient.startSession();
		try {
			Session.Command cmd = session.exec("stat -c %s \"" + path + '\"');
			cmd.join();
			return !IOUtils.toString(cmd.getErrorStream()).contains(path);
		} finally {
			session.close();
		}
	}

	public boolean copy(String path, String toPath, boolean recursive) throws IOException {
		FileSystemFile file = new FileSystemFile(toPath);
		if(!file.getFile().exists()) {
			file.getFile().mkdirs();
		}

		SCPDownloadClient client = sshClient.newSCPFileTransfer().newSCPDownloadClient();
		client.setRecursiveMode(recursive);
		return client.copy(path, file) == 0;
	}

	public boolean upload(String path, String toPath, String filter) throws IOException {
		FileSystemFile file;
		if(filter != null) {
			file = new FileSystemDirectory(
					FilenameUtils.getFullPath(path),
					filter);
		} else {
			file = new FileSystemFile(path);
		}
		return sshClient.newSCPFileTransfer().newSCPUploadClient().copy(file, toPath) == 0;
	}

	public void close() throws IOException {
		if(sshClient.isConnected()) {
			sshClient.disconnect();
		}
	}
}
