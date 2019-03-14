package mmdanggg2.cste.commands;

import java.util.List;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import org.apache.commons.lang3.StringUtils;

public class CommandPos extends CommandBase {

	@Override
	public String getName() {
		return "cste-pos";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return I18n.format("cste.commands.pos");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		CSTELogger.logDebug("Pos Command Recieved!");
		if (!(args.length < 1)) {
			int result = 0;
			if (args.length == 4) {
				int[] intArgs = new int[4];
				for (int i = 0 ; i < 4 ; i++) {
					String str = args[i];
					if (StringUtils.isNumeric(str)) {
						intArgs[i] = Integer.parseInt(str);
					}
					else {
						CSTELogger.logDebug("Args given were not ints");
						throw new WrongUsageException("cste.commands.pos", new Object[0]);
					}
				}
				result = CSTE.selProcessor.onPosCommand(intArgs);
			}
			else if (args.length == 1) {
				result = CSTE.selProcessor.onPosCommand(args[0]);
			}
			else {
				CSTELogger.logDebug("Incorrect num of args");
				throw new WrongUsageException("cste.commands.pos", new Object[0]);
			}
			switch (result) {
			case 0:	return;
			case 1:	CSTELogger.logDebug("Arg not \"clear\" or int");
					throw new WrongUsageException("cste.commands.pos", new Object[0]);
			case 2: CSTELogger.logDebug("Invalid pos number");
					throw new CommandException("cste.commands.pos.invalidnum", args[0]);
			}
		}
		else {
			CSTELogger.logDebug("No args were given.");
			throw new WrongUsageException("cste.commands.pos", new Object[0]);
		}
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}
	
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		if (args.length > 1 && args.length <= 4) {
			return getTabCompletionCoordinate(args, 0, pos);
		}
		else {
			return null;
		}
	}
}
