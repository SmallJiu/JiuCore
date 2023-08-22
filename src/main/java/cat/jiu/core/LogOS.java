package cat.jiu.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogOS {
	protected final Logger logger;
	protected final String tag;

	public LogOS() {
		String name = new Throwable().getStackTrace()[1].getClassName();
		name = name.substring(name.lastIndexOf(".") + 1);
		this.tag = name;
		this.logger = LogManager.getLogger(this.tag);
		setHandler(logger);
	}

	public LogOS(String tag) {
		this.tag = tag;
		this.logger = LogManager.getLogger(this.tag);
		setHandler(this.logger);
	}

	public LogOS(Class<?> clazz) {
		this.tag = clazz.getSimpleName();
		this.logger = LogManager.getLogger(this.tag);
		setHandler(this.logger);
	}

	public LogOS(Logger logger) {
		this.logger = logger;
		this.tag = logger.getName();
		setHandler(logger);
	}

	private static void setHandler(Logger logger) {

	}

	public Logger getLogger() {
		return logger;
	}

	public String getTag() {
		return tag;
	}

	public void log(Level level, String msg, Object... params) {
		if(this.getLogger()!=null) {
			this.getLogger().log(level, msg, params);
		}
	}

	public void debug(String msg, Object... params) {
		this.log(Level.DEBUG, msg, params);
	}

	public void info(String msg, Object... params) {
		this.log(Level.INFO, msg, params);
	}

	public void warning(String msg, Object... params) {
		this.log(Level.WARN, msg, params);
	}

	public void error(String msg, Object... params) {
		this.log(Level.ERROR, msg, params);
	}

	public void fatal(String msg, Object... params) {
		this.log(Level.FATAL, msg, params);
	}
}
