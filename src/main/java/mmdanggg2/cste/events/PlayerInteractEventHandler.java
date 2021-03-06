package mmdanggg2.cste.events;

import mmdanggg2.cste.CSTE;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerInteractEventHandler {
	
	@SubscribeEvent
	public void handleEvent(PlayerInteractEvent event) {
		if (event.world.isRemote) {
			if (CSTE.selProcessor.wand != null && event.entityPlayer.getHeldItem() != null) {
				if (event.entityPlayer.getCurrentEquippedItem().getItem() == CSTE.selProcessor.wand) {
					if (event.action == Action.RIGHT_CLICK_BLOCK) {
						event.setCanceled(true);
						CSTE.selProcessor.onBlockActivated(event.pos);
					}
				}
			}
		}
	}
}
