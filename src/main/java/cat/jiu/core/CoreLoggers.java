package cat.jiu.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CoreLoggers {
	private static Logger logger;
	private static LogOS logos;
	
	public static Logger getLogger() {
		if(logger == null) {
			logger = LogManager.getLogger(JiuCore.NAME);
		}
		return logger;
	}

	public static LogOS getLogOS() {
		if(logos==null) {
			logos = new LogOS(getLogger());
		}
		return logos;
	}
}
