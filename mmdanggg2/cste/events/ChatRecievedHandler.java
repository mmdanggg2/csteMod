package mmdanggg2.cste.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ChatRecievedHandler {
	
	private Map<ErrorType, String> errors = new HashMap<ErrorType, String>();
	private boolean error = false;
	private int blocksChanged = 0;

	@SubscribeEvent
	public void handleEvent(ClientChatReceivedEvent event) {
		if (event.type == 1/* && CSTE.processor.isBuilding()*/) {
			CSTELogger.logDebug("System message recieved: " + event.message.getUnformattedText());
			if (event.message.getUnformattedText().equals(I18n.format("commands.fill.outOfWorld"))) {
				error(ErrorType.OUTOFWORLD, event.message.getUnformattedText());
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.fill.tagError", ""))) {
				error(ErrorType.TAGERROR, event.message.getUnformattedText());
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.give.notFound", ""))) {
				error(ErrorType.BLOCKNOTFOUND, event.message.getUnformattedText());
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.fill.tooManyBlocks", "SPLIT", "").split("SPLIT")[0])) {
				error(ErrorType.TOOMANYBLOCKS, event.message.getUnformattedText());
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.fill.failed"))) {
				CSTELogger.logDebug("No blocks changed.");
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.fill.success", ""))) {
				String changedStr = event.message.getUnformattedText().replace(I18n.format("commands.fill.success", ""), "");
				if (StringUtils.isNumeric(changedStr)) {
					int changed = Integer.parseInt(changedStr);
					blocksChanged += changed;
					CSTELogger.logDebug(changed + " blocks changed, total: " + blocksChanged);
				}
			}
//			String responsesNoTrans[] = new String[] {"commands.fill.outOfWorld",
//					"commands.fill.tagError",
//					"commands.fill.success",
//					"commands.fill.failed",
//					"commands.fill.tooManyBlocks"};
//			String responses[] = new String[responsesNoTrans.length];
//			for (int i = 0; i < responsesNoTrans.length; i++) {
//				responses[i] = I18n.format(responsesNoTrans[i], "INT", "INT");
//			}
//			for (String str : responses) {
//				CSTELogger.logDebug("Responce: " + str/*.split("INT")[0]*/);
//			}
		}
	}
	
	private void error(ErrorType type, String errorText) {
		error = true;
		CSTELogger.logDebug("Error: " + type.name());
		if (!errors.containsKey(type)) {
			CSTELogger.logDebug("Error was not found in map, adding.");
			errors.put(type, errorText);
		}
	}
	
	public void clearErrors() {
		error = false;
		errors.clear();
	}
	
	private enum ErrorType {
		OUTOFWORLD, TAGERROR, TOOMANYBLOCKS, BLOCKNOTFOUND
	}
}
