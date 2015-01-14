package mmdanggg2.cste.client;

import mmdanggg2.cste.CommonProxy;
import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTEInfo;
import mmdanggg2.cste.commands.CommandCSTEHelp;
import mmdanggg2.cste.commands.CommandSelectTool;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
	
	@Override
	public void registerRenderers() {
	}
	
	@Override
	public void registerCommands() {
		
		ClientCommandHandler.instance.registerCommand(new CommandSelectTool());
		ClientCommandHandler.instance.registerCommand(new CommandCSTEHelp());
	}

}