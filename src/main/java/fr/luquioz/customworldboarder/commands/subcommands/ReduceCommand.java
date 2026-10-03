package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ReduceCommand implements SubCommand {
    private final CustomWorldBoarder plugin;

    public ReduceCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!sender.hasPermission("customworldborder.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("no-permission")));
            return;
        }

        if (args.length < 4) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("usage-reduce")));
            return;
        }

        String name = args[1];
        double targetSize;
        int duration;

        try {
            targetSize = Double.parseDouble(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("number-error")));
            return;
        }

        try {
            duration = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("number-error")));
            return;
        }

        if (targetSize <= 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("positive-error")));
            return;
        }

        if (duration <= 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("positive-error")));
            return;
        }

        Border border = plugin.getBorderManager().getBorder(name);
        if (border == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("border-not-found", "name", name)));
            return;
        }

        border.setReducing(true);
        border.setInitialSize(border.getSize());
        border.setTargetSize(targetSize);
        border.setReduceDuration(duration);
        border.setReduceElapsed(0);
        plugin.getBorderManager().saveBorders();

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("reduce-started", "name", name, "from", String.valueOf(border.getInitialSize()), "to", String.valueOf(targetSize), "ticks", String.valueOf(duration))));
    }

    @Override
    public String getUsage() {
        return "/cwb reduce <nom> <taille_cible> <durée_secondes>";
    }

    @Override
    public String getDescription() {
        return "Réduire progressivement une bordure";
    }

    @Override
    public String getCategory() {
        return "borders";
    }
}
