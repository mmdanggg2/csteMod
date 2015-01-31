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

public class CommandBrush extends CommandBase {

	@Override
	public String getName() {
		return "cste-brush";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cste.commands.brush");
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Brush Command Recieved!");
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length != 0 && args[0].equalsIgnoreCase("clear")) {
				CSTELogger.logDebug("Clearing Brush");
				CSTE.selProcessor.wand = null;
				player.addChatMessage(new ChatComponentText(I18n.format("cste.commands.brush.clear")));
			}
			else if (player.getHeldItem() != null) {
				Item item = player.getHeldItem().getItem();
				CSTELogger.logDebug("Setting Brush: " + item.getUnlocalizedName());
				CSTE.brushProcessor.brush = item;
				player.addChatMessage(new ChatComponentText(I18n.format("cste.commands.brush.sel", I18n.format(item.getUnlocalizedName() + ".name"))));
			}
			else {
				CSTELogger.logDebug("Brush unchanged, hand was empty");
				player.addChatMessage(new ChatComponentText(I18n.format("cste.commands.brush.noitem")));
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
