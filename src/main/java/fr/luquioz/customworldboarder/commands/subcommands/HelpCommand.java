package fr.luquioz.customworldboarder.commands.subcommands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.SubCommand;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class HelpCommand implements SubCommand {
    private final CustomWorldBoarder plugin;
    private Map<String, SubCommand> subCommands;
    private static final int COMMANDS_PER_PAGE = 5;

    public HelpCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    public void setSubCommands(Map<String, SubCommand> subCommands) {
        this.subCommands = subCommands;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (subCommands == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("command-error")));
            return;
        }

        int page = 1;
        if (args.length > 1) {
            try {
                page = Integer.parseInt(args[1]);
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        // Essential commands only - organized by function
        List<SubCommand> essentialCommands = new ArrayList<>();
        
        // Page 1: Basic operations
        if (page == 1) {
            essentialCommands.add(subCommands.get("create"));
            essentialCommands.add(subCommands.get("list"));
            essentialCommands.add(subCommands.get("activate"));
            essentialCommands.add(subCommands.get("deactivate"));
            essentialCommands.add(subCommands.get("delete"));
        }
        // Page 2: Border configuration
        else if (page == 2) {
            essentialCommands.add(subCommands.get("info"));
            essentialCommands.add(subCommands.get("setcenter"));
            essentialCommands.add(subCommands.get("setsize"));
            essentialCommands.add(subCommands.get("setwarning"));
            essentialCommands.add(subCommands.get("reduce"));
        }
        // Page 3: Advanced configuration
        else if (page == 3) {
            essentialCommands.add(subCommands.get("setdamage"));
            essentialCommands.add(subCommands.get("setdamagerange"));
            essentialCommands.add(subCommands.get("setwarncolor"));
            essentialCommands.add(subCommands.get("setvisualcolor"));
            essentialCommands.add(subCommands.get("bypass"));
        }
        // Page 4: Messages & Effects
        else if (page == 4) {
            essentialCommands.add(subCommands.get("setwarningtitle"));
            essentialCommands.add(subCommands.get("setwarningsubtitle"));
            essentialCommands.add(subCommands.get("setdamagetitle"));
            essentialCommands.add(subCommands.get("setdamagesubtitle"));
            essentialCommands.add(subCommands.get("setkicktitle"));
        }
        // Page 5: Particles & Sounds
        else if (page == 5) {
            essentialCommands.add(subCommands.get("setkicksubtitle"));
            essentialCommands.add(subCommands.get("setparticles"));
            essentialCommands.add(subCommands.get("setsounds"));
            essentialCommands.add(subCommands.get("setvisualparticles"));
        }
        else {
            return;
        }

        int totalPages = 5;

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getLanguageManager().getMessage("help-header")));

        for (SubCommand subCommand : essentialCommands) {
            if (subCommand != null) {
                sender.sendMessage(ChatColor.WHITE + subCommand.getUsage() + ChatColor.GRAY + " - " + subCommand.getDescription());
            }
        }

        // Navigation buttons
        if (totalPages > 1) {
            TextComponent navMessage = new TextComponent();
            
            if (page > 1) {
                TextComponent prevButton = new TextComponent(ChatColor.YELLOW + "◀ Page précédente");
                prevButton.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/cwb help " + (page - 1)));
                prevButton.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Aller à la page " + (page - 1))));
                navMessage.addExtra(" ");
                navMessage.addExtra(prevButton);
            }
            
            if (page < totalPages) {
                navMessage.addExtra(" ");
                TextComponent nextButton = new TextComponent(ChatColor.YELLOW + "Page suivante ►");
                nextButton.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/cwb help " + (page + 1)));
                nextButton.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.GRAY + "Aller à la page " + (page + 1))));
                navMessage.addExtra(nextButton);
            }
            
            sender.spigot().sendMessage(navMessage);
        }
    }

    @Override
    public String getUsage() {
        return "/cwb help [page]";
    }

    @Override
    public String getDescription() {
        return "Afficher l'aide";
    }

    @Override
    public String getCategory() {
        return "general";
    }
}
