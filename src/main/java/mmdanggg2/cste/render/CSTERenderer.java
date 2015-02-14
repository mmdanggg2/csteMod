package mmdanggg2.cste.render;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTEInfo;
import mmdanggg2.cste.selections.SelectionCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CSTERenderer {

	@SubscribeEvent
	public void render(RenderWorldLastEvent event) {
		if (CSTE.selProcessor.hasSelection()) {
			renderSelBox(event);
		}
	}
	
	private void renderSelBox(RenderWorldLastEvent event) {
		SelectionCube sel = CSTE.selProcessor.getSelection();
		Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
		double partialTicks = event.partialTicks;
        double xOff = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
        double yOff = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
        double zOff = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
        
        BlockPos pos1 = sel.getPos1();
        BlockPos pos2 = sel.getPos2();
        AxisAlignedBB selBBox = new AxisAlignedBB(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
        AxisAlignedBB rendBBox = selBBox.offset(-xOff, -yOff, -zOff);
		
		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        if (CSTEInfo.xrayMode) {GlStateManager.disableDepth();}
        
		RenderGlobal.drawOutlinedBoundingBox(rendBBox.offset(0.5, 0.5, 0.5).expand(0.5, 0.5, 0.5), Integer.parseInt(CSTEInfo.selColour, 16));
		
		GlStateManager.enableTexture2D();
		if (CSTEInfo.xrayMode) {GlStateManager.enableDepth();}
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}
}
