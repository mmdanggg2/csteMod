package mmdanggg2.cste.commands;

import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class CommandWand extends CommandBase {

	@Override
	public String getName() {
		return "cste-wand";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("commands.cste.wand");
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Wand Command Recieved!");
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length != 0 && args[0].equalsIgnoreCase("clear")) {
				CSTELogger.logDebug("Clearing Wand");
				CSTE.processor.wand = null;
				player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.wand.clear")));
			}
			else if (player.getHeldItem() != null) {
				Item item = player.getHeldItem().getItem();
				CSTELogger.logDebug("Setting Wand: " + item.getUnlocalizedName());
				CSTE.processor.wand = item;
				player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.wand.sel", I18n.format(item.getUnlocalizedName() + ".name"))));
			}
			else {
				CSTELogger.logDebug("Wand unchanged, hand was empty");
				player.addChatMessage(new ChatComponentText(I18n.format("commands.cste.wand.noitem")));
			}
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if(args.length == 1) {
        	return getListOfStringsMatchingLastWord(args, new String[] {"clear"});
        }
        else {
        	return null;
        }
    }

}
