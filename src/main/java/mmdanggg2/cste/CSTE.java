package mmdanggg2.cste;

import java.util.regex.Pattern;

import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.events.KeyEventHandler;
import mmdanggg2.cste.events.MouseEventHandler;
import mmdanggg2.cste.events.PlayerInteractEventHandler;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
	
	public static CSTESelectionProcessor selProcessor;
	
	public static CSTEBrushProcessor brushProcessor;

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
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (event.getSide().isServer()) {
			return;
		}
		proxy.registerRenderers();
		proxy.registerCommands();
		
		FMLCommonHandler.instance().bus().register(instance);
		FMLCommonHandler.instance().bus().register(new KeyEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerInteractEventHandler());
		MinecraftForge.EVENT_BUS.register(new ChatRecievedHandler());
		MinecraftForge.EVENT_BUS.register(new MouseEventHandler());
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (event.getSide().isServer()) {
			return;
		}
		CSTE.selProcessor = new CSTESelectionProcessor();
		CSTE.brushProcessor = new CSTEBrushProcessor();
	}
	
	public static void updateConfig() {
		
		CSTEInfo.debug = config.get("debug", "DebugOutput", false, "Show debug output in log (Default false)").getBoolean(false);
		
		Property colourProp = config.get("general", "SelectionColour", "B00000", "The colour of the selection box in HEX format (Default B00000)");
		colourProp = colourProp.setValidationPattern(Pattern.compile("[0-F]{6}", Pattern.CASE_INSENSITIVE));
		CSTEInfo.selColour = colourProp.getString().toUpperCase();
		
		CSTEInfo.xrayMode = config.get("general", "XrayMode", true, "Show selection through blocks (Default true)").getBoolean(true);
		
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
