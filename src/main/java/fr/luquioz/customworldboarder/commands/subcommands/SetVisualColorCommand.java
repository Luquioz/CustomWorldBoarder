package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class SetVisualColorCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public SetVisualColorCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-setvisualcolor")));
            return;
        }

        String name = args[1];
        String color = args[2];

        // Validate color format (R,G,B)
        String[] colorParts = color.split(",");
        if (colorParts.length != 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("rgb-error")));
            return;
        }

        try {
            int r = Integer.parseInt(colorParts[0].trim());
            int g = Integer.parseInt(colorParts[1].trim());
            int b = Integer.parseInt(colorParts[2].trim());

            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("rgb-error")));
                return;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("rgb-error")));
            return;
        }

        Border border = plugin.getBorderManager().getBorder(name);
        if (border == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", name)));
            return;
        }

        border.setVisualParticleColor(color);
        plugin.getBorderManager().saveBorders();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("visual-color-updated", "name", name)));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("new-visual-color", "color", color)));
    }

    @Override
    public String getUsage() {
        return "/cwb setvisualcolor <nom> <couleur>";
    }

    @Override
    public String getDescription() {
        return "Définir la couleur des particules visuelles";
    }

    @Override
    public String getCategory() {
        return "configuration";
    }
}
