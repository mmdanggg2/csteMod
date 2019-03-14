package mmdanggg2.cste.commands;

import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.util.CSTELogger;
import mmdanggg2.cste.util.ChatMessenger;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import org.apache.commons.lang3.StringUtils;

public class CommandReplace extends CommandBase {

	@Override
	public String getName() {
		return "cste-replace";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return I18n.format("cste.commands.replace");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Replace Command Recieved!");
		if (ChatRecievedHandler.instance.isBuilding()) {
			throw new CommandException("cste.commands.error.stillbuilding");
		}
		if (args.length >= 2) {
			String[] args2 = StringUtils.split(args[0], ':');//TODO get this to work with minecraft:block
			Block oldBlock = CommandBase.getBlockByText(sender, args2[0]);
			Integer oldMeta = null;
			if (args2.length == 2 && args2[1] != null && !args2[1].isEmpty()) {
				if (StringUtils.isNumeric(args2[1])) {
					oldMeta = Integer.parseInt(args2[1]);
					if (oldMeta > 15) {
						throw new WrongUsageException("cste.commands.replace.notint", args2[1]);
					}
				}
				else {
					throw new WrongUsageException("cste.commands.replace.notint", args2[1]);
				}
			}
			Block newBlock;
			try {
				newBlock = CommandBase.getBlockByText(null, args[1]);
			} catch (NumberInvalidException e) {
				ChatMessenger.addMessageLocalized(e.getMessage(), TextFormatting.RED, args[1]);
				return;
			}
			Integer newMeta = null;
			if (args.length == 3) {
				if (StringUtils.isNumeric(args[2])) {
					newMeta = Integer.parseInt(args[2]);
					if (newMeta > 15) {
						newMeta = 0;
					}
				}
				else {
					newMeta = 0;
				}
			}
			else {
				newMeta = 0;
			}
			CSTE.selProcessor.onReplaceCommand(oldBlock, oldMeta, newBlock, newMeta);
		}
		else {
			CSTELogger.logDebug("No args were given.");
			throw new WrongUsageException("cste.commands.replace");
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1 || args.length == 2) {
        	return getListOfStringsMatchingLastWord(args, Block.REGISTRY.getKeys());
        }
        else {
        	return null;
        }
    }

}
