package mmdanggg2.cste.commands;

import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;

public class CommandMode extends CommandBase {

	@Override
	public String getCommandName() {
		return "cste-fillmode";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cste.commands.fillmode");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Mode Command Recieved!");
		if (!(args.length < 1)) {
			CSTE.selProcessor.onModeCommand(args);
		}
		else {
			CSTELogger.logDebug("No args were given.");
			throw new WrongUsageException("cste.commands.fillmode", new Object[0]);
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if(args.length == 1) {
        	return getListOfStringsMatchingLastWord(args, new String[] {"solid", "hollow", "frame"});
        }
        else {
        	return null;
        }
    }

}
