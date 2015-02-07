package mmdanggg2.cste.events;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTESelectionProcessor;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MouseEventHandler {

	@SubscribeEvent
	public void handleEvent(MouseEvent event) {
		if (event.button == 1 && event.buttonstate) {
			CSTELogger.logDebug("MOUSE EVENT!!!");
			EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
			if (CSTE.brushProcessor.brush != null && player.getHeldItem() != null) {
				if (player.getCurrentEquippedItem().getItem() == CSTE.brushProcessor.brush) {
					event.setCanceled(true);
					MovingObjectPosition pos = player.rayTrace(100, 0);
					if (pos.typeOfHit.equals(MovingObjectType.BLOCK)) {
						CSTELogger.logDebug("Hit Block: " + CSTESelectionProcessor.posToStr(pos.getBlockPos()));
						CSTE.brushProcessor.onBrushActivated(pos.getBlockPos());
					}
				}
			}
		}
	}
}
