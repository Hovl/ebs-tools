package ebs.locale;

/**
 * Created by Lebedev Andrey
 * Date: 3/14/12
 * Time: 2:14 AM
 */
public class CurrencyData {
	String currencyPrefix;
	String currencyCode;
	String currencySuffix;

	public CurrencyData() {
	}

	public CurrencyData(String currencyPrefix, String currencyCode, String currencySuffix) {
		this.currencyPrefix = currencyPrefix;
		this.currencyCode = currencyCode;
		this.currencySuffix = currencySuffix;
	}

	public String getCurrencyPrefix() {
		return currencyPrefix;
	}

	public void setCurrencyPrefix(String currencyPrefix) {
		this.currencyPrefix = currencyPrefix;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencySuffix() {
		return currencySuffix;
	}

	public void setCurrencySuffix(String currencySuffix) {
		this.currencySuffix = currencySuffix;
	}


	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("CurrencyData");
		sb.append("{currencyPrefix='").append(currencyPrefix).append('\'');
		sb.append(", currencyCode='").append(currencyCode).append('\'');
		sb.append(", currencySuffix='").append(currencySuffix).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
