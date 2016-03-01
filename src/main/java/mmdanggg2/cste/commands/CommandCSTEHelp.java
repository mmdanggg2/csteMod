package mmdanggg2.cste.commands;

import mmdanggg2.cste.util.CSTELogger;
import mmdanggg2.cste.util.ChatMessenger;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class CommandCSTEHelp extends CommandBase {

	@Override
	public String getCommandName() {
		return "cste-help";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cste.commands.help");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Help Command Recieved!");
		ChatMessenger.addMessageLocalized("cste.commands.fill.help");
		ChatMessenger.addMessageLocalized("cste.commands.replace.help");
		ChatMessenger.addMessageLocalized("cste.commands.wand.help");
		ChatMessenger.addMessageLocalized("cste.commands.fillmode.help");
		ChatMessenger.addMessageLocalized("cste.commands.pos.help");
		ChatMessenger.addMessageLocalized("cste.commands.brush.help");
		ChatMessenger.addMessageLocalized("cste.commands.brushmode.help");
		ChatMessenger.addMessageLocalized("cste.commands.undo.help");
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
