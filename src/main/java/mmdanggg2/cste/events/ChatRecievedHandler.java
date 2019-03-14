package mmdanggg2.cste.events;

import java.util.HashMap;
import java.util.Map;

import mmdanggg2.cste.util.CSTELogger;
import mmdanggg2.cste.util.ChatMessenger;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.commons.lang3.StringUtils;

public class ChatRecievedHandler {
	
	private Map<ErrorType, String> errors = new HashMap<ErrorType, String>();
	private boolean error = false;
	private int blocksChanged = 0;
	private int messagesNeeded = 0;
	private int messagesGathered = 0;
	private boolean building = false;
	public static ChatRecievedHandler instance;
	
	private int cmdsWasted = 0;

	public ChatRecievedHandler() {
		instance = this;
	}

	@SubscribeEvent
	public void handleEvent(ClientChatReceivedEvent event) {
		//TODO put in all failure chat events
		if (event.getType() == ChatType.SYSTEM && building) {
			//CSTELogger.logDebug("System message recieved: " + event.message.getUnformattedText());
			if (event.getMessage().getUnformattedText().equals(I18n.format("commands.fill.outOfWorld"))) {
				error(ErrorType.OUTOFWORLD, event.getMessage().getUnformattedText());
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.getMessage().getUnformattedText().contains(I18n.format("commands.fill.tagError", ""))) {
				error(ErrorType.TAGERROR, event.getMessage().getUnformattedText());
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.getMessage().getUnformattedText().contains(I18n.format("commands.give.notFound", ""))) {
				error(ErrorType.BLOCKNOTFOUND, event.getMessage().getUnformattedText());
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.getMessage().getUnformattedText().contains(I18n.format("commands.fill.tooManyBlocks", "SPLIT", "").split("SPLIT")[0])) {
				error(ErrorType.TOOMANYBLOCKS, event.getMessage().getUnformattedText());
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.getMessage().getUnformattedText().contains(I18n.format("commands.fill.failed"))) {
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.getMessage().getUnformattedText().contains(I18n.format("commands.setblock.noChange"))) {
				messagesGathered++;
				cmdsWasted++;
				event.setCanceled(true);
			}
			else if (event.getMessage().getUnformattedText().contains(I18n.format("commands.setblock.success"))) {
				blocksChanged++;
				//CSTELogger.logDebug("1 block changed, total: " + blocksChanged);
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.getMessage().getUnformattedText().contains(I18n.format("commands.fill.success", ""))) {
				String changedStr = event.getMessage().getUnformattedText().replace(I18n.format("commands.fill.success", ""), "");
				if (StringUtils.isNumeric(changedStr)) {
					int changed = Integer.parseInt(changedStr);
					blocksChanged += changed;
					CSTELogger.logDebug(changed + " blocks changed, total: " + blocksChanged);
				}
				messagesGathered++;
				event.setCanceled(true);
			}
			//CSTELogger.logDebug(messagesGathered + " messages gathered.");
			if (messagesGathered >= messagesNeeded) {
				if (error) {
					ChatMessenger.addMessage(getError(), TextFormatting.RED);
				}
				ChatMessenger.addMessage(getChanged());
				clearAll();
			}
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
	
	private void clearAll() {
		blocksChanged = 0;
		messagesGathered = 0;
		messagesNeeded = 0;
		building = false;
		error = false;
		cmdsWasted = 0;
		errors.clear();
	}
	
	private String getError() {
		CSTELogger.logDebug("Getting errors");
		StringBuilder sb = new StringBuilder();
		boolean comma = false;
		for (String str : errors.values()) {
			if (comma) {
				sb.append(", ");
			}
			if (!str.isEmpty()){
				comma = true;
				sb.append(str);
			}
		}
		String out = I18n.format("cste.commands.fill.builderr", sb.toString());
		return out;
	}
		
	private String getChanged() {
		String str;
		if (blocksChanged > 0) {
			CSTELogger.logDebug(blocksChanged + " blocks were changed.");
			CSTELogger.logDebug(cmdsWasted + " commands wasted.");
			str = I18n.format("cste.commands.fill.success", blocksChanged);
		}
		else {
			CSTELogger.logDebug("No blocks changed");
			CSTELogger.logDebug(cmdsWasted + " commands wasted.");
			str = I18n.format("cste.commands.fill.nochange");
		}
		return str;
	}
	
	public void buildingStart(int numResults) {
		if (numResults < 1) {
			ChatMessenger.addMessageLocalized("cste.commands.fill.nochange");
			return;
		}
		building = true;
		messagesNeeded = numResults;
	}

	public void buildingStop() {
		CSTELogger.logDebug("Building Stopped!");
		if (error) {CSTELogger.logInfo(getError());}
		CSTELogger.logInfo(getChanged());
		CSTELogger.logDebug((messagesNeeded - messagesGathered) + " messages were not counted!");
		clearAll();
	}
	
	public boolean isBuilding() {
		return building;
	}
	
	private enum ErrorType {
		OUTOFWORLD, TAGERROR, TOOMANYBLOCKS, BLOCKNOTFOUND, COULDNTPLACE
	}
}
