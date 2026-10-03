package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class DeleteCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public DeleteCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-delete")));
            return;
        }

        String name = args[1];

        if (plugin.getBorderManager().getBorder(name) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", name)));
            return;
        }

        plugin.getBorderManager().deleteBorder(name);
        plugin.getBorderManager().saveBorders();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-deleted", "name", name)));
    }

    @Override
    public String getUsage() {
        return "/cwb delete <nom>";
    }

    @Override
    public String getDescription() {
        return "Supprimer une bordure";
    }

    @Override
    public String getCategory() {
        return "general";
    }
}
