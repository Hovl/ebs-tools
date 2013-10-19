package ebs.locale;

import java.util.*;

/**
 * Created by Dubov Aleksey
 * Date: Jan 15, 2010
 * Time: 3:25:49 PM
 * Company: EBS (c) 2009-2011
 */

public class TimeZoneData {
	public static class TimeZoneInfo {
		private int id;
		private int hours;
		private String tzID;
		private String name;

		public TimeZoneInfo() {
		}

		public TimeZoneInfo(int id, int hours, String tzID, String name) {
			this.id = id;
			this.hours = hours;
			this.tzID = tzID;
			this.name = name;
		}

		public int getId() {
			return id;
		}

		public int getHours() {
			return hours;
		}

		public String getTzID() {
			return tzID;
		}

		public String getName() {
			return name;
		}
	}

	private static List<TimeZoneInfo> tzInfos = new ArrayList<TimeZoneInfo>(100);

	private static HashMap<Integer, String> tzIdNameMap = new HashMap<Integer, String>(100);
	private static HashMap<Integer, TimeZoneInfo> tzIdInfoMap = new HashMap<Integer, TimeZoneInfo>(100);
//	private static HashMap<Integer, TimeZoneInfo> tzHoursInfoMap = new HashMap<Integer, TimeZoneInfo>(100);
	private static HashMap<String, TimeZoneInfo> tzIdsInfoMap = new HashMap<String, TimeZoneInfo>(100);

	private static TimeZoneInfo defaultTimeZoneInfo;

	static {
		String[] allTZs = TimeZone.getAvailableIDs();
		Arrays.sort(allTZs);

		int counter = 1;
		for(String tz : allTZs) {
			if(tz.contains("/") && !tz.contains("Etc") && !tz.contains("GMT") && !tz.contains("SystemV")) {
				TimeZone timeZone = TimeZone.getTimeZone(tz);
				addTimeZoneInfo(new TimeZoneInfo(counter++, timeZone.getRawOffset()/(1000*60*60), tz, tz));
			}
		}

//		addTimeZoneInfo(new TimeZoneInfo(1, -12, "Etc/GMT+12", "(-12:00) International Date Line West"));
//		addTimeZoneInfo(new TimeZoneInfo(2, -11, "Etc/GMT+11", "(-11:00) Midway Island, Samoa"));
//		addTimeZoneInfo(new TimeZoneInfo(3, -10, "Etc/GMT+10", "(-10:00) Hawaii"));
//		addTimeZoneInfo(new TimeZoneInfo(4, -9, "Etc/GMT+9", "(-09:00) Alaska"));
//		addTimeZoneInfo(new TimeZoneInfo(5, -8, "Etc/GMT+8", "(-08:00) Pacific Time (US & Canada)"));
//		addTimeZoneInfo(new TimeZoneInfo(6, -7, "Etc/GMT+7", "(-07:00) Arizona, Mountain Time (US & Canada)"));
//		addTimeZoneInfo(new TimeZoneInfo(7, -6, "Etc/GMT+6", "(-06:00) Central America, Saskatchewan, Central Time (US & Canada), Guadalajara, Mexico city"));
//		addTimeZoneInfo(new TimeZoneInfo(8, -5, "Etc/GMT+5", "(-05:00) Indiana, Bogota, Lima, Quito, Rio Branco, Eastern time (US & Canada)"));
//		addTimeZoneInfo(new TimeZoneInfo(9, -4, "Etc/GMT+4", "(-04:00) Atlantic time (Canada), Manaus, Santiago, Caracas, La Paz"));
//		addTimeZoneInfo(new TimeZoneInfo(10, -3, "Etc/GMT+3", "(-03:00) Greenland, Brasilia, Montevideo, Buenos Aires, Georgetown"));
//		addTimeZoneInfo(new TimeZoneInfo(11, -2, "Etc/GMT+2", "(-02:00) Mid-Atlantic"));
//		addTimeZoneInfo(new TimeZoneInfo(12, -1, "Etc/GMT+1", "(-01:00) Azores, Cape Verde Is."));
//		addTimeZoneInfo(new TimeZoneInfo(13, 0, "Etc/GMT0", "(00:00) Casablanca, Monrovia, Reykjavik, Dublin, Edinburgh, Lisbon, London"));
//		addTimeZoneInfo(new TimeZoneInfo(14, 1, "Etc/GMT-1", "(+01:00) Amsterdam, Berlin, Rome, Vienna, Prague, Brussels, West Central Africa"));
//		addTimeZoneInfo(new TimeZoneInfo(15, 2, "Etc/GMT-2", "(+02:00) Amman, Athens, Istanbul, Beirut, Cairo, Jerusalem, Harare, Pretoria"));
//		addTimeZoneInfo(new TimeZoneInfo(16, 3, "Etc/GMT-3", "(+03:00) Baghdad, Moscow, St. Petersburg, Volgograd, Kuwait, Riyadh, Nairobi, Tbilisi"));
//		addTimeZoneInfo(new TimeZoneInfo(17, 4, "Etc/GMT-4", "(+04:00) Abu Dhadi, Muscat, Baku, Yerevan"));
//		addTimeZoneInfo(new TimeZoneInfo(18, 5, "Etc/GMT-5", "(+05:00) Ekaterinburg, Islamabad, Karachi, Tashkent"));
//		addTimeZoneInfo(new TimeZoneInfo(19, 6, "Etc/GMT-6", "(+06:00) Astana, Dhaka, Almaty, Nonosibirsk"));
//		addTimeZoneInfo(new TimeZoneInfo(20, 7, "Etc/GMT-7", "(+07:00) Krasnoyarsk, Bangkok, Hanoi, Jakarta"));
//		addTimeZoneInfo(new TimeZoneInfo(21, 8, "Etc/GMT-8", "(+08:00) Beijing, Hong Kong, Singapore, Taipei, Irkutsk, Ulaan Bataar, Perth"));
//		addTimeZoneInfo(new TimeZoneInfo(22, 9, "Etc/GMT-9", "(+09:00) Yakutsk, Seoul, Osaka, Sapporo, Tokyo"));
//		addTimeZoneInfo(new TimeZoneInfo(23, 10, "Etc/GMT-10", "(+10:00) Brisbane, Guam, Port Moresby, Canberra, Melbourne, Sydney, Hobart, Vladivostok"));
//		addTimeZoneInfo(new TimeZoneInfo(24, 11, "Etc/GMT-11", "(+11:00) Magadan, Solomon Is., New Caledonia"));
//		addTimeZoneInfo(new TimeZoneInfo(25, 12, "Etc/GMT-12", "(+12:00) Auckland, Wellington, Fiji, Kamchatka, Marshall Is."));

		defaultTimeZoneInfo = getTimeZoneInfoByID("Europe/Moscow");
//		defaultTimeZoneInfo = tzHoursInfoMap.get(3);
	}

