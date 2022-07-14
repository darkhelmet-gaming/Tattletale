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

package network.darkhelmet.tattletail;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import network.darkhelmet.tattletail.listeners.BlockBreakListener;
import network.darkhelmet.tattletail.services.configuration.BlockConfiguration;
import network.darkhelmet.tattletail.services.configuration.ConfigurationService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Tattletail extends JavaPlugin {
    /**
     * Cache static instance.
     */
    private static Tattletail instance;

    /**
     * The logger.
     */
    private static final Logger logger = LogManager.getLogger("Tattletail");

    /**
     * The configuration service.
     */
    private ConfigurationService configurationService;

    /**
     * The bukkit audiences.
     */
    private BukkitAudiences adventure;

    /**
     * Cache block alert configs by material.
     */
    private final Map<Material, BlockConfiguration> blockBrokenAlerts = new HashMap<>();

    /**
     * Cached veins.
     */
    private final Map<Location, Long> veins = new HashMap<>();

    /**
     * Get this instance.
     *
     * @return The plugin instance
     */
    public static Tattletail getInstance() {
        return instance;
    }

    /**
     * Constructor.
     */
    public Tattletail() {
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

        // Cache block broke alerts by material
        for (BlockConfiguration blockConfiguration : configurationService.tattletailConfig().blockBreakAlerts()) {
            for (Material material : blockConfiguration.materials()) {
                blockBrokenAlerts.put(material, blockConfiguration);
            }
        }

        if (isEnabled()) {
            // Register listeners
            getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
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
     * Get the block break alert configs.
     *
     * @return The block break alerts
     */
    public Map<Material, BlockConfiguration> blockBrokenAlerts() {
        return blockBrokenAlerts;
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
     * Log a debug message to console.
     *
     * @param message String
     */
    public void debug(String message) {
        if (configurationService.tattletailConfig().debug()) {
            logger.info(message);
        }
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
