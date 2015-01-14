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

public class CommandSelectTool extends CommandBase {

	@Override
	public String getName() {
		return "cste-tool";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/cste-tool [clear]";
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Tool Command Recieved!!!");
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length != 0 && args[0].equalsIgnoreCase("clear")) {
				CSTELogger.logDebug("Clearing Wand");
				CSTE.processor.setWand(null);
				player.addChatMessage(new ChatComponentText("Wand Cleared."));
			}
			else if (player.getHeldItem() != null) {
				Item item = player.getHeldItem().getItem();
				CSTE.processor.setWand(item);
				player.addChatMessage(new ChatComponentText("Set " + item.getUnlocalizedName() + " as the wand."));
			}
			else {
				player.addChatMessage(new ChatComponentText("Please hold the item to use as a wand."));
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

}
