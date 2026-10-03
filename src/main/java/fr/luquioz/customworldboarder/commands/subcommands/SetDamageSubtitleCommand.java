package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetDamageSubtitleCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public SetDamageSubtitleCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-setdamagesubtitle")));
            return;
        }

        String name = args[1];
        StringBuilder subtitleBuilder = new StringBuilder();
        for (int i = 2; i < args.length; i++) {
            subtitleBuilder.append(args[i]).append(" ");
        }
        String subtitle = subtitleBuilder.toString().trim();

        Border border = plugin.getBorderManager().getBorder(name);
        if (border == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", name)));
            return;
        }

        border.setDamageSubtitle(subtitle);
        plugin.getBorderManager().saveBorders();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("damage-subtitle-updated")));
    }

    @Override
    public String getUsage() {
        return "/cwb setdamagesubtitle <nom> <sous-titre>";
    }

    @Override
    public String getDescription() {
        return "Définir le sous-titre de dégâts";
    }

    @Override
    public String getCategory() {
        return "configuration";
    }
}
