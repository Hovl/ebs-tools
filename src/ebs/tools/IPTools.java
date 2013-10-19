package ebs.tools;

import com.maxmind.geoip.Country;
import com.maxmind.geoip.LookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * Created by Lebedev Andrey
 * Date: 2/8/12
 * Time: 9:24 PM
 */
public class IPTools {
    private final static String DEFAULT_GEO_IP_DAT_PATH = 
            '.' + File.separator + "data" + File.separator + "GeoIP.dat";

    private static Logger logger = LoggerFactory.getLogger(IPTools.class);
    private static LookupService lookupService = null;
    
    public static void initCountryData(String countryDataPath) throws IOException {
        if(lookupService != null) {
            return;
        }

        try {
            lookupService = new LookupService(countryDataPath);
        } catch (Exception e) {
            logger.warn("Given GeoIP.dat path is invalid {}, trying default {}. " +
                    "Call initCountryData() first next time.", countryDataPath,
                    DEFAULT_GEO_IP_DAT_PATH);
            lookupService = new LookupService(DEFAULT_GEO_IP_DAT_PATH);
        }
    }
    
    public static String getCountryName(String ip) throws IOException {
        return getCountry(ip).getName();
    }
    
    public static String getCountryCode(String ip) throws IOException {
        return getCountry(ip).getCode();
    }
    
    public static Country getCountry(String ip) throws IOException {
        initCountryData(null);

        return lookupService.getCountry(ip);
    }
}
