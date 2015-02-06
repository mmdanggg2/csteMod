/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.math.convolution;

import java.util.ArrayList;
import java.util.List;
import mmdanggg2.cste.selections.SelectionCube;
import mmdanggg2.cste.util.BlockDelta;
import mmdanggg2.cste.world.WorldReader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.BlockPos;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Allows applications of Kernels onto the region's height map.
 *
 * <p>Currently only used for smoothing (with a GaussianKernel)</p>.
 */
public class HeightMap {

    private int[] data;
    private int width;
    private int height;

    private SelectionCube selection;

    /**
     * Constructs the HeightMap
     *
     * @param session an edit session
     * @param selection the region
     * @param naturalOnly ignore non-natural blocks
     */
    public HeightMap(SelectionCube sel) {
        checkNotNull(sel);
        
        this.selection = sel;
        
        this.width = selection.getWidth();
        this.height = selection.getDepth();

        int minX = selection.getSmallestCoord().getX();
        int minY = selection.getSmallestCoord().getY();
        int minZ = selection.getSmallestCoord().getZ();
        int maxY = selection.getLargestCoord().getY();

        // Store current heightmap data
        data = new int[width * height];
        for (int z = 0; z < height; ++z) {
            for (int x = 0; x < width; ++x) {
                data[z * width + x] = WorldReader.getHighestTerrainBlock(x + minX, z + minZ, minY, maxY);
            }
        }
    }

    /**
     * Apply the filter 'iterations' amount times.
     * 
     * @param filter the filter
     * @param iterations the number of iterations
     * @return number of blocks affected
     * @throws MaxChangedBlocksException
     */

    public List<BlockDelta> applyFilter(HeightMapFilter filter, int iterations) {
        checkNotNull(filter);

        int[] newData = new int[data.length];
        System.arraycopy(data, 0, newData, 0, data.length);

        for (int i = 0; i < iterations; ++i) {
            newData = filter.filter(newData, width, height);
        }

        return apply(newData);
    }

    /**
     * Apply a raw heightmap to the region
     * 
     * @param data the data
     * @return number of blocks affected
     * @throws MaxChangedBlocksException
     */

    public List<BlockDelta> apply(int[] data) {
        checkNotNull(data);

        BlockPos minY = selection.getSmallestCoord();
        int originX = minY.getX();
        int originY = minY.getY();
        int originZ = minY.getZ();

        int maxY = selection.getLargestCoord().getY();

        List<BlockDelta> blocksChanged = new ArrayList<BlockDelta>();

        // Apply heightmap
        for (int z = 0; z < height; ++z) {
            for (int x = 0; x < width; ++x) {
                int index = z * width + x;
                int curHeight = this.data[index];

                // Clamp newHeight within the selection area
                int newHeight = Math.min(maxY, data[index]);

                // Offset x,z to be 'real' coordinates
                int xr = x + originX;
                int zr = z + originZ;

                // We are keeping the topmost blocks so take that in account for the scale
                double scale = (double) (curHeight - originY) / (double) (newHeight - originY);

                // Depending on growing or shrinking we need to start at the bottom or top
                if (newHeight > curHeight) {
                    // Set the top block of the column to be the same type (this might go wrong with rounding)
                    Block existing = WorldReader.getBlock(new BlockPos(xr, curHeight, zr));

                    // Skip water/lava
                    if (!(existing instanceof BlockLiquid)) {
                        addBlock(new BlockPos(xr, newHeight, zr), existing, blocksChanged);

                        // Grow -- start from 1 below top replacing airblocks
                        for (int y = newHeight - 1 - originY; y >= 0; --y) {
                            int copyFrom = (int) (y * scale);
                            addBlock(new BlockPos(xr, originY + y, zr), WorldReader.getBlock(new BlockPos(xr, originY + copyFrom, zr)), blocksChanged);
                        }
                    }
                } else if (curHeight > newHeight) {
                    // Shrink -- start from bottom
                    for (int y = 0; y < newHeight - originY; ++y) {
                        int copyFrom = (int) (y * scale);
                        addBlock(new BlockPos(xr, originY + y, zr), WorldReader.getBlock(new BlockPos(xr, originY + copyFrom, zr)), blocksChanged);
                    }

                    // Set the top block of the column to be the same type
                    // (this could otherwise go wrong with rounding)
                    addBlock(new BlockPos(xr, newHeight, zr), WorldReader.getBlock(new BlockPos(xr, curHeight, zr)), blocksChanged);

                    // Fill rest with air
                    for (int y = newHeight + 1; y <= curHeight; ++y) {
                        addBlock(new BlockPos(xr, y, zr), Block.getBlockFromName("air"), blocksChanged);
                    }
                }
            }
        }

        // Drop trees to the floor -- TODO

        return blocksChanged;
    }

	private void addBlock(BlockPos blockPos, Block block, List<BlockDelta> blocksChanged) {
		blocksChanged.add(new BlockDelta(blockPos, block));
	}

}