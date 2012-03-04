package com.meidusa.amoeba.aladdin.test;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.SimpleLayout;

public class ApacheLog {
	public static void main(String args[]) {
		Logger logger = Logger.getLogger(ApacheLog.class);
		SimpleLayout layout = new SimpleLayout();
		FileAppender appender = null;
		try {
			appender = new FileAppender(layout, "myApacheLog.log", false);
		} catch (Exception e) {
		}
		logger.addAppender(appender);

		// Set the logger level to Level.INFO
		logger.setLevel(Level.INFO);

		// This request will be disabled since Level.DEBUG < Level.INFO.
		logger.debug("This is debug.");

		// These requests will be enabled.
		logger.info("This is an info.");
		logger.warn("This is a warning.");
		logger.error("This is an error.");
		logger.fatal("This is a fatal error.");
	}
}
