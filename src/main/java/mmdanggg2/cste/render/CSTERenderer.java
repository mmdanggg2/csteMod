package mmdanggg2.cste.render;

import mmdanggg2.cste.CSTE;
import mmdanggg2.cste.CSTEInfo;
import mmdanggg2.cste.selections.SelectionCube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
        EntityPlayer player = mc.player;
		double partialTicks = event.getPartialTicks();
        double xOff = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTicks;
        double yOff = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTicks;
        double zOff = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTicks;
        
        BlockPos pos1 = sel.getPos1();
        BlockPos pos2 = sel.getPos2();
        if (pos2 == null && pos1 != null) {
        	pos2 = pos1;
        }
        else if (pos1 == null && pos2 != null) {
        	pos1 = pos2;
        }
        AxisAlignedBB selBBox = new AxisAlignedBB(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
        AxisAlignedBB rendBBox = selBBox.offset(-xOff, -yOff, -zOff);
		

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(5.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        if (CSTEInfo.xrayMode) {GlStateManager.disableDepth();}
        
        
        int colour = Integer.parseInt(CSTEInfo.selColour, 16);
        int red = (colour>>16)&0x0ff;
        int green=(colour>>8) &0x0ff;
        int blue= (colour)    &0x0ff;
		RenderGlobal.drawSelectionBoundingBox(rendBBox.expand(1, 1, 1), red/255f, green/255f, blue/255f, 1.0f);

		if (CSTEInfo.xrayMode) {GlStateManager.enableDepth();}
		GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
}
