package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreateCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public CreateCommand(CustomWorldBoarder plugin) {
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

        if (args.length < 3) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-create")));
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

        Player player = (Player) sender;

        Border border = plugin.getBorderManager().createBorder(name, player.getWorld(), player.getLocation().getX(), player.getLocation().getZ(), size);
        
        if (border == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-exists", "name", name)));
            return;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-created", "name", name)));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-center", "x", String.valueOf((int) border.getCenterX()), "z", String.valueOf((int) border.getCenterZ()))));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-size", "size", String.valueOf(size))));
    }

    @Override
    public String getUsage() {
        return "/cwb create <nom> <taille>";
    }

    @Override
    public String getDescription() {
        return "Créer une bordure à votre position";
    }

    @Override
    public String getCategory() {
        return "general";
    }
}
