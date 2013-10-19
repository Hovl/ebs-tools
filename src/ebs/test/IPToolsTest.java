package ebs.test;

import ebs.tools.IPTools;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Lebedev Andrey
 * Date: 2/10/12
 * Time: 4:04 PM
 */
public class IPToolsTest extends TestCase {

    public static final HashMap<String, String[]> IP_DATA;

    static {
        IP_DATA = new HashMap<String, String[]>(10);
        IP_DATA.put("213.180.193.3", new String[]{"Russian Federation", "RU"});
        IP_DATA.put("178.0.118.178", new String[]{"Germany", "DE"});
        IP_DATA.put("209.85.173.104", new String[]{"United States", "US"});
        IP_DATA.put("mail.ru", new String[]{"Russian Federation", "RU"});
        IP_DATA.put("google.com", new String[]{"United States", "US"});
        IP_DATA.put("fhn3b49fba", new String[]{"N/A", "--"});
        IP_DATA.put(null, new String[]{"N/A", "--"});
    }

    public void testGetCountry() throws IOException {

        Set<String> ipData = IP_DATA.keySet();

        for (String ip : ipData){
            assertEquals(IP_DATA.get(ip)[0], IPTools.getCountryName(ip));
            assertEquals(IP_DATA.get(ip)[1], IPTools.getCountryCode(ip));
        }

    }
}
