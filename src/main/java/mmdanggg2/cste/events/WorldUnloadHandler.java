package mmdanggg2.cste.events;

import mmdanggg2.cste.util.CSTELogger;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldUnloadHandler {

	@SubscribeEvent
	public void handleEvent(WorldEvent.Unload event) {
		CSTELogger.logDebug("World unload event!");
		if (ChatRecievedHandler.instance.isBuilding()) {
			CSTELogger.logInfo("The world is being unloaded before a command was completed!");
			ChatRecievedHandler.instance.buildingStop();
		}
	}
}
