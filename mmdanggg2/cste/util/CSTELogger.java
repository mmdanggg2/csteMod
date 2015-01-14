package mmdanggg2.cste.util;

import org.apache.logging.log4j.Logger;

import mmdanggg2.cste.CSTEInfo;
import net.minecraftforge.fml.common.FMLLog;

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
