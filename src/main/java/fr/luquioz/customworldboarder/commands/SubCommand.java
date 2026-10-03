package fr.luquioz.customworldboarder.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    void execute(CommandSender sender, String[] args);
    String getUsage();
    String getDescription();
    String getCategory();
}
