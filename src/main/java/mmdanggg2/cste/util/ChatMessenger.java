package mmdanggg2.cste.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class ChatMessenger {
	public static void addMessage(String str) {
		addMessage(str, EnumChatFormatting.RESET);
	}
	
	public static void addMessageLocalized(String str, Object ... replacements) {
		addMessage(I18n.format(str, replacements));
	}
	
	public static void addMessage(String str, EnumChatFormatting colour) {
		IChatComponent chatComp = new ChatComponentText(str);
		chatComp.getChatStyle().setColor(colour);
		add(chatComp);
	}
	
	public static void addMessageLocalized(String str, EnumChatFormatting colour, Object ... replacements) {
		addMessage(I18n.format(str, replacements), colour);
	}
	
	public static void add(IChatComponent chatComp){
		Minecraft.getMinecraft().thePlayer.addChatMessage(chatComp);
	}
	
	public static void sendMessage(String message) {
		Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
	}
}
