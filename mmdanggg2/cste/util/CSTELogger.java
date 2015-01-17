package mmdanggg2.cste.util;

import mmdanggg2.cste.CSTEInfo;

import org.apache.logging.log4j.Logger;

public class CSTELogger {
	
	public static Logger logger;
	
	public static void logInfo(String message) {
		logger.info(message);
	}

	public static void logDebug(String message) {
		if (CSTEInfo.debug) {
			logger.info("[DEBUG] " + message);
		}
	}
}
