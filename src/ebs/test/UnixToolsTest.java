package ebs.test;

import ebs.tools.UnixTools;
import junit.framework.TestCase;

/**
 * Created by Aleksey Dubov.
 * Date: 2010-12-17
 * Time: 17:32
 * Copyright (c) 2010
 */
public class UnixToolsTest extends TestCase {
	public void testTooManyFilesOpened() throws Exception {
		for(int i = 0; i < 10000; i++) {
			testCommandOutput();
			System.out.println(new StringBuilder().append("count ").append(i).append(" - OK").toString());
		}
	}

	public void testCommandOutput() throws Exception {
		String result = UnixTools.getCommandOutput("ssh", "root@minotaurus.ru", "uptime");
		assertNotNull(result);
		System.out.println(result);
	}
}
