package mmdanggg2.cste;

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
import mmdanggg2.cste.world.WorldEditor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

public class CSTEBrushProcessor {
	public Item brush = null;
	private int radius = 5;
	private Block brushBlock = Blocks.STONE;
	private int brushMeta = 0;
	private BrushMode brushMode = BrushMode.FILL;

	public void onBrushActivated(BlockPos pos) {
		if (ChatRecievedHandler.instance.isBuilding()) {
			ChatMessenger.addMessageLocalized("cste.commands.error.stillbuilding", TextFormatting.RED);
			return;
		}
		if (radius < 1) {
			ChatMessenger.addMessageLocalized("cste.commands.brush.smallrad", TextFormatting.RED);
		}
		else if (brushMode.equals(BrushMode.SMOOTH)) {
			CSTELogger.logDebug("Brush smooth");
			smoothArea(pos, radius);
		}
		else {
			if (brushBlock == null) {
				ChatMessenger.addMessageLocalized("cste.commands.brush.noblock", TextFormatting.RED);
			}
			else {
				if (brushMode.equals(BrushMode.FILL)) {
					CSTELogger.logDebug("Brush fill");
					makeSphere(pos, brushBlock, brushMeta, radius, true);
				}
				else if (brushMode.equals(BrushMode.HOLLOWFILL)) {
					CSTELogger.logDebug("Brush hollow fill");
					makeSphere(pos, brushBlock, brushMeta, radius, false);
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
				String modeStr = StringUtils.join(new String[] {brushMode.getName(), Integer.toString(radius), new ItemStack(brushBlock, 1, brushMeta).getDisplayName()}, ", ");
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
					ChatMessenger.addMessageLocalized("cste.commands.brushmode.badmode", TextFormatting.RED);
					return;
				}
			}
			if (args.length >= 2) {
				if (StringUtils.isNumeric(args[1])) {
					int rad = Integer.parseInt(args[1]);
					if (rad <= 0) {
						ChatMessenger.addMessageLocalized("cste.commands.brushmode.badrad", TextFormatting.RED);
						return;
					}
					else if (rad > 100) {
						ChatMessenger.addMessageLocalized("cste.commands.brushmode.radtoolarge", TextFormatting.RED, Minecraft.getMinecraft().player.getName());
						return;
					}
					else {
						if (rad > 40) {
							ChatMessenger.addMessageLocalized("cste.commands.brushmode.vlargerad", TextFormatting.RED);
						}
						else if (rad > 20) {
							ChatMessenger.addMessageLocalized("cste.commands.brushmode.largerad", TextFormatting.RED);
						}
						radius = rad;
					}
				}
				else {
					ChatMessenger.addMessageLocalized("cste.commands.brushmode.badrad", TextFormatting.RED);
					return;
				}
			}
			if (args.length >= 3 && !brushMode.equals(BrushMode.SMOOTH)) {
				Block block;
				try {
					block = CommandBase.getBlockByText(null, args[2]);
				} catch (NumberInvalidException e) {
					ChatMessenger.addMessageLocalized(e.getMessage(), TextFormatting.RED);
					return;
				}
				Integer meta = null;
				if (args.length == 4) {
					if (StringUtils.isNumeric(args[3])) {
						meta = Integer.parseInt(args[3]);
						if (meta > 15) {
							meta = 0;
						}
					}
					else {
						meta = 0;
					}
				}
				else {
					meta = 0;
				}
				brushBlock = block;
				brushMeta = meta;
			}
			if (brushMode.equals(BrushMode.SMOOTH)) {
				String modeStr = StringUtils.join(new String[] {brushMode.getName(), Integer.toString(radius)}, ", ");
				ChatMessenger.addMessageLocalized("cste.commands.brushmode.success", modeStr);
			}
			else {
				String blockName;
				try {
					blockName = new ItemStack(brushBlock, 1, brushMeta).getDisplayName();
				}
				catch (NullPointerException e) {
					blockName = I18n.format(brushBlock.getLocalizedName());
				}
				String modeStr = StringUtils.join(new String[] {brushMode.getName(), Integer.toString(radius), blockName}, ", ");
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
    private void makeSphere(BlockPos pos, Block block, int meta, double radius, boolean filled) {
    	
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

                    WorldEditor.setBlock(pos.add(x, y, z), block, meta);
                    WorldEditor.setBlock(pos.add(-x, y, z), block, meta);
                    WorldEditor.setBlock(pos.add(x, -y, z), block, meta);
                    WorldEditor.setBlock(pos.add(x, y, -z), block, meta);
                    WorldEditor.setBlock(pos.add(-x, -y, z), block, meta);
                    WorldEditor.setBlock(pos.add(x, -y, -z), block, meta);
                    WorldEditor.setBlock(pos.add(-x, y, -z), block, meta);
                    WorldEditor.setBlock(pos.add(-x, -y, -z), block, meta);
                }
            }
        }
        
        WorldEditor.sendCommands();
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
		for (BlockDelta bd : changed) {
			if (bd.isChangedFromCurrent()){
				WorldEditor.setBlock(bd.getPos(), bd.getNewBlock(), bd.getNewMeta());
			}
		}
		
		WorldEditor.sendCommands();
	}

	private void setBrushMode(BrushMode mode) {
		brushMode = mode;
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
