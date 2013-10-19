package ebs.db;

import java.sql.SQLException;

/**
 * Created by Dubov Aleksei
 * Date: Oct 11, 2007
 * Time: 12:26:11 AM
 * Company: EBS (c) 2007
 */

public interface DbWorker<T> {
	T work(DbService service) throws SQLException;
}
