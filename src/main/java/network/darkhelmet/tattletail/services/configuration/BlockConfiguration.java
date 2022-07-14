/*
 * Tattletail
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

package network.darkhelmet.tattletail.services.configuration;

import java.util.List;

import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class BlockConfiguration {
    @Comment("The CSS hex color to use for the alert message.")
    private String hexColor;

    @Comment("Whether to indicate if users have night vision.")
    private boolean includeNightVision = true;

    @Comment("Set the maximum light level that triggers the alert.")
    private int maxLightLevel = 100;

    @Comment("The materials, choices listed here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
    private List<Material> materials;

    @Comment("Limit how many neighboring blocks are scanned.")
    private int maxScanCount;

    @Comment("Set the minimum light level that triggers the alert.")
    private int minLightLevel = 0;

    /**
     * Empty constructor for serializer.
     */
    public BlockConfiguration() {}

    /**
     * Constructor.
     *
     * @param materials The materials
     * @param hexColor The hex color
     * @param maxScanCount The max scan count
     */
    public BlockConfiguration(List<Material> materials, String hexColor, int maxScanCount) {
        this.materials = materials;
        this.hexColor = hexColor;
        this.maxScanCount = maxScanCount;
    }

    /**
     * Get the hex color.
     *
     * @return The hex color
     */
    public String hexColor() {
        return hexColor;
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
     * Get the materials.
     *
     * @return The materials
     */
    public List<Material> materials() {
        return materials;
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
