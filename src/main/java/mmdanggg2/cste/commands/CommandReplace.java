package mmdanggg2.cste.commands;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;

public class CommandReplace extends CommandBase {

	@Override
	public String getName() {
		return "cste-replace";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cste.commands.replace");
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Replace Command Recieved!");
		if (args.length >= 2) {
			String[] args2 = StringUtils.split(args[0], ':');
			Block block = CommandBase.getBlockByText(null, args2[0]);
			String replacement = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), " ");
			Integer meta = null;
			if (args2.length == 2 && args2[1] != null && !args2[1].isEmpty()) {
				if (StringUtils.isNumeric(args2[1])) {
					meta = Integer.parseInt(args2[1]);
					if (meta > 15) {
						throw new WrongUsageException("cste.commands.replace.notint", args2[1]);
					}
				}
				else {
					throw new WrongUsageException("cste.commands.replace.notint", args2[1]);
				}
			}
			CSTE.selProcessor.onReplaceCommand(block, meta, replacement);
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

    public List<?> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1) {
        	return func_175762_a(args, Block.blockRegistry.getKeys());
        }
        if (args.length == 3) {
        	return getListOfStringsMatchingLastWord(args, new String[] {"replace", "destroy", "keep", "hollow", "outline"});
        }
        else {
        	return null;
        }
    }

}
