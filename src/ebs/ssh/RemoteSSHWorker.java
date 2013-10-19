package ebs.ssh;

import java.io.IOException;

/**
 * Created by Aleksey Dubov.
 * Date: 7/2/11
 * Time: 4:45 PM
 * Copyright (c) 2011
 */
public abstract class RemoteSSHWorker<X> {
	public abstract X doWork(RemoteSSHSCPFileClient client) throws IOException;
}
