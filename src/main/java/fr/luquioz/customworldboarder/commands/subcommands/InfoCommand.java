package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class InfoCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public InfoCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-info")));
            return;
        }

        String name = args[1];
        Border border = plugin.getBorderManager().getBorder(name);

        if (border == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", name)));
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-header", "name", name)));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-world", "world", border.getWorldName())));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-center", "x", String.valueOf((int) border.getCenterX()), "z", String.valueOf((int) border.getCenterZ()))));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-size", "size", String.valueOf(border.getSize()))));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-warning-distance", "distance", String.valueOf(border.getWarningDistance()))));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-damage-per-tick", "damage", String.valueOf(border.getDamagePerTick()))));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-damage-range", "range", String.valueOf(border.getDamageRange()))));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-warning-color", "color", border.getWarningParticleColor())));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-visual-color", "color", border.getVisualParticleColor())));
        String statusKey = border.isActive() ? "info-status-active" : "info-status-inactive";
        String status = ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage(statusKey));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("info-status", "status", status)));
    }

    @Override
    public String getUsage() {
        return "/cwb info <nom>";
    }

    @Override
    public String getDescription() {
        return "Voir les infos d'une bordure";
    }

    @Override
    public String getCategory() {
        return "general";
    }
}
