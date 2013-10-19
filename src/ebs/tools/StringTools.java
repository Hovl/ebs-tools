package ebs.tools;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dubov Aleksei
 * Date: Jul 24, 2008
 * Time: 4:49:46 PM
 * Company: EBS (c) 2008
 */

public class StringTools {
	public static Map<String, String> getMapFromString(String input, String div, String eq) {
		Map<String, String> data = new HashMap<String, String>();

		int cur_index = 0;
		int last_index = 0;
		while (cur_index >= 0) {
			int mid = input.indexOf(eq, last_index);
			if(mid < 0) break;
			cur_index = input.indexOf(div, cur_index);
			if(cur_index < 0) cur_index = input.length();
			data.put(input.substring(last_index, mid), input.substring(mid + 1, cur_index));
			cur_index +=2;
			last_index = cur_index;
		}

		return data;
	}

	public static String getStringFromMap(Map<Object, Object> map, String div, String eq) {
		StringBuilder builder = new StringBuilder();

		Set<Map.Entry<Object, Object>> set = map.entrySet();
		for(Map.Entry<Object, Object> entry : set) {
			builder.append(entry.getKey()).append(eq).append(entry.getValue()).append(div);
		}
		
		return builder.toString();
	}

	public static String intArrayToSQLString(int[] ints) {
		StringBuilder buffer = new StringBuilder("(");
		for(int i = 0; i < ints.length - 1; i++) {
			buffer.append(ints[i]).append(',');
		}
		return buffer.append(ints[ints.length - 1]).append(')').toString();
	}

	public static Float parseFloatSafe(String s) {
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			return null;
		}
	}

    public static Integer parseIntegerSafe(String s) {
   		try {
   			return Integer.parseInt(s);
   		} catch (NumberFormatException e) {
   			return null;
   		}
   	}

    public static Number parseNumberSafe(String s) {
   		try {
   			return NumberFormat.getInstance().parse(s);
   		} catch (ParseException e) {
               return null;
        }
    }
}
