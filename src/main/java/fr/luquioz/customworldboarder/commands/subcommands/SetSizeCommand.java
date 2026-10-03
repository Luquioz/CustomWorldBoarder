package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetSizeCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public SetSizeCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-setsize")));
            return;
        }

        String name = args[1];
        double size;

        try {
            size = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("number-error")));
            return;
        }

        if (size <= 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("positive-error")));
            return;
        }

        Border border = plugin.getBorderManager().getBorder(name);
        if (border == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", name)));
            return;
        }

        border.setSize(size);
        plugin.getBorderManager().saveBorders();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("size-updated", "name", name)));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("new-size", "size", String.valueOf(size))));
    }

    @Override
    public String getUsage() {
        return "/cwb setsize <nom> <taille>";
    }

    @Override
    public String getDescription() {
        return "Définir la taille d'une bordure";
    }

    @Override
    public String getCategory() {
        return "borders";
    }
}
