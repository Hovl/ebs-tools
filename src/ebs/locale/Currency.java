package ebs.locale;

import org.joda.money.CurrencyUnit;

import java.util.*;

/**
 * Created by Dubov Aleksey
 * Date: Jul 15, 2009
 * Time: 7:51:57 PM
 * Company: EBS (c) 2009-2012
 */

public enum Currency {
	RUB("руб", "руб", Nominals.rubBanknotesNominals, Nominals.rubCoinsNominals),
	EUR("€", "", Nominals.eurBanknotesNominals, Nominals.eurCoinsNominals),
	USD("$", "", Nominals.usdBanknotesNominals, Nominals.usdCoinsNominals),
	CNY("¥", "", Nominals.cnyBanknotesNominals, Nominals.cnyCoinsNominals),
	LVL("Ls", "", Nominals.lvlBanknotesNominals, Nominals.lvlCoinsNominals),
	JPY("¥", "", Nominals.jpyBanknotesNominals, Nominals.jpyCoinsNominals),
	RON("RON", "RON", Nominals.ronBanknotesNominals, Nominals.ronCoinsNominals),
	CZK("Kč", "Kč", Nominals.czkBanknotesNominals, Nominals.czkCoinsNominals),
	MXN("$", "", Nominals.mxnBanknotesNominals, Nominals.mxnCoinsNominals),
	CAD("$", "", Nominals.cadBanknotesNominals, Nominals.cadCoinsNominals),
	ZAR("R", "", Nominals.zarBanknotesNominals, Nominals.zarCoinsNominals),
	NZD("$", "", Nominals.nzdBanknotesNominals, Nominals.nzdCoinsNominals),
	AUD("$", "", Nominals.audBanknotesNominals, Nominals.audCoinsNominals),
	GBP("£", "", Nominals.gbpBanknotesNominals, Nominals.gbpCoinsNominals),
	ILS("₪", "", Nominals.ilsBanknotesNominals, Nominals.ilsCoinsNominals),
	NOK("kr", "kr", Nominals.nokBanknotesNominals, Nominals.nokCoinsNominals),
	CHF("fr.", "fr.", Nominals.chfBanknotesNominals, Nominals.chfCoinsNominals),
	INR("₹", "", Nominals.inrBanknotesNominals, Nominals.inrCoinsNominals),
	THB("฿", "", Nominals.thbBanknotesNominals, Nominals.thbCoinsNominals),
	IDR("Rp", "Rp", Nominals.idrBanknotesNominals, Nominals.idrCoinsNominals),
	TRY("TL", "TL", Nominals.tryBanknotesNominals, Nominals.tryCoinsNominals),
	SGD("$", "", Nominals.sgdBanknotesNominals, Nominals.sgdCoinsNominals),
	HKD("$", "", Nominals.hkdBanknotesNominals, Nominals.hkdCoinsNominals),
	LTL("Lt", "Lt", Nominals.ltlBanknotesNominals, Nominals.ltlCoinsNominals),
	HRK("kn", "kn", Nominals.hrkBanknotesNominals, Nominals.hrkCoinsNominals),
	DKK("kr", "kr", Nominals.dkkBanknotesNominals, Nominals.dkkCoinsNominals),
	MYR("RM", "", Nominals.myrBanknotesNominals, Nominals.myrCoinsNominals),
	SEK("kr", "kr", Nominals.sekBanknotesNominals, Nominals.sekCoinsNominals),
	BRL("R$", "", Nominals.brlBanknotesNominals, Nominals.brlCoinsNominals),
	BGN("лв", "лв", Nominals.bgnBanknotesNominals, Nominals.bgnCoinsNominals),
	PHP("₱", "", Nominals.phpBanknotesNominals, Nominals.phpCoinsNominals),
	HUF("Ft", "Ft", Nominals.hufBanknotesNominals, Nominals.hufCoinsNominals),
	PLN("zł", "zł", Nominals.plnBanknotesNominals, Nominals.plnCoinsNominals),
	KRW("₩", "", Nominals.krwBanknotesNominals, Nominals.krwCoinsNominals);

	private static final Map<String, Currency> signsCurrenciesMap = new HashMap<String, Currency>(Currency.values().length) {
		{
			Currency[] currencies = Currency.values();
			for (Currency currency : currencies) {
				put(currency.getSign(), currency);
			}
		}
	};

