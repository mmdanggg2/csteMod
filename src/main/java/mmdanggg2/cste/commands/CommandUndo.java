package mmdanggg2.cste.commands;

import java.util.HashSet;
import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTESelectionProcessor;
import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.util.CSTELogger;
import mmdanggg2.cste.util.ChatMessenger;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

public class CommandUndo extends CommandBase {

	@Override
	public String getName() {
		return "cste-undo";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cste.commands.undo");
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Undo Command Recieved!");
		if (ChatRecievedHandler.instance.isBuilding()) {
			throw new CommandException("cste.commands.error.stillbuilding");
		}
		HashSet<BlockDelta> history = CSTE.history.getHistory();
		HashSet<String> commands = new HashSet<String>();
		for (BlockDelta bd : history) {
			if (bd.isChanged()) {
				String command = "/setblock " + CSTESelectionProcessor.posToStr(bd.getPos()) + " " + bd.getOldBlockStr();
				if (!commands.contains(command)) {
					commands.add(command);
				}
			}
		}
		ChatRecievedHandler.instance.buildingStart(commands.size());
		for (String command : commands) {
			ChatMessenger.sendMessage(command);
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	public List<?> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        return null;
    }

}
