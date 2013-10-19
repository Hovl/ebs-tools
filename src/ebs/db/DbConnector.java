package ebs.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import ebs.hb.DataSourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by Dubov Aleksey
 * Date: Oct 10, 2007
 * Time: 11:45:52 PM
 * Company: EBS (c) 2007-2011
 */
public class DbConnector {
	private static Logger logger = LoggerFactory.getLogger(DbConnector.class);
	private static ComboPooledDataSource source = null;

	static {
		if (source == null) {
			try {
				DataSourceProvider dataSourceProvider =
						(DataSourceProvider) Class.forName("ebs.db.DbDataSourceProvider").newInstance();
				source = dataSourceProvider.configure(new ComboPooledDataSource());
			} catch (Exception e) {
				logger.warn("No DbDataSourceProvider found! Trying to load from dataSource.properties...");
			}
		}

		if (source == null) {
			try {
				InputStream stream = DbConnector.class.getResourceAsStream(File.separator + "dataSources.properties");
				try {
					if (stream != null) {
						Properties dataSourcesProp = new Properties();
						dataSourcesProp.load(stream);

						source = new ComboPooledDataSource();
						source.setDriverClass(dataSourcesProp.getProperty("driver"));
						source.setJdbcUrl(dataSourcesProp.getProperty("url"));
						source.setUser(dataSourcesProp.getProperty("login"));
						source.setPassword(dataSourcesProp.getProperty("password"));
						source.setMinPoolSize(2);
						source.setMaxPoolSize(100);
						source.setAcquireIncrement(1);
						source.setNumHelperThreads(6);
						source.setMaxStatements(100);
						source.setMaxStatementsPerConnection(12);
						source.setMaxAdministrativeTaskTime(10000);
					}
				} finally {
					if (stream != null) {
						stream.close();
					}
				}
			} catch (Exception e) {
				throw new RuntimeException("Cannot get SQL connection data", e);
			}
		}

	}

	public static Connection getConnection() {
		try {
			return source.getConnection();
		} catch (SQLException e) {
			LoggerFactory.getLogger(DbConnector.class).warn("Cannot create SQL Connection", e);
			throw new RuntimeException("Cannot create SQL Connection", e);
		}
	}

	public static void releaseConnection(Connection conn) {
		try {
			if (conn != null && !conn.isClosed()) {
				if (!conn.getAutoCommit()) {
					conn.commit();
				}
				conn.close();
			}
		} catch (SQLException e) {
			LoggerFactory.getLogger(DbConnector.class).warn("Cann't close SQL Connection", e);
		}
	}

	public static void closeSource() {
		if (source != null) {

		}
	}
}