	private static final List<String> currencyCodes = new ArrayList<String>() {
		{
            Currency[] currencies = Currency.values();
            for (Currency currency : currencies) {
          			add(currency.toString());
          		}
		}
	};

	private static final Collection<CurrencyData> currencyDataCollection = new ArrayList<CurrencyData>(values()
			.length) {
		{
			Currency[] currencies = Currency.values();
			for (Currency currency : currencies) {
				add(currency.getCurrencyData());
			}
		}
	};

	private String sign;
	private CurrencyData currencyData;
	private int[] banknotesNominals;
	private float[] coinsNominals;

	private Currency(String sign) {
		this.sign = sign;
		this.currencyData = new CurrencyData(sign, this.toString(), "");
	}

	private Currency(String sign, String suffix) {
		this.sign = sign;
		this.currencyData = suffix.isEmpty() ? new CurrencyData(sign, this.toString(), "") : new CurrencyData("",
				this.toString(), suffix);
	}

	private Currency(String sign, String suffix, int[] banknotesNominals, float[] coinsNominals) {
		this(sign, suffix);
		this.banknotesNominals = banknotesNominals;
		this.coinsNominals = coinsNominals;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public CurrencyData getCurrencyData() {
		return currencyData;
	}

	public void setCurrencyData(CurrencyData currencyData) {
		this.currencyData = currencyData;
	}

	public int[] getBanknotesNominals() {
		return banknotesNominals;
	}

	public void setBanknotesNominals(int[] banknotesNominals) {
		this.banknotesNominals = banknotesNominals;
	}

	public float[] getCoinsNominals() {
		return coinsNominals;
	}

	public void setCoinsNominals(float[] coinsNominals) {
		this.coinsNominals = coinsNominals;
	}

	public String[] getBanknotesNominalsAsStrings() {
		int[] banknotesInt = banknotesNominals;
		String[] banknotesStr = new String[banknotesInt.length];
		for (int i = 0; i < banknotesInt.length; i++) {
			banknotesStr[i] = Integer.toString(banknotesInt[i]);
		}
		return banknotesStr;
	}

	public String[] getCoinsNominalsAsStrings() {
		float[] coinsInt = coinsNominals;
		String[] coinsStr = new String[coinsInt.length];
		for (int i = 0; i < coinsInt.length; i++) {
			coinsStr[i] = Float.toString(coinsInt[i]);
		}
		return coinsStr;
	}

	public static int[] getBanknotesNominalsBySign(String sign) {
		return getCurrencyBySign(sign).banknotesNominals;
	}

	public static float[] getCoinsNominalsBySign(String sign) {
		return getCurrencyBySign(sign).coinsNominals;
	}

	public static String getSign(String currency) {
		return getCurrency(currency) != null ? getCurrency(currency).sign : null;
	}

	public static Currency getCurrencyBySign(String sign) {
		return signsCurrenciesMap.get(sign);
	}

	public static Currency getCurrency(String currency) {
		try {
			return Currency.valueOf(currency);
		} catch (IllegalArgumentException e) {
			return null;
		} catch (NullPointerException e) {
			return null;
		}
	}

	public static Currency getCurrency(Integer currencyID) {
		try {
			return values()[currencyID];
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public static String getCurrencyCode(Integer currencyID) {
		Currency currency = getCurrency(currencyID);
		if(currency != null) {
			return currency.toString();
		}
		return null;
	}

	public static Currency getDefaultCurrency() {
		return EUR;
	}

	public static String getCurrencyCodeByCountry(String country) {
		try {
			return CurrencyUnit.ofCountry(country).getCurrencyCode();
		} catch (Exception e) {
			//Catching IllegalCurrency and NullPointerException
			return null;
		}
	}

	public static Currency getCurrencyByCountry(String country) {
		return Currency.getCurrency(getCurrencyCodeByCountry(country));
	}

	public static List<String> getCurrencies() {
		return currencyCodes;
	}

	public static Collection<CurrencyData> getCurrenciesData() {
		return currencyDataCollection;
	}

	public static CurrencyData getCurrencyData(String currencyCode) {
		return Currency.getCurrency(currencyCode) != null ?
				Currency.valueOf(currencyCode).currencyData : null;
	}
}
