package mmdanggg2.cste.events;

import java.util.HashMap;
import java.util.Map;

import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import org.apache.commons.lang3.StringUtils;

public class ChatRecievedHandler {
	
	private Map<ErrorType, String> errors = new HashMap<ErrorType, String>();
	private boolean error = false;
	private int blocksChanged = 0;
	private int messagesNeeded = 0;
	private int messagesGathered = 0;
	private boolean building;
	private EntityPlayer player;
	public static ChatRecievedHandler instance;

	public ChatRecievedHandler() {
		instance = this;
	}

	@SubscribeEvent
	public void handleEvent(ClientChatReceivedEvent event) {
		if (event.type == 1 && building) {
			CSTELogger.logDebug("System message recieved: " + event.message.getUnformattedText());
			if (event.message.getUnformattedText().equals(I18n.format("commands.fill.outOfWorld"))) {
				error(ErrorType.OUTOFWORLD, event.message.getUnformattedText());
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.fill.tagError", ""))) {
				error(ErrorType.TAGERROR, event.message.getUnformattedText());
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.give.notFound", ""))) {
				error(ErrorType.BLOCKNOTFOUND, event.message.getUnformattedText());
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.fill.tooManyBlocks", "SPLIT", "").split("SPLIT")[0])) {
				error(ErrorType.TOOMANYBLOCKS, event.message.getUnformattedText());
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.fill.failed"))) {
				messagesGathered++;
				event.setCanceled(true);
			}
			else if (event.message.getUnformattedText().contains(I18n.format("commands.fill.success", ""))) {
				String changedStr = event.message.getUnformattedText().replace(I18n.format("commands.fill.success", ""), "");
				if (StringUtils.isNumeric(changedStr)) {
					int changed = Integer.parseInt(changedStr);
					blocksChanged += changed;
					CSTELogger.logDebug(changed + " blocks changed, total: " + blocksChanged);
				}
				messagesGathered++;
				event.setCanceled(true);
			}
			CSTELogger.logDebug(messagesGathered + " messages gathered.");
			if (messagesGathered >= messagesNeeded) {
				if (error) {
					IChatComponent errorMessage = new ChatComponentText(getError());
					errorMessage.getChatStyle().setColor(EnumChatFormatting.RED);
					player.addChatMessage(errorMessage);
				}
				player.addChatMessage(new ChatComponentText(getChanged()));
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
		String out = I18n.format("commands.cste.fill.builderr", sb.toString());
		return out;
	}
		
	private String getChanged() {
		String str;
		if (blocksChanged > 0) {
			CSTELogger.logDebug(blocksChanged + " blocks were changed.");
			str = I18n.format("commands.cste.fill.success", blocksChanged);
		}
		else {
			CSTELogger.logDebug("No blocks changed");
			str = I18n.format("commands.cste.fill.nochange");
		}
		return str;
	}
	
	public void buildingStart(int numResults, EntityPlayer player) {
		building = true;
		messagesNeeded = numResults;
		this.player = player;
	}
	
	private enum ErrorType {
		OUTOFWORLD, TAGERROR, TOOMANYBLOCKS, BLOCKNOTFOUND
	}
}
