package ebs.test;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Dubov Aleksey
 * Date: Jun 29, 2010
 * Time: 4:21:45 PM
 * Company: EBS (c) 2009
 */

public class TimeZoneDataTest extends TestCase {
	public void testShowAllTimeZones() throws Exception {
		String[] allTZs = TimeZone.getAvailableIDs();

		Arrays.sort(allTZs);

		List<String> TZs = new ArrayList<String>(allTZs.length);

		for(String tz : allTZs) {
			if(tz.contains("/") && !tz.contains("Etc") && !tz.contains("GMT") && !tz.contains("SystemV")) {
				TZs.add(tz);
			}
		}

//		System.out.println(TZs.toString().replaceAll(", ", "\n"));

		for(String tz : TZs) {
			TimeZone timeZone = TimeZone.getTimeZone(tz);
			System.out.println(timeZone.getID() + " " + timeZone.getRawOffset());
		}
	}
}
