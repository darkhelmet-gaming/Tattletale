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

package network.darkhelmet.tattletale;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import network.darkhelmet.tattletale.listeners.BlockBreakListener;
import network.darkhelmet.tattletale.listeners.BlockIgniteListener;
import network.darkhelmet.tattletale.listeners.BlockPlaceListener;
import network.darkhelmet.tattletale.listeners.PlayerBucketEmptyListener;
import network.darkhelmet.tattletale.services.configuration.BlockBreakConfiguration;
import network.darkhelmet.tattletale.services.configuration.ConfigurationService;
import network.darkhelmet.tattletale.services.configuration.MaterialConfiguration;
import network.darkhelmet.tattletale.services.configuration.TattletaleConfiguration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Tattletale extends JavaPlugin {
    /**
     * Cache static instance.
     */
    private static Tattletale instance;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger("Tattletale");

    /**
     * The configuration service.
     */
    private ConfigurationService configurationService;

    /**
     * The bukkit audiences.
     */
    private BukkitAudiences adventure;

    /**
     * Cache block break alert configs by material.
     */
    private final Map<Material, BlockBreakConfiguration> blockBreakAlerts = new HashMap<>();

    /**
     * Cache block place alert configs by material.
     */
    private final Map<Material, MaterialConfiguration> blockPlaceAlerts = new HashMap<>();

    /**
     * Cache bucket empty alert configs by material.
     */
    private final Map<Material, MaterialConfiguration> bucketEmptyAlerts = new HashMap<>();

    /**
     * Cached veins.
     */
    private final Map<Location, Long> veins = new HashMap<>();

    /**
     * Get this instance.
     *
     * @return The plugin instance
     */
    public static Tattletale getInstance() {
        return instance;
    }

    /**
     * Constructor.
     */
    public Tattletale() {
        instance = this;
    }

    /**
     * On enable.
     */
    @Override
    public void onEnable() {
        String pluginName = this.getDescription().getName();
        String pluginVersion = this.getDescription().getVersion();
        logger.info("Initializing {} {} by viveleroi", pluginName, pluginVersion);

        // Load the configuration service (and files)
        configurationService = new ConfigurationService(getDataFolder().toPath());

        this.adventure = BukkitAudiences.create(this);

        // Cache block break alerts by material
        for (BlockBreakConfiguration blockBreakConfiguration :
                configurationService.tattletaleConfig().blockBreakAlerts()) {
            for (Material material : blockBreakConfiguration.materials()) {
                blockBreakAlerts.put(material, blockBreakConfiguration);
            }
        }

        // Cache block place alerts by material
        for (MaterialConfiguration materialConfiguration :
                configurationService.tattletaleConfig().blockPlaceAlerts()) {
            for (Material material : materialConfiguration.materials()) {
                blockPlaceAlerts.put(material, materialConfiguration);
            }
        }

        // Cache bucket empty alerts by material
        for (MaterialConfiguration materialConfiguration :
                configurationService.tattletaleConfig().bucketEmptyAlerts()) {
            for (Material material : materialConfiguration.materials()) {
                bucketEmptyAlerts.put(material, materialConfiguration);
            }
        }

        if (isEnabled()) {
            // Register listeners
            getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
            getServer().getPluginManager().registerEvents(new BlockIgniteListener(), this);
            getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
            getServer().getPluginManager().registerEvents(new PlayerBucketEmptyListener(), this);
        }
    }

    /**
     * Get the adventure bukkit audience.
     *
     * @return The bukkit audience
     */
    public BukkitAudiences adventure() {
        return adventure;
    }

    /**
     * Send an alert to appropriate players.
     *
     * @param cause The causing player
     * @param message The message
     */
    public void alert(Player cause, Component message) {
        adventure.filter(sender -> {
            if (!sender.hasPermission("tattletale.receivealerts")) {
                return false;
            }

            return configuration().ignoreSelf() || !sender.equals(cause);
        }).sendMessage(message);

        if (configuration().logAlerts()) {
            logger.info(PlainTextComponentSerializer.plainText().serialize(message));
        }
    }

    /**
     * Get the block break alert configs.
     *
     * @return The block break alerts
     */
    public Map<Material, BlockBreakConfiguration> blockBreakAlerts() {
        return blockBreakAlerts;
    }

    /**
     * Get the block place alert configs.
     *
     * @return The block place alerts
     */
    public Map<Material, MaterialConfiguration> blockPlaceAlerts() {
        return blockPlaceAlerts;
    }

    /**
     * Get the bucket empty alert configs.
     *
     * @return The bucket empty alert configs
     */
    public Map<Material, MaterialConfiguration> bucketEmptyAlerts() {
        return bucketEmptyAlerts;
    }

    /**
     * Get the configuration.
     *
     * @return The configs
     */
    public TattletaleConfiguration configuration() {
        return configurationService.tattletaleConfig();
    }

    /**
     * Cache vein locations.
     *
     * @param locations The locations
     */
    public void cacheVein(List<Location> locations) {
        final Date date = new Date();
        for (Location loc : locations) {
            veins.put(loc, date.getTime());
        }
    }

    /**
     * Get if location is in cache.
     *
     * @param location The location
     * @return True if location scanned
     */
    public boolean isInVein(Location location) {
        return veins.containsKey(location);
    }

    /**
     * Handle exceptions.
     *
     * @param ex The exception
     */
    public void handleException(Exception ex) {
        logger.error(ex.getMessage(), ex);
    }
}
