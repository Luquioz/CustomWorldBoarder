package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetCenterCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public SetCenterCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("player-only")));
            return;
        }

        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-setcenter")));
            return;
        }

        String name = args[1];
        Border border = plugin.getBorderManager().getBorder(name);

        if (border == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", name)));
            return;
        }

        Player player = (Player) sender;
        border.setCenterX(player.getLocation().getX());
        border.setCenterZ(player.getLocation().getZ());
        plugin.getBorderManager().saveBorders();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("center-updated", "name", name)));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("new-center", "x", String.valueOf((int) border.getCenterX()), "z", String.valueOf((int) border.getCenterZ()))));
    }

    @Override
    public String getUsage() {
        return "/cwb setcenter <nom>";
    }

    @Override
    public String getDescription() {
        return "Définir le centre à votre position";
    }

    @Override
    public String getCategory() {
        return "borders";
    }
}
