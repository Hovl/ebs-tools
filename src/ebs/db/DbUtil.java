package ebs.db;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Dubov Aleksei
 * Date: Oct 12, 2007
 * Time: 3:13:11 PM
 * Company: EBS (c) 2007
 */
public abstract class DbUtil {
	public static <T>T doWork(DbWorker<T> worker) throws SQLException {
		Connection dbConnection = DbConnector.getConnection();

		T result;
		try {
			result = worker.work(new DbService(dbConnection));
		} finally {
			DbConnector.releaseConnection(dbConnection);
		}
		return result;
	}

	public static String addSortBy(String query, int sortby, int order, int from, int to) {
		return new StringBuilder(query)
				.append(" order by ").append(sortby).append(' ').append(order == 0 ? "asc" : "desc")
				.append(" limit ").append(from).append(',').append(to)
				.toString();
	}
}
