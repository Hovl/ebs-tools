package ebs.tools;

import ebs.locale.LocaleData;
import org.apache.commons.lang.time.DateUtils;

/**
 * Created by Dubov Aleksey
 * Date: Jun 10, 2009
 * Time: 12:02:24 AM
 * Company: EBS (c) 2009
 */

public class DateTools {
	public static String getDeltaDate(long time, boolean full, String locale) {
		StringBuilder builder = new StringBuilder();

		boolean days = false;
		long delta;

		LocaleData localeData = LocaleData.valueOf(locale);

		if((delta = time / DateUtils.MILLIS_PER_DAY) > 0) {
			builder.append(delta).append(localeData.getDay());
			days = true;
		}
		if((delta = (time / DateUtils.MILLIS_PER_HOUR) % 24) > 0) {
			builder.append(delta).append(localeData.getHour());
		}
		if(full || !days) {
			if((delta = (time / DateUtils.MILLIS_PER_MINUTE) % 60) > 0) {
				builder.append(delta).append(localeData.getMinute());
			}

			if((delta = (time / DateUtils.MILLIS_PER_SECOND) % 60) > 0) {
				builder.append(delta).append(localeData.getSecond());
			}
		}

		return builder.toString();
	}
}
