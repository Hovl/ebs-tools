package ebs.test;

import ebs.tools.FileTools;
import junit.framework.TestCase;

import java.io.File;

/**
 * Created by Dubov Aleksey
 * Date: Feb 12, 2010
 * Time: 12:16:11 AM
 * Company: EBS (c) 2009
 */

public class FileToolsTest extends TestCase {
	public void testWriteFileSpeed() throws Exception {
		long[] speeds = new long[10];

		String destFolderName = "fileToolsTest";
		File destFolder = new File(destFolderName);
		boolean created = destFolder.exists();
		if(!created) {
			created = destFolder.mkdirs();
		}

		assertTrue("destFolder can not be created", created);

		for(int i = 0; i < 10; i++) {
			speeds[i] = FileTools.getWriteFileSpeed(destFolderName, "writefilespeedtest.dat");
			System.out.println(speeds[i]);
			Thread.yield();
		}

		long avg = 0;
		for(int i = 0; i < 10; i++) {
			assertNotSame(new StringBuilder("speed test #").append(i).append(" failed").toString(), speeds[i], -1);
			avg += speeds[i];
		}

		System.out.println("avg speed = " + (avg / 10) + " - " + (avg / (10 * 1024f * 1024f)) + "MBps");
	}
}
