package mmdanggg2.cste;

import mmdanggg2.cste.events.PlayerInteractEventHandler;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = CSTEInfo.ID, name = CSTEInfo.NAME, version = CSTEInfo.VER, guiFactory = "mmdanggg2.cste.client.CSTEConfigGUIFactory", acceptableRemoteVersions = "*")
public class CSTE {
	// The instance of your mod that Forge uses.
	@Instance(CSTEInfo.ID)
	public static CSTE instance;
	public static Configuration config;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = CSTEInfo.CLIENTPROXY, serverSide = CSTEInfo.COMMONPROXY)
	public static CommonProxy proxy;
	
	public static Item wand = null;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		CSTELogger.logger = event.getModLog();
		
		if (event.getSide().isServer()) {
			CSTELogger.logInfo("This is a client-side mod, it has no effect on servers!".toUpperCase());
			return;
		}
		
		if (config == null) {
			config = new Configuration(event.getSuggestedConfigurationFile());
		}
		
		CSTELogger.logInfo("Loading Config");
		// loading the configuration from its file
		config.load();
		updateConfig();

//		CSTELogger.logInfo("Registering Items");
//		DogeRegisterItems.register();
//		
//		CSTELogger.logInfo("Registering Blocks");
//		DogeRegisterBlocks.register();
//		
//		CSTELogger.logInfo("Registering Entities");
//		DogeRegisterEntities.register();
//		
//		CSTELogger.logInfo("Registering Recipies");
//		DogeRegisterRecipies.register();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide().isServer()) {
			return;
		}
		proxy.registerRenderers();
		proxy.registerCommands();
		
		FMLCommonHandler.instance().bus().register(instance);
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
	
	public static void updateConfig() {
		
		CSTEInfo.debug = config.get("debug", "DebugOutput", false, "Show debug output in log (Default false)").getBoolean(false);
		
		// saving the configuration to its file
		if (config.hasChanged()){
			config.save();
		}
	}

	@SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(CSTEInfo.ID))
        	CSTELogger.logInfo("Reloading Config");
            updateConfig();
    }
}
