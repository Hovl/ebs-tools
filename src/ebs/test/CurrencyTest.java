package ebs.test;

import ebs.locale.Currency;
import ebs.locale.CurrencyData;
import ebs.tools.CurrencyConverter;
import junit.framework.TestCase;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by Lebedev Andrey
 * Date: 2/29/12
 * Time: 11:59 PM
 */
public class CurrencyTest extends TestCase {

    public static final HashMap<String, String> CURRENCY_DATA;

    static {
        CURRENCY_DATA = new HashMap<String, String>(5);
        CURRENCY_DATA.put("RU", "RUB");
        CURRENCY_DATA.put("DE", "EUR");
        CURRENCY_DATA.put("GB", "GBP");
        CURRENCY_DATA.put("--", null);
        CURRENCY_DATA.put(null, null);
    }

    public void testGetCurrencyCodeByCountry() throws IOException {
        for (String country : CURRENCY_DATA.keySet()){
            assertEquals(CURRENCY_DATA.get(country), Currency.getCurrencyCodeByCountry(country));
        }

    }
    
    public void testGetCurrencyData() throws IOException, ParseException {
        CurrencyData currencyData;
        for (String currency : CurrencyConverter.getInstance().getCurrencies()){
            currencyData = Currency.getCurrencyData(currency);
            assertNotNull(currencyData);
            assertEquals(currencyData.getCurrencyPrefix(),
                    Currency.valueOf(currency).getCurrencyData().getCurrencyPrefix());
            assertEquals(currencyData.getCurrencyCode(), currency);
            assertSame(currencyData.getCurrencySuffix(),
                    Currency.getCurrency(currency).getCurrencyData().getCurrencySuffix());
            assertNotNull(currencyData.getCurrencySuffix());
        }
        assertNull(Currency.getCurrencyData("фыв"));
        assertNull(Currency.getCurrencyData(null));
    }

    public void testGetCurrenciesData() throws IOException, ParseException {
        Collection<CurrencyData> currenciesData = Currency.getCurrenciesData();
        String[] currencies = (CurrencyConverter.getInstance().getCurrencies());
        List<String> currencyList = new ArrayList(Arrays.asList(currencies));

        for (CurrencyData currencyData : currenciesData) {
            assertTrue(currencyList.contains(currencyData.getCurrencyCode()) );
            assertTrue(currencyList.remove(currencyData.getCurrencyCode()));
        }

        assertTrue("Seems like some unsupported currencies appeared: "
                + currencyList.toString(),
                currencyList.isEmpty());
    }
}
