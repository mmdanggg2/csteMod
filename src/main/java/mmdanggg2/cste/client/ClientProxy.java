package mmdanggg2.cste.client;

import mmdanggg2.cste.CommonProxy;
import mmdanggg2.cste.commands.CommandBrush;
import mmdanggg2.cste.commands.CommandBrushMode;
import mmdanggg2.cste.commands.CommandCSTEHelp;
import mmdanggg2.cste.commands.CommandFill;
import mmdanggg2.cste.commands.CommandMode;
import mmdanggg2.cste.commands.CommandPos;
import mmdanggg2.cste.commands.CommandReplace;
import mmdanggg2.cste.commands.CommandUndo;
import mmdanggg2.cste.commands.CommandWand;
import mmdanggg2.cste.render.CSTERenderer;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers() {
		MinecraftForge.EVENT_BUS.register(new CSTERenderer());
	}
	
	@Override
	public void registerCommands() {
		
		ClientCommandHandler.instance.registerCommand(new CommandWand());
		ClientCommandHandler.instance.registerCommand(new CommandCSTEHelp());
		ClientCommandHandler.instance.registerCommand(new CommandFill());
		ClientCommandHandler.instance.registerCommand(new CommandReplace());
		ClientCommandHandler.instance.registerCommand(new CommandMode());
		ClientCommandHandler.instance.registerCommand(new CommandPos());
		ClientCommandHandler.instance.registerCommand(new CommandBrush());
		ClientCommandHandler.instance.registerCommand(new CommandBrushMode());
		ClientCommandHandler.instance.registerCommand(new CommandUndo());
	}

}