package ebs.tools;

import java.util.Date;

/**
 * Created by Dubov Aleksei
 * Date: Apr 17, 2007
 * Time: 6:17:36 PM
 * Company: EBS (c) 2007
 */
public class Profiling {
	private long time = 0;
	private long sumTime = 0;

	public Profiling() {
		time = new Date().getTime();
	}

	public Long getTime() {
		return new Date().getTime() - time;
	}

	public void milestone() {
		sumTime += new Date().getTime() - time;
	}

	public Long getSumTime() {
		return sumTime;
	}

	public void clear() {
		sumTime = 0;
	}
}
