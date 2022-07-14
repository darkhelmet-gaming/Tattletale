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

import java.io.File;
import java.nio.file.Path;

import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.ConfigurationLoader;

public class ConfigurationService {
    /**
     * The plugin data path.
     */
    private final Path dataPath;

    /**
     * The primary plugin configuration.
     */
    private TattletaleConfiguration tattletaleConfiguration;

    /**
     * Construct the configuration service.
     *
     * @param dataPath The plugin datapath
     */
    public ConfigurationService(Path dataPath) {
        this.dataPath = dataPath;

        loadConfigurations();
    }

    /**
     * Get the prism configuration.
     *
     * @return The prism configuration
     */
    public TattletaleConfiguration tattletaleConfig() {
        return tattletaleConfiguration;
    }

    /**
     * Load the configurations.
     */
    public void loadConfigurations() {
        // Load the main config
        File prismConfigFile = new File(dataPath.toFile(), "tattletale.conf");
        tattletaleConfiguration = getOrWriteConfiguration(TattletaleConfiguration.class, prismConfigFile);
    }

    /**
     * Build a hocon configuration loader.
     *
     * @param file The config file
     * @return The config loader
     */
    public ConfigurationLoader<?> configurationLoader(final Path file) {
        return HoconConfigurationLoader.builder()
            .prettyPrinting(true)
            .path(file)
            .build();
    }

    /**
     * Get or create a configuration file.
     *
     * @param clz The configuration class.
     * @param file The file path we'll read/write to.
     * @param <T> The configuration class type.
     * @return The configuration class instance
     */
    public <T> T getOrWriteConfiguration(Class<T> clz, File file) {
        return getOrWriteConfiguration(clz, file, null);
    }

    /**
     * Get or create a configuration file.
     *
     * @param clz The configuration class
     * @param file The file path we'll read/write to
     * @param config The existing config object to write
     * @param <T> The configuration class type
     * @return The configuration class instance
     */
    public <T> T getOrWriteConfiguration(Class<T> clz, File file, T config) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        final ConfigurationLoader loader = configurationLoader(file.toPath());

        try {
            final ConfigurationNode root = loader.load();

            // If config is not provided, load it
            if (config == null) {
                config = root.get(clz);
            }

            root.set(clz, config);
            loader.save(root);

            return config;
        } catch (final ConfigurateException e) {
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        }

        return null;
    }
}
