package ebs.web;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ebs.db.DbConnector;
import ebs.hb.HbUtil;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.List;

/**
 * Created by Dubov Aleksey
 * Date: Jan 30, 2010
 * Time: 12:44:06 PM
 * Company: EBS (c) 2009
 */

public abstract class StartStopServlet extends HttpServlet {
	protected static final Object sync = new Object();
	protected static volatile Boolean started = false;

	private static void getInited(StartStopServlet servlet) {
				if(!started) {
					servlet.onStart();

					started = true;
				}
	}

	private static void getDestroyed(StartStopServlet servlet) {
		if(started) {
			servlet.onStop();

			HbUtil.closeFactory();
			DbConnector.closeSource();

			LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
			List<Logger> loggers = loggerContext.getLoggerList();
			for (Logger logger : loggers) {
				logger.detachAndStopAllAppenders();
			}
			loggerContext.stop();

			started = false;
		}
	}

	@Override
	public void init() throws ServletException {
		synchronized (sync) {
			getInited(this);
		}

		super.init();
	}

	@Override
	public void destroy() {
		synchronized (sync) {
			getDestroyed(this);
		}

		super.destroy();
	}

	protected abstract void onStart();

	protected abstract void onStop();
}
