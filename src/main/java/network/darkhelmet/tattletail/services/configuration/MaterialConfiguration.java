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
public class MaterialConfiguration {
    @Comment("The CSS hex color to use for the alert message.")
    private String hexColor;

    @Comment("The materials, choices listed here: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html")
    private List<Material> materials;

    /**
     * Empty constructor for serializer.
     */
    public MaterialConfiguration() {}

    /**
     * Constructor.
     *
     * @param materials The materials
     * @param hexColor The hex color
     */
    public MaterialConfiguration(List<Material> materials, String hexColor) {
        this.materials = materials;
        this.hexColor = hexColor;
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
     * Get the materials.
     *
     * @return The materials
     */
    public List<Material> materials() {
        return materials;
    }
}