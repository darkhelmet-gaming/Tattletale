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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class TattletailConfiguration {
    @Comment("Configure alerts for block break.")
    private List<BlockBreakConfiguration> blockBreakAlerts = new ArrayList<>();

    @Comment("Configure alerts for block placement.")
    private List<MaterialConfiguration> blockPlaceAlerts = new ArrayList<>();

    @Comment("Configure alerts for emptying buckets.")
    private List<MaterialConfiguration> bucketEmptyAlerts = new ArrayList<>();

    @Comment("Enable plugin debug mode. Produces extra logging to help diagnose issues.")
    private boolean debug = false;

    @Comment("Alert on lighter use.")
    private AlertConfiguration lighterUseAlert = new AlertConfiguration("#cfcfcf");

    /**
     * Constructor.
     */
    public TattletailConfiguration() {
        blockBreakAlerts.add(new BlockBreakConfiguration(Arrays.asList(Material.ANCIENT_DEBRIS), "#aa00aa", 20));

        List<Material> copperOres = Arrays.asList(Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE);
        blockBreakAlerts.add(new BlockBreakConfiguration(copperOres, "#c1765a", 150));

        List<Material> diamondOres = Arrays.asList(Material.DIAMOND_ORE, Material.DEEPSLATE_DIAMOND_ORE);
        blockBreakAlerts.add(new BlockBreakConfiguration(diamondOres, "#04babd", 20));

        List<Material> emeraldOres = Arrays.asList(Material.EMERALD_ORE, Material.DEEPSLATE_EMERALD_ORE);
        blockBreakAlerts.add(new BlockBreakConfiguration(emeraldOres, "#21bf60", 20));

        List<Material> goldOres = Arrays.asList(Material.GOLD_ORE, Material.DEEPSLATE_GOLD_ORE);
        blockBreakAlerts.add(new BlockBreakConfiguration(goldOres, "#ffe17d", 20));

        List<Material> ironOres = Arrays.asList(Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE);
        blockBreakAlerts.add(new BlockBreakConfiguration(ironOres, "#d6d6d6", 150));

        List<Material> lapisOres = Arrays.asList(Material.LAPIS_ORE, Material.DEEPSLATE_LAPIS_ORE);
        blockBreakAlerts.add(new BlockBreakConfiguration(lapisOres, "#0670cc", 20));

        blockBreakAlerts.add(new BlockBreakConfiguration(Arrays.asList(Material.NETHER_GOLD_ORE), "#ff7308", 20));

        blockPlaceAlerts.add(new MaterialConfiguration(Arrays.asList(Material.TNT, Material.BEDROCK), "#ffffff"));

        bucketEmptyAlerts.add(new MaterialConfiguration(Arrays.asList(Material.LAVA_BUCKET), "#ffffff"));
    }

    /**
     * Get the block break alerts.
     *
     * @return The block break alerts
     */
    public List<BlockBreakConfiguration> blockBreakAlerts() {
        return blockBreakAlerts;
    }

    /**
     * Get the block place alerts.
     *
     * @return The block place alerts
     */
    public List<MaterialConfiguration> blockPlaceAlerts() {
        return blockPlaceAlerts;
    }

    /**
     * Get the bucket empty alerts.
     *
     * @return The bucket empty alerts
     */
    public List<MaterialConfiguration> bucketEmptyAlerts() {
        return bucketEmptyAlerts;
    }

    /**
     * Get the lighter use alert config.
     *
     * @return The lighter use alert config.
     */
    public AlertConfiguration lighterUseAlert() {
        return lighterUseAlert;
    }

    /**
     * Get the debug setting.
     *
     * @return True if debug enabled.
     */
    public boolean debug() {
        return debug;
    }
}