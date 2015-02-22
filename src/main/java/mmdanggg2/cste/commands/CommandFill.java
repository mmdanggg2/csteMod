package mmdanggg2.cste.commands;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;

public class CommandFill extends CommandBase {

	@Override
	public String getName() {
		return "cste-fill";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return I18n.format("cste.commands.fill");
	}

	@Override
	public void execute(ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Fill Command Recieved!");
		if (ChatRecievedHandler.instance.isBuilding()) {
			throw new CommandException("cste.commands.error.stillbuilding");
		}
		if (args.length >= 1) {
			Block block = CommandBase.getBlockByText(sender, args[0]);
			Integer meta;
			if (args.length >= 2) {
				if (StringUtils.isNumeric(args[1])) {
					meta = Integer.parseInt(args[1]);
					if (meta > 15) {
						throw new WrongUsageException("cste.commands.replace.notint", args[1]);
					}
				}
				else {
					throw new WrongUsageException("cste.commands.replace.notint", args[1]);
				}
				CSTE.selProcessor.onFillCommand(block, meta);
			}
			else {
				meta = 0;
				CSTE.selProcessor.onFillCommand(block, meta);
			}
		}
		else {
			CSTELogger.logDebug("No args were given.");
			throw new WrongUsageException("cste.commands.fill", new Object[0]);
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
        else {
        	return null;
        }
    }

}
