package ebs.test;

import ebs.tools.CurrencyConverter;
import junit.framework.TestCase;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;

/**
 * Created by Lebedev Andrey
 * Date: 3/7/12
 * Time: 12:10 AM
 */
public class CurrencyConverterTest extends TestCase {
    
    public void testConvert() throws IOException, ParseException {
        Float value = 29.95F;
        Float cuteValue = 29.99F;
        Float exchangedValue;
        String fromCurrency = "RUB";

        String[] currencies = CurrencyConverter.getInstance().getCurrencies();
        System.out.println(Arrays.toString(CurrencyConverter.getInstance().getCurrencies()));

        for (String toCurrency : currencies) {
            exchangedValue = CurrencyConverter.getInstance().convert(value, fromCurrency, toCurrency, true);
            System.out.println("Converting " + value + " from " + fromCurrency + " to " + toCurrency
                    + ": " + exchangedValue);
        }

        assertNull(CurrencyConverter.getInstance().convert(null, fromCurrency, fromCurrency));
        assertNull(CurrencyConverter.getInstance().convert(value, null, fromCurrency));
        assertEquals(CurrencyConverter.getInstance().convert(0, fromCurrency, currencies[0]), 0);
        assertEquals(CurrencyConverter.getInstance().convert(value, fromCurrency, fromCurrency), value);
        assertEquals(CurrencyConverter.getInstance().convert(value, fromCurrency, fromCurrency, true), cuteValue);
    }
}
