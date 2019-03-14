package mmdanggg2.cste.events;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTESelectionProcessor;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MouseEventHandler {

	@SubscribeEvent
	public void handleEvent(MouseEvent event) {
		if (event.getButton() == 1 && event.isButtonstate()) {
			CSTELogger.logDebug("MOUSE EVENT!!!");
			EntityPlayerSP player = Minecraft.getMinecraft().player;
			if (CSTE.brushProcessor.brush != null && player.getHeldItemMainhand() != null) {
				if (player.getHeldItemMainhand().getItem() == CSTE.brushProcessor.brush) {
					event.setCanceled(true);
					RayTraceResult pos = player.rayTrace(100, 0);
					if (pos.typeOfHit.equals(RayTraceResult.Type.BLOCK)) {
						CSTELogger.logDebug("Hit Block: " + CSTESelectionProcessor.posToStr(pos.getBlockPos()));
						CSTE.brushProcessor.onBrushActivated(pos.getBlockPos());
					}
				}
			}
		}
	}
}
