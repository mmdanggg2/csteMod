package mmdanggg2.cste;

import java.util.ArrayList;
import java.util.List;

import com.sk89q.worldedit.math.convolution.GaussianKernel;
import com.sk89q.worldedit.math.convolution.HeightMap;
import com.sk89q.worldedit.math.convolution.HeightMapFilter;

import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.selections.SelectionCube;
import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.util.CSTELogger;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;

public class CSTEBrushProcessor {
	public Item brush = null;
	private int radius = 0;
	private String block = ""; //String containing the block name and data tags
	private BrushMode brushMode = BrushMode.FILL;
	private List<String> commands = new ArrayList<String>();

	public void onBrushActivated(EntityPlayerSP player, BlockPos pos) {
		if (brushMode.equals(BrushMode.FILL)) {
			CSTELogger.logDebug("Brush fill");
			makeSphere(player, pos, block, radius, true);
		}
		else if (brushMode.equals(BrushMode.HOLLOWFILL)) {
			CSTELogger.logDebug("Brush hollow fill");
			makeSphere(player, pos, block, radius, false);
		}
		else if (brushMode.equals(BrushMode.SMOOTH)) {
			CSTELogger.logDebug("Brush smooth");
			smoothArea(player, pos, block, radius);
		}
	}

	public void onModeCommand(EntityPlayer player, String[] args) {
		for (BrushMode mode : BrushMode.values()) {
			CSTELogger.logDebug("Checking " + mode.name );
			if (args[0].equalsIgnoreCase(mode.getName())) {
				CSTELogger.logDebug("Match, setting mode.");
				setBrushMode(mode);
				player.addChatMessage(new ChatComponentTranslation("cste.commands.brushmode.success", args[0]));
				return;
			}
		}
		CSTELogger.logDebug("No match found!");
		player.addChatMessage(new ChatComponentTranslation("cste.commands.brushmode.badarg"));
	}
	
	/**
     * Makes a sphere or ellipsoid.
     * 
     * Modified from WorldEdit
     * 
	 * @param player the player to send the commands through
     * @param pos Center of the sphere
     * @param block the Block and data string
     * @param radius the radius
     * @param filled If false, only a shell will be generated.
     */
    public void makeSphere(EntityPlayerSP player, BlockPos pos, String block, double radius, boolean filled) {
    	
    	commands.clear();
    	
        radius += 0.5;
        
        final double invRadius = 1 / radius;
        
        final int ceilRadius = (int) Math.ceil(radius);

        double nextXn = 0;
        forX: for (int x = 0; x <= ceilRadius; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadius;
            double nextYn = 0;
            forY: for (int y = 0; y <= ceilRadius; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadius;
                double nextZn = 0;
                forZ: for (int z = 0; z <= ceilRadius; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadius;

                    double distanceSq = lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }

                    if (!filled) {
                        if (lengthSq(nextXn, yn, zn) <= 1 && lengthSq(xn, nextYn, zn) <= 1 && lengthSq(xn, yn, nextZn) <= 1) {
                            continue;
                        }
                    }

                    setBlockComp(pos.add(x, y, z), block);
                    setBlockComp(pos.add(-x, y, z), block);
                    setBlockComp(pos.add(x, -y, z), block);
                    setBlockComp(pos.add(x, y, -z), block);
                    setBlockComp(pos.add(-x, -y, z), block);
                    setBlockComp(pos.add(x, -y, -z), block);
                    setBlockComp(pos.add(-x, y, -z), block);
                    setBlockComp(pos.add(-x, -y, -z), block);
                }
            }
        }
        
        sendCommands(player);
    }

	private void smoothArea(EntityPlayerSP player, BlockPos pos, String block, int radius) {
		BlockPos min = new BlockPos(pos.add(-radius, -radius, -radius));
		BlockPos max = pos.add(radius, radius + 10, radius);
		CSTELogger.logDebug("posMin = " + min + ", posMax = " + max);
		SelectionCube selection = new SelectionCube(min, max);
		HeightMap heightMap = new HeightMap(selection);
		HeightMapFilter filter = new HeightMapFilter(new GaussianKernel(5, 1.0));
		List<BlockDelta> changed = heightMap.applyFilter(filter, 4);
		
		CSTELogger.logDebug("Blocks changing: " + changed.size());
		
    	commands.clear();
		for (BlockDelta bd : changed) {
			if (bd.isChanged()){
				ResourceLocation blockName = (ResourceLocation) Block.blockRegistry.getNameForObject(bd.getBlock());
				//CSTELogger.logDebug("Block change: " + bd.getPos().toString() + ", " + blockName);
				setBlockComp(bd.getPos(), blockName.toString());
			}
		}
		
		sendCommands(player);
	}

	private void setBrushMode(BrushMode mode) {
		brushMode = mode;
	}
	
	private void setBlockComp(BlockPos pos, String block) {
		commands.add("/setblock " + CSTESelectionProcessor.posToStr(pos) + " " + block);
	}
	
	private void sendCommands(EntityPlayerSP player) {
		ChatRecievedHandler.instance.buildingStart(commands.size(), player);
		for (String command : commands) {
			player.sendChatMessage(command);
		}
		commands.clear();
	}
	
	private static double lengthSq(double x, double y, double z) {
		return (x * x) + (y * y) + (z * z);
	}
	
	private enum BrushMode {
		FILL("fill"), HOLLOWFILL("hollow"), SMOOTH("smooth");
		
		private String name;

		private BrushMode(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}
}