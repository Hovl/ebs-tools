package ebs.tools;

import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

/**
 * Created by Dubov Aleksey
 * Date: Jul 12, 2009
 * Time: 9:25:32 PM
 * Company: EBS (c) 2009
 */

public class UnixTools {
	private static Semaphore semaphore = new Semaphore(500);

	public static String getUptimeAndLoad() {
		String result = getCommandOutput("uptime");
		return result == null ? "" : result;
	}
	public static String getCommandOutput(String... command) {
		semaphore.acquireUninterruptibly();

		Process process = null;
		try {
			process = Runtime.getRuntime().exec(command);
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				LoggerFactory.getLogger(UnixTools.class).warn("Process interrupted:" + Arrays.toString(command), e);
			}

			if(process.getErrorStream().available() == 0) {
				return FileTools.streamToString(process.getInputStream());
			} else {
				return FileTools.streamToString(process.getErrorStream());
			}
		} catch (IOException e) {
			LoggerFactory.getLogger(UnixTools.class).warn("Error executing command" + Arrays.toString(command), e);
		} finally {
			if(process != null) {
				process.destroy();
			}

			semaphore.release();
		}
		return null;
	}
}
