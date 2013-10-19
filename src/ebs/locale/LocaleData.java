package ebs.locale;

/**
 * Created by Dubov Aleksey
 * Date: Jul 15, 2009
 * Time: 3:51:37 AM
 * Company: EBS (c) 2009
 */

public enum LocaleData {
	en("d.", "h.", "m.", "s."),
	ru("д.", "ч.", "м.", "с."),
	de("T.", "S.", "M.", "s.");

	private String day;
	private String hour;
	private String minute;
	private String second;

	LocaleData(String day, String hour, String minute, String second) {
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}

	public String getDay() {
		return day;
	}

	public String getHour() {
		return hour;
	}

	public String getMinute() {
		return minute;
	}

	public String getSecond() {
		return second;
	}

	public static LocaleData getDefaultLocale() {
		return en;
	}
}
