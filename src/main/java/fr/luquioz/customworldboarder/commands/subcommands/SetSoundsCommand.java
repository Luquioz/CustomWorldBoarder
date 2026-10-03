package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetSoundsCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public SetSoundsCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-setsounds")));
            return;
        }

        String name = args[1];
        String state = args[2].toLowerCase();

        if (!state.equals("true") && !state.equals("false")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("boolean-error")));
            return;
        }

        boolean enabled = state.equals("true");

        Border border = plugin.getBorderManager().getBorder(name);
        if (border == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", name)));
            return;
        }

        border.setBorderSounds(enabled);
        plugin.getBorderManager().saveBorders();

        String key = enabled ? "sounds-enabled" : "sounds-disabled";
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage(key)));
    }

    @Override
    public String getUsage() {
        return "/cwb setsounds <nom> <true|false>";
    }

    @Override
    public String getDescription() {
        return "Activer/désactiver les sons de bordure";
    }

    @Override
    public String getCategory() {
        return "configuration";
    }
}
