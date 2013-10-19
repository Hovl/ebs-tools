package ebs.db;

import java.sql.Connection;

/**
 * Created by Dubov Aleksei
 * Date: Oct 11, 2007
 * Time: 12:28:50 AM
 * Company: EBS (c) 2007
 */
public class DbService {
    private Connection connection;

	public DbService(Connection connection) { this.connection = connection; }

    public Connection getConnection() { return connection; }
}
