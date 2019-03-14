package mmdanggg2.cste.events;

import mmdanggg2.cste.CSTE;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerInteractEventHandler {
	
	@SubscribeEvent
	public void handleEvent(PlayerInteractEvent.RightClickBlock event) {
		if (event.getSide() == Side.CLIENT) {
			if (CSTE.selProcessor.wand != null && !event.getItemStack().isEmpty()) {
				if (event.getItemStack().getItem() == CSTE.selProcessor.wand) {
					event.setCanceled(true);
					CSTE.selProcessor.onBlockActivated(event.getPos());
				}
			}
		}
	}
}
