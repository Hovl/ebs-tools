package ebs.hb;

import org.hibernate.Session;

/**
 * Created by Dubov Aleksei
 * Date: Nov 30, 2007
 * Time: 3:27:03 AM
 * Company: EBS (c) 2007
 */
public interface HbWorker<T> {
	T work(Session session);
}
