package mmdanggg2.cste;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.sk89q.worldedit.math.convolution.GaussianKernel;
import com.sk89q.worldedit.math.convolution.HeightMap;
import com.sk89q.worldedit.math.convolution.HeightMapFilter;

import mmdanggg2.cste.events.ChatRecievedHandler;
import mmdanggg2.cste.selections.SelectionCube;
import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.util.CSTELogger;
import mmdanggg2.cste.util.ChatMessenger;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

public class CSTEBrushProcessor {
	public Item brush = null;
	private int radius = 5;
	private String block = "stone"; //String containing the block name and data tags
	private BrushMode brushMode = BrushMode.FILL;
	private List<String> commands = new ArrayList<String>();

	public void onBrushActivated(BlockPos pos) {
		if (radius < 1) {
			ChatMessenger.addMessageLocalized("cste.commands.brush.smallrad", EnumChatFormatting.RED);
		}
		else if (brushMode.equals(BrushMode.SMOOTH)) {
			CSTELogger.logDebug("Brush smooth");
			smoothArea(pos, radius);
		}
		else {
			if (block.isEmpty()) {
				ChatMessenger.addMessageLocalized("cste.commands.brush.noblock", EnumChatFormatting.RED);
			}
			else {
				if (brushMode.equals(BrushMode.FILL)) {
					CSTELogger.logDebug("Brush fill");
					makeSphere(pos, block, radius, true);
				}
				else if (brushMode.equals(BrushMode.HOLLOWFILL)) {
					CSTELogger.logDebug("Brush hollow fill");
					makeSphere(pos, block, radius, false);
				}
			}
		}
	}

	public void onModeCommand(String[] args) {
		if (args.length == 0) {
			if (brushMode.equals(BrushMode.SMOOTH)) {
				String modeStr = StringUtils.join(new String[] {brushMode.getName(), Integer.toString(radius)}, ", ");
				ChatMessenger.addMessageLocalized("cste.commands.brushmode.info", modeStr);
			}
			else {
				String modeStr = StringUtils.join(new String[] {brushMode.getName(), Integer.toString(radius), block}, ", ");
				ChatMessenger.addMessageLocalized("cste.commands.brushmode.info", modeStr);
			}
		}
		else {
			if (args.length >= 1) {
				boolean modeFound = false;
				for (BrushMode mode : BrushMode.values()) {
					CSTELogger.logDebug("Checking " + mode.name );
					if (args[0].equalsIgnoreCase(mode.getName())) {
						CSTELogger.logDebug("Match, setting mode.");
						setBrushMode(mode);
						modeFound = true;
					}
				}
				if (!modeFound) {
					CSTELogger.logDebug("No match found!");
					ChatMessenger.addMessageLocalized("cste.commands.brushmode.badmode", EnumChatFormatting.RED);
					return;
				}
			}
			if (args.length >= 2) {
				if (StringUtils.isNumeric(args[1])) {
					int rad = Integer.parseInt(args[1]);
					if (rad <= 0) {
						ChatMessenger.addMessageLocalized("cste.commands.brushmode.badrad", EnumChatFormatting.RED);
						return;
					}
					else if (rad > 100) {
						ChatMessenger.addMessageLocalized("cste.commands.brushmode.radtoolarge", EnumChatFormatting.RED, Minecraft.getMinecraft().thePlayer.getName());
						return;
					}
					else {
						if (rad > 40) {
							ChatMessenger.addMessageLocalized("cste.commands.brushmode.vlargerad", EnumChatFormatting.RED);
						}
						else if (rad > 20) {
							ChatMessenger.addMessageLocalized("cste.commands.brushmode.largerad", EnumChatFormatting.RED);
						}
						radius = rad;
					}
				}
				else {
					ChatMessenger.addMessageLocalized("cste.commands.brushmode.badrad", EnumChatFormatting.RED);
					return;
				}
			}
			if (args.length >= 3 && !brushMode.equals(BrushMode.SMOOTH)) {
				block = StringUtils.join(Arrays.copyOfRange(args, 2, args.length), " ");
			}
			if (brushMode.equals(BrushMode.SMOOTH)) {
				String modeStr = StringUtils.join(new String[] {brushMode.getName(), Integer.toString(radius)}, ", ");
				ChatMessenger.addMessageLocalized("cste.commands.brushmode.success", modeStr);
			}
			else {
				String modeStr = StringUtils.join(new String[] {brushMode.getName(), Integer.toString(radius), block}, ", ");
				ChatMessenger.addMessageLocalized("cste.commands.brushmode.success", modeStr);
			}
		}
	}
	
	/**
     * Makes a sphere or ellipsoid.
     * 
     * Modified from WorldEdit
     * 
	 * @param pos Center of the sphere
     * @param block the Block and data string
     * @param radius the radius
     * @param filled If false, only a shell will be generated.
     */
    private void makeSphere(BlockPos pos, String block, double radius, boolean filled) {
    	
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
        
        sendCommands();
    }

	/**
	 * Smooths an area
	 * 
	 * Modified from WorldEdit
	 * 
	 * @param pos
	 * @param radius
	 */
	private void smoothArea(BlockPos pos, int radius) {
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
			if (bd.isChangedFromCurrent()){
				ResourceLocation blockName = (ResourceLocation) Block.blockRegistry.getNameForObject(bd.getNewBlock());
				//CSTELogger.logDebug("Block change: " + bd.getPos().toString() + ", " + blockName);
				setBlockComp(bd.getPos(), blockName.toString() + " " + bd.getNewMeta());
			}
		}
		
		sendCommands();
	}

	private void setBrushMode(BrushMode mode) {
		brushMode = mode;
	}
	
	private void setBlockComp(BlockPos pos, String block) {
		if (256 > pos.getY() && pos.getY() >= 0) {
			commands.add("/setblock " + CSTESelectionProcessor.posToStr(pos) + " " + block);
		}
	}
	
	private void sendCommands() {
		ChatRecievedHandler.instance.buildingStart(commands.size());
		for (String command : commands) {
			ChatMessenger.sendMessage(command);
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
