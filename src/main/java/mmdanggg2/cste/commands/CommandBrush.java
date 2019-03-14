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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class CommandBrush extends CommandBase {

	@Override
	public String getName() {
		return "cste-brush";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return I18n.format("cste.commands.brush");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Brush Command Recieved!");
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (args.length != 0 && args[0].equalsIgnoreCase("clear")) {
				CSTELogger.logDebug("Clearing Brush");
				CSTE.brushProcessor.brush = null;
				ChatMessenger.addMessageLocalized("cste.commands.brush.clear");
			}
			else if (player.getHeldItemMainhand() != null) {//TODO support offhand?
				Item item = player.getHeldItemMainhand().getItem();
				CSTELogger.logDebug("Setting Brush: " + item.getUnlocalizedName());
				CSTE.brushProcessor.brush = item;
				ChatMessenger.addMessageLocalized("cste.commands.brush.sel", I18n.format(item.getUnlocalizedName() + ".name"));
			}
			else {
				CSTELogger.logDebug("Brush unchanged, hand was empty");
				ChatMessenger.addMessageLocalized("cste.commands.brush.noitem", TextFormatting.RED);
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
