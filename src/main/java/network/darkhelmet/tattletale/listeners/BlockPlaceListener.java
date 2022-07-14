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

package network.darkhelmet.tattletale.listeners;

import java.util.Locale;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

import network.darkhelmet.tattletale.Tattletale;
import network.darkhelmet.tattletale.services.configuration.MaterialConfiguration;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    /**
     * On block place.
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();

        // Ignore creative
        if (Tattletale.getInstance().configuration().ignoreCreative()
                && player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        // Let players bypass
        if (player.hasPermission("tattletale.bypass")) {
            return;
        }

        // Get block alert configuration
        MaterialConfiguration materialConfiguration = Tattletale.getInstance()
            .blockPlaceAlerts().get(event.getBlock().getType());
        if (materialConfiguration == null || !materialConfiguration.enabled()) {
            return;
        }

        TextColor color = TextColor.fromCSSHexString(materialConfiguration.hexColor());
        String blockName = event.getBlock().getType().toString().replace("_", " ")
                .toLowerCase(Locale.ENGLISH).replace("glowing", " ");

        // Create alert message
        final TextComponent.Builder component = Component.text().color(color)
            .append(Component.text(player.getDisplayName()))
            .append(Component.text(" placed "))
            .append(Component.text(blockName))
            .hoverEvent(
                HoverEvent.hoverEvent(HoverEvent.Action.SHOW_ITEM,
                    HoverEvent.ShowItem.of(Key.key(event.getBlock().getType().getKey().toString()), 1)));

        Tattletale.getInstance().alert(player, component.build());
    }
}
