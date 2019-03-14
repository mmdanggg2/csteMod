package mmdanggg2.cste.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatMessenger {
	public static void addMessage(String str) {
		addMessage(str, TextFormatting.RESET);
	}
	
	public static void addMessageLocalized(String str, Object ... replacements) {
		addMessage(I18n.format(str, replacements));
	}
	
	public static void addMessage(String str, TextFormatting colour) {
		ITextComponent chatComp = new TextComponentString(str);
		chatComp.getStyle().setColor(colour);
		add(chatComp);
	}
	
	public static void addMessageLocalized(String str, TextFormatting colour, Object ... replacements) {
		addMessage(I18n.format(str, replacements), colour);
	}
	
	public static void add(ITextComponent textComp){
		Minecraft.getMinecraft().player.sendMessage(textComp);
	}
	
	public static void sendMessage(String message) {
		Minecraft.getMinecraft().player.sendChatMessage(message);
	}
}
