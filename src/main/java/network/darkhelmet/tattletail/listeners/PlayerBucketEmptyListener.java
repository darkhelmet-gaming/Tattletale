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

import java.util.Locale;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;

import network.darkhelmet.tattletail.Tattletail;
import network.darkhelmet.tattletail.services.configuration.MaterialConfiguration;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

public class PlayerBucketEmptyListener implements Listener {
    /**
     * On bucket empty.
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        final Player player = event.getPlayer();

        // Ignore creative
        if (player.getGameMode().equals(GameMode.CREATIVE)) {
            return;
        }

        // Get alert configuration
        MaterialConfiguration materialConfiguration = Tattletail.getInstance()
            .bucketEmptyAlerts().get(event.getBucket());
        if (materialConfiguration == null) {
            return;
        }

        TextColor color = TextColor.fromCSSHexString(materialConfiguration.hexColor());
        String bucketName = event.getBucket().toString().replace("_", " ").toLowerCase(Locale.ENGLISH);

        // Create alert message
        final TextComponent.Builder component = Component.text().color(color)
            .append(Component.text(player.getDisplayName()))
            .append(Component.text(" emptied "))
            .append(Component.text(bucketName))
            .hoverEvent(
                HoverEvent.hoverEvent(HoverEvent.Action.SHOW_ITEM,
                    HoverEvent.ShowItem.of(Key.key(event.getBucket().getKey().toString()), 1)));

        Tattletail.getInstance().adventure()
            .filter(sender -> sender.hasPermission("tattletail.receivealerts")).sendMessage(component.build());
    }
}