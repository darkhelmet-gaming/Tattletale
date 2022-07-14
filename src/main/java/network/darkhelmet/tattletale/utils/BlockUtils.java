/*
 * Tattletale
 *
 * Copyright (c) 2022 M Botsko (viveleroi)
 *                    Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package network.darkhelmet.tattletale.utils;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockUtils {
    private BlockUtils() {}

    /**
     * Get light level.
     *
     * @param block Block
     * @return int
     */
    public static int getLightLevel(Block block) {
        int light = 0;
        final BlockFace[] blockFaces =
            new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST,
                BlockFace.UP, BlockFace.DOWN};
        for (BlockFace blockFace : blockFaces) {
            light = Math.max(light, block.getRelative(blockFace).getLightLevel());
            if (light >= 15) {
                break;
            }
        }

        return light * 100 / 15;
    }
}
