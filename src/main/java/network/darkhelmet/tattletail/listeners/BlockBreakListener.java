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

package network.darkhelmet.tattletail.listeners;

import java.util.List;
import java.util.Locale;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

import network.darkhelmet.tattletail.Tattletail;
import network.darkhelmet.tattletail.services.configuration.BlockBreakConfiguration;
import network.darkhelmet.tattletail.services.vein.VeinScanner;
import network.darkhelmet.tattletail.utils.BlockUtils;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BlockBreakListener implements Listener {
    /**
     * On block break.
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();

        // Ignore creative
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        // Ignore already-announced vein locations
        if (Tattletail.getInstance().isInVein(event.getBlock().getLocation())) {
            return;
        }

        // Get block alert configuration
        BlockBreakConfiguration blockBreakConfiguration = Tattletail.getInstance()
            .blockBreakAlerts().get(event.getBlock().getType());
        if (blockBreakConfiguration == null) {
            return;
        }

        // Apply light level bounds
        int lightLevel = BlockUtils.getLightLevel(event.getBlock());
        if (lightLevel < blockBreakConfiguration.minLightLevel()
                || lightLevel > blockBreakConfiguration.maxLightLevel()) {
            return;
        }

        // Scan for the vein
        VeinScanner veinScanner = new VeinScanner(
            event.getBlock(), blockBreakConfiguration.materials(), blockBreakConfiguration.maxScanCount());
        List<Location> vein = veinScanner.scan();

        // Cache it
        Tattletail.getInstance().cacheVein(vein);

        TextColor color = TextColor.fromCSSHexString(blockBreakConfiguration.hexColor());
        String blockName = event.getBlock().getType().toString().replace("_", " ")
            .toLowerCase(Locale.ENGLISH).replace("glowing", " ");
        final String count = vein.size() + (vein.size() >= blockBreakConfiguration.maxScanCount() ? "+" : "");

        // Create alert message
        final TextComponent.Builder component = Component.text().color(color)
            .append(Component.text(player.getDisplayName()))
            .append(Component.text(" found "))
            .append(Component.text(count + " "))
            .append(Component.text(blockName))
            .append(Component.text(" in " + lightLevel + "% light"))
            .hoverEvent(
                HoverEvent.hoverEvent(HoverEvent.Action.SHOW_ITEM,
                    HoverEvent.ShowItem.of(Key.key(event.getBlock().getType().getKey().toString()), 1)));

        if (blockBreakConfiguration.includeNightVision()) {
            boolean usingNightVision = false;
            for (PotionEffect effect : player.getActivePotionEffects()) {
                usingNightVision = effect.getType().equals(PotionEffectType.NIGHT_VISION);

                if (usingNightVision) {
                    break;
                }
            }

            if (usingNightVision) {
                component.append(Component.text(" (using night vision)"));
            }
        }

        Tattletail.getInstance().adventure()
            .filter(sender -> sender.hasPermission("tattletail.receivealerts")).sendMessage(component.build());
    }
}
