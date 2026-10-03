package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BypassCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public BypassCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 4) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-bypass")));
            return;
        }

        String borderName = args[1];
        String targetPlayer = args[2];
        String state = args[3].toLowerCase();

        if (!state.equals("on") && !state.equals("off")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("state-error")));
            return;
        }

        // Check if border exists
        if (plugin.getBorderManager().getBorder(borderName) == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", borderName)));
            return;
        }

        boolean enable = state.equals("on");

        // Find target player
        Player target = Bukkit.getPlayer(targetPlayer);
        if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("player-not-found", "player", targetPlayer)));
            return;
        }

        if (enable) {
            plugin.getBypassManager().setBypass(borderName, target.getUniqueId(), true);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("bypass-enabled", "player", target.getName(), "border", borderName)));
        } else {
            plugin.getBypassManager().setBypass(borderName, target.getUniqueId(), false);
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("bypass-disabled", "player", target.getName(), "border", borderName)));
        }
    }

    @Override
    public String getUsage() {
        return "/cwb bypass <bordure> <joueur> <on|off>";
    }

    @Override
    public String getDescription() {
        return "Activer/désactiver le contournement pour un joueur sur une bordure";
    }

    @Override
    public String getCategory() {
        return "borders";
    }
}
