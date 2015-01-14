package mmdanggg2.cste.commands;

import java.util.ArrayList;
import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringTranslate;

public class CommandCSTEHelp extends CommandBase {

	@Override
	public String getName() {
		return "cste-help";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/cste-help";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Help Command Recieved!!!");
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			player.addChatMessage(new ChatComponentText("/cste-tool [clear] - selects/clears the wand item."));
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
