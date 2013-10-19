package ebs.hb;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Dubov Aleksey
 * Date: Nov 30, 2007
 * Time: 3:28:00 AM
 * Company: EBS (c) 2007-2011
 */

public abstract class HbUtil {
	private static Logger logger = LoggerFactory.getLogger(HbUtil.class);
	private static SessionFactory factory = null;
	private static CountableThreadLocal<Session> localSession = new CountableThreadLocal<>();
	private static CountableThreadLocal<Transaction> localTransaction = new CountableThreadLocal<>();

	static {
		try {
			DataSourceProvider dataSourceProvider =
					(DataSourceProvider) Class.forName("ebs.db.DbDataSourceProvider").newInstance();
			Configuration configuration = dataSourceProvider.configure(new Configuration());
			factory = configuration.buildSessionFactory();
		} catch (Exception e) {
			logger.warn("No HbDataSourceProvider found! Trying to load from dataSource.properties...");

			try {
				boolean hbConfigOnStart = false;

				InputStream stream = HbUtil.class.getResourceAsStream(File.separator + "dataSources.properties");
				try {
					if (stream != null) {
						Properties dataSourcesProp = new Properties();
						dataSourcesProp.load(stream);

						hbConfigOnStart = Boolean.parseBoolean(dataSourcesProp.getProperty("hbConfigOnStart", "false"));
					}
				} finally {
					if (stream != null) {
						stream.close();
					}
				}

				if (hbConfigOnStart) {
					factory = new Configuration().configure().buildSessionFactory();
				}
			} catch (Exception e1) {
				throw new RuntimeException("Cannot create SessionFactory for Hibernate", e1);
			}
		}
	}

	public static void setSessionFactory(Configuration configuration) {
		if (factory != null) {
			factory.close();
		}
		factory = configuration.configure().buildSessionFactory();
	}

	private static Session getSessionWithoutTransaction() {
		Session session = localSession.use();
		if (session != null) {
			if (session.isOpen()) {
				return session;
			}
		}

		if (factory == null) {
			throw new RuntimeException("Set SessionFactory First!");
		}
		localSession.setValue(factory.openSession());
		return localSession.use();
	}

	public static Session getSessionWithTransaction() {
		Session session = localSession.use();
		if (session != null) {
			if (session.isOpen()) {
				return beginTransaction(session);
			}
		}

		if (factory == null) {
			throw new RuntimeException("Set SessionFactory First!");
		}
		localSession.setValue(factory.openSession());
		return beginTransaction(localSession.use());
	}

	private static Session beginTransaction(Session session) {
		Transaction transaction = localTransaction.use();
		if (transaction != null) {
			if (transaction.isActive()) {
				return session;
			}
		}

		localTransaction.setValue(session.beginTransaction());
		localTransaction.use();
		return session;
	}

	public static <T> T doWork(HbWorker<T> worker) {
		T result = null;
		try {
			result = worker.work(getSessionWithTransaction());
			commit();
		} catch (HibernateException e) {
			rollback();
			LoggerFactory.getLogger(HbUtil.class).warn("Error while hb execution", e);
		} finally {
			closeSession();
		}

		return result;
	}

	public static <T> T doRead(HbWorker<T> worker) {
		T result = null;
		try {
			result = worker.work(getSessionWithoutTransaction());
			commit();
		} catch (HibernateException e) {
			rollback();
			LoggerFactory.getLogger(HbUtil.class).warn("Error while hb execution", e);
		} finally {
			closeSession();
		}

		return result;
	}

	public static void commit() {
		Transaction transaction = localTransaction.release();
		if (transaction != null && transaction.isActive()) {
			transaction.commit();
		}
		Session session = localSession.get();
		if(session != null) {
			session.flush();
		}
	}

	public static void rollback() {
		Transaction transaction = localTransaction.get();
		Session session = localSession.get();
		if (transaction != null && session != null && session.isOpen()) {
			transaction.rollback();
		}
	}

	public static void closeSession() {
		localTransaction.release();
		Session session = localSession.release();
		if (session != null) {
			session.flush();
			session.close();
			localTransaction.setValue(null);
			localSession.setValue(null);
		}
	}

	public static void closeFactory() {
		if (factory != null) {
			factory.close();
		}
	}

	public static String addSortBy(String query, int sortby, int order) {
		return query + " order by " + sortby + ' ' + (order == 1 ? "asc" : "desc");
	}

	public static Map<Integer, String> getIntegerStringMap(Query query) {
		List list = query.list();

		HashMap<Integer, String> result = new HashMap<Integer, String>(list.size());
		for (Object o : list) {
			Object[] os = (Object[]) o;
			result.put((Integer) os[0], (String) os[1]);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> getCastedList(Query query) {
		return query.list();
	}
}
