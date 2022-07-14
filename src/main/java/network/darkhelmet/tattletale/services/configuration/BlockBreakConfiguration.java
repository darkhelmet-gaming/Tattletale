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

package network.darkhelmet.tattletale.services.configuration;

import java.util.List;

import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class BlockBreakConfiguration extends MaterialConfiguration {
    @Comment("Whether to indicate if users have night vision.")
    private final boolean includeNightVision = true;

    @Comment("Set the maximum light level that triggers the alert.")
    private final int maxLightLevel = 100;

    @Comment("Limit how many neighboring blocks are scanned.")
    private int maxScanCount;

    @Comment("Set the minimum light level that triggers the alert.")
    private final int minLightLevel = 0;

    /**
     * Empty constructor for serializer.
     */
    public BlockBreakConfiguration() {}

    /**
     * Constructor.
     *
     * @param materials The materials
     * @param hexColor The hex color
     * @param maxScanCount The max scan count
     */
    public BlockBreakConfiguration(List<Material> materials, String hexColor, int maxScanCount) {
        super(materials, hexColor);

        this.maxScanCount = maxScanCount;
    }

    /**
     * Get the toggle to include night vision.
     *
     * @return Whether to include night vision in alerts
     */
    public boolean includeNightVision() {
        return includeNightVision;
    }

    /**
     * Get the max light level.
     *
     * @return The max light level
     */
    public int maxLightLevel() {
        return maxLightLevel;
    }

    /**
     * Get the max scan count.
     *
     * @return The max scan count
     */
    public int maxScanCount() {
        return maxScanCount;
    }

    /**
     * Get the min light level.
     *
     * @return The min light level
     */
    public int minLightLevel() {
        return minLightLevel;
    }
}
