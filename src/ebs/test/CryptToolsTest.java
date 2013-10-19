package ebs.test;

import ebs.tools.CryptTools;
import junit.framework.TestCase;

/**
 * Created by Aleksey Dubov.
 * Date: 2011-02-08
 * Time: 03:56
 * Copyright (c) 2010
 */
public class CryptToolsTest extends TestCase {
	public void testCryptDecrypt() throws Exception {
		String data = "jshdbfhjdsvfghasvfky4t\rr763\t4q5\n8ou9[[	OLS';S";
		String key = "1736487326";

		String cryptedData = CryptTools.encrypt(data, key);
		assertEquals("Decrypted data is not equals to the original", data, CryptTools.decrypt(cryptedData, key));
	}
}
