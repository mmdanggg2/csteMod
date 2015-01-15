package mmdanggg2.cste.commands;

import java.util.ArrayList;
import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StringTranslate;

public class CommandFill extends CommandBase {

	@Override
	public String getName() {
		return "cste-fill";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("commands.cste.fill");
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Fill Command Recieved!");
		if (sender instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) sender;
			if (!(args.length < 1)) {
				CSTE.processor.onFillCommand(player, args);
			}
			else {
				CSTELogger.logDebug("No args were given.");
				throw new WrongUsageException("commands.cste.fill", new Object[0]);
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
        	return func_175762_a(args, Block.blockRegistry.getKeys());
        }
        else {
        	return null;
        }
    }

}
