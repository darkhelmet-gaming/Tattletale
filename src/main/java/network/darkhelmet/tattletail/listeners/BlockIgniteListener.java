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

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

import network.darkhelmet.tattletail.Tattletail;
import network.darkhelmet.tattletail.services.configuration.AlertConfiguration;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;

public class BlockIgniteListener implements Listener {
    /**
     * On block break.
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockIgnite(final BlockIgniteEvent event) {
        final Player player = event.getPlayer();
        if (player == null) {
            return;
        }

        // Ignore creative
        if (Tattletail.getInstance().configuration().ignoreCreative()
                && player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        // Let players bypass
        if (player.hasPermission("tattletail.bypass")) {
            return;
        }

        AlertConfiguration alertConfiguration = Tattletail.getInstance().configuration().lighterUseAlert();
        if (!alertConfiguration.enabled()) {
            return;
        }

        TextColor color = TextColor.fromCSSHexString(alertConfiguration.hexColor());

        // Create alert message
        final TextComponent.Builder component = Component.text().color(color)
            .append(Component.text(player.getDisplayName()))
            .append(Component.text(" used a lighter"))
            .hoverEvent(
                HoverEvent.hoverEvent(HoverEvent.Action.SHOW_ITEM,
                    HoverEvent.ShowItem.of(Key.key(event.getBlock().getType().getKey().toString()), 1)));

        Tattletail.getInstance().adventure()
            .filter(sender -> sender.hasPermission("tattletail.receivealerts")).sendMessage(component.build());
    }
}
