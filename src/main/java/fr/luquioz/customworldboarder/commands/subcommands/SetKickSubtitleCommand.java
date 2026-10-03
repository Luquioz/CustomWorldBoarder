package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetKickSubtitleCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public SetKickSubtitleCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-setkicksubtitle")));
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

        border.setKickSubtitle(subtitle);
        plugin.getBorderManager().saveBorders();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("kick-subtitle-updated")));
    }

    @Override
    public String getUsage() {
        return "/cwb setkicksubtitle <nom> <sous-titre>";
    }

    @Override
    public String getDescription() {
        return "Définir le sous-titre d'expulsion";
    }

    @Override
    public String getCategory() {
        return "configuration";
    }
}
