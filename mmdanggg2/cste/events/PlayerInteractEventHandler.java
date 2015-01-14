package mmdanggg2.cste.events;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTEProcessor;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerInteractEventHandler {
	
	@SubscribeEvent
	public void handleEvent(PlayerInteractEvent event) {
		if (event.world.isRemote) {
			if (CSTE.processor.wand != null && event.entityPlayer.getHeldItem() != null) {
				if (event.entityPlayer.getCurrentEquippedItem().getItem() == CSTE.processor.wand) {
					if (event.action == event.action.RIGHT_CLICK_BLOCK) {
						event.setCanceled(true);
						CSTE.processor.onBlockActivated(event.pos, event.entityPlayer);
					}
				}
			}
		}
	}
}
