package network.darkhelmet.tattletale.providers.placements;

import net.kyori.adventure.text.serializer.plain.PlainComponentSerializer;
import network.darkhelmet.prism.api.PrismApi;
import network.darkhelmet.prism.api.PrismParameters;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class Prism3PlacementProvider {
    public Prism3PlacementProvider() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("Prism");
        if (plugin != null && plugin.isEnabled()) {
            PrismApi prismApi = (PrismApi) plugin;

            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
                PrismParameters parameters = prismApi.createParameters();
                parameters.setWorld(player.getWorld().getName());
                parameters.addSpecificBlockLocation(block.getLocation());
                parameters.addActionType("block-place");

                prismApi.performLookup(parameters);

//                final ActionsQuery aq = new ActionsQuery(plugin);
//                final QueryResult results = aq.lookup(params, player);
//                if (results.getActionResults().isEmpty()) {
//                    // Block was not placed - Alert staff
//                    plugin.alertPlayers(null, component, alertPerm);
//                    // Log to console
//                    if (plugin.getConfig().getBoolean("prism.alerts.ores.log-to-console")) {
//                        Prism.log(PlainComponentSerializer.plain().serialize(component));
//                    }
//                    // Log to commands
//                    List<String> commands = plugin.getConfig().getStringList("prism.alerts.ores.log-commands");
//                    MiscUtils.dispatchAlert(msg, commands);
//                }
            });
        }
    }
}