	private static void addTimeZoneInfo(TimeZoneInfo info) {
		tzInfos.add(info);
		tzIdNameMap.put(info.getId(), info.getName());
		tzIdInfoMap.put(info.getId(), info);
//		tzHoursInfoMap.put(info.getHours(), info);
		tzIdsInfoMap.put(info.getTzID(), info);
	}

	public static List<TimeZoneInfo> getTimeZoneInfos() {
		return tzInfos;
	}

	public static HashMap<Integer, String> getTzIdNameMap() {
		return tzIdNameMap;
	}

//	public static TimeZoneInfo getTimeZoneInfoByHours(int hours) {
//		TimeZoneInfo info = tzHoursInfoMap.get(hours);
//		if(info != null) {
//			return null;
//		} else {
//			return defaultTimeZoneInfo;
//		}
//	}

	public static TimeZoneInfo getTimeZoneInfoByID(String id) {
		TimeZoneInfo info = tzIdsInfoMap.get(id);
		if(info != null) {
			return info;
		} else {
			return defaultTimeZoneInfo;
		}
	}

	public static TimeZoneInfo getTimeZoneInfoByID(int id) {
		TimeZoneInfo info = tzIdInfoMap.get(id);
		if(info != null) {
			return info;
		} else {
			return defaultTimeZoneInfo;
		}
	}

//	public static void setDefaultTimeZoneInfo(int hours) {
//		defaultTimeZoneInfo = getTimeZoneInfoByHours(hours);
//	}

	public static void setDefaultTimeZoneInfo(String id) {
		defaultTimeZoneInfo = getTimeZoneInfoByID(id);
	}
}
