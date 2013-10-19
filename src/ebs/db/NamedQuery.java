package ebs.db;

import ebs.context.Context;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Dubov Aleksey
 * Date: Oct 10, 2007
 * Time: 10:23:59 PM
 * Company: EBS (c) 2007-2012
 */
public class NamedQuery {
	private static final Pattern PARAM_PATTERN = Pattern.compile(":([\\w\\.]+)");
	private static final Pattern QUESTION_PATTERN = Pattern.compile("\\?");

	private String sql;
	private String[] paramNames;

	public NamedQuery(String namedSQL) {
        StringBuffer filteredSQL = new StringBuffer();
        ArrayList<String> namesList = new ArrayList<String>(10);
        Matcher matcher = PARAM_PATTERN.matcher(namedSQL);
        while (matcher.find()) {
            namesList.add(matcher.group(1));
            matcher.appendReplacement(filteredSQL, "?");
        }
        matcher.appendTail(filteredSQL);
        sql = filteredSQL.toString();
        paramNames = namesList.toArray(new String[namesList.size()]);
    }

    public PreparedStatement getPreparedStatement(Connection connection, Context context) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(sql);
		if(context == null) {
			return statement;
		}

        for (int i = 0; i < paramNames.length; i++) {
            String name = paramNames[i];
            statement.setObject(i + 1, context.getArgument(name));
        }
        return statement;
    }

    public String substituteArguments(Context context) {
        StringBuffer buf = new StringBuffer();
        Matcher m = QUESTION_PATTERN.matcher(sql);
        for (int i = 0; m.find() && i < paramNames.length; i++) {
            Object value = context.getArgument(paramNames[i]);
            m.appendReplacement(buf, value != null ? formatValue(value) : "NULL");
        }
        m.appendTail(buf);
        return buf.toString();
    }

    private static String formatValue(Object argument) {
        if (argument instanceof Number || argument instanceof Boolean) {
			return argument.toString();
		}
        return '\'' + argument.toString() + '\'';
    }
}
