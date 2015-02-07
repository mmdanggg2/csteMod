package mmdanggg2.cste.commands;

import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class CommandBrushMode extends CommandBase {

	@Override
	public String getName() {
		return "cste-brushmode";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cste.commands.brushmode");
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Brush mode Command Recieved!");
		CSTE.brushProcessor.onModeCommand(args);
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	public List<?> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if(args.length == 1) {
        	return getListOfStringsMatchingLastWord(args, new String[] {"fill", "hollow", "smooth"});
        }
        else {
        	return null;
        }
    }

}
