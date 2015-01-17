package mmdanggg2.cste.render;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTEInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RenderSelection {

	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		if (CSTE.processor.hasSelection()) {
			BlockPos[] sel = CSTE.processor.getSelection();
			Tessellator tess = Tessellator.getInstance();
			WorldRenderer wr = tess.getWorldRenderer();
			Minecraft mc = Minecraft.getMinecraft();
            EntityPlayerSP player = mc.thePlayer;
			double partialTicks = event.partialTicks;
            double xOff = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
            double yOff = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
            double zOff = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
            
            AxisAlignedBB selBBox = new AxisAlignedBB(sel[0].getX(), sel[0].getY(), sel[0].getZ(), sel[1].getX(), sel[1].getY(), sel[1].getZ());
            AxisAlignedBB rendBBox = selBBox.offset(-xOff, -yOff, -zOff);
			
			GlStateManager.pushAttrib();
			GlStateManager.pushMatrix();
	        GlStateManager.disableTexture2D();
	        if (CSTEInfo.xrayMode) {GlStateManager.disableDepth();}
	        
			event.context.drawOutlinedBoundingBox(rendBBox.offset(0.5, 0.5, 0.5).expand(0.5, 0.5, 0.5), Integer.parseInt(CSTEInfo.selColour, 16));
			
			GlStateManager.enableTexture2D();
			if (CSTEInfo.xrayMode) {GlStateManager.enableDepth();}
			GlStateManager.popMatrix();
			GlStateManager.popAttrib();
		}
	}
}
