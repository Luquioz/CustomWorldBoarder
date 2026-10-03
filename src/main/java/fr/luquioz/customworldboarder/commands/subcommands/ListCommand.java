package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Map;

public class ListCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public ListCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Map<String, fr.luquioz.customworldboarder.models.Border> borders = plugin.getBorderManager().getAllBorders();

        if (borders.isEmpty()) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-borders")));
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("list-header")));
        for (Map.Entry<String, fr.luquioz.customworldboarder.models.Border> entry : borders.entrySet()) {
            String statusKey = entry.getValue().isActive() ? "list-active" : "list-inactive";
            String status = ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage(statusKey));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("list-item", "name", entry.getKey(), "status", status)));
        }
    }

    @Override
    public String getUsage() {
        return "/cwb list";
    }

    @Override
    public String getDescription() {
        return "Lister toutes les bordures";
    }

    @Override
    public String getCategory() {
        return "general";
    }
}
