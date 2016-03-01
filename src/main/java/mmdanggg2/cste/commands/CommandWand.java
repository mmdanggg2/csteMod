package mmdanggg2.cste.commands;

import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import mmdanggg2.cste.util.ChatMessenger;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;

public class CommandWand extends CommandBase {

	@Override
	public String getCommandName() {
		return "cste-wand";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cste.commands.wand");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Wand Command Recieved!");
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length != 0 && args[0].equalsIgnoreCase("clear")) {
				CSTELogger.logDebug("Clearing Wand");
				CSTE.selProcessor.wand = null;
				ChatMessenger.addMessageLocalized("cste.commands.wand.clear");
			}
			else if (player.getHeldItem() != null) {
				Item item = player.getHeldItem().getItem();
				CSTELogger.logDebug("Setting Wand: " + item.getUnlocalizedName());
				CSTE.selProcessor.wand = item;
				ChatMessenger.addMessageLocalized("cste.commands.wand.sel", I18n.format(item.getUnlocalizedName() + ".name"));
			}
			else {
				CSTELogger.logDebug("Wand unchanged, hand was empty");
				ChatMessenger.addMessageLocalized("cste.commands.wand.noitem", EnumChatFormatting.RED);
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if(args.length == 1) {
        	return getListOfStringsMatchingLastWord(args, new String[] {"clear"});
        }
        else {
        	return null;
        }
    }
}
