package fr.luquioz.customworldboarder.commands;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.commands.subcommands.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.*;

public class CWBCommand implements CommandExecutor, TabCompleter {

    private final CustomWorldBoarder plugin;
    private final Map<String, SubCommand> subCommands;

    public CWBCommand(CustomWorldBoarder plugin) {
        this.plugin = plugin;
        this.subCommands = new HashMap<>();
        registerSubCommands();
    }

    private void registerSubCommands() {
        registerSubCommand("create", new CreateCommand(plugin));
        registerSubCommand("createsel", new CreateSelectionCommand(plugin));
        registerSubCommand("delete", new DeleteCommand(plugin));
        registerSubCommand("list", new ListCommand(plugin));
        registerSubCommand("activate", new ActivateCommand(plugin));
        registerSubCommand("deactivate", new DeactivateCommand(plugin));
        registerSubCommand("info", new InfoCommand(plugin));
        registerSubCommand("setcenter", new SetCenterCommand(plugin));
        registerSubCommand("setsize", new SetSizeCommand(plugin));
        registerSubCommand("setwarning", new SetWarningCommand(plugin));
        registerSubCommand("setwarncolor", new SetWarningColorCommand(plugin));
        registerSubCommand("setvisualcolor", new SetVisualColorCommand(plugin));
        registerSubCommand("reduce", new ReduceCommand(plugin));
        registerSubCommand("setdamage", new SetDamageCommand(plugin));
        registerSubCommand("setdamagerange", new SetDamageRangeCommand(plugin));
        registerSubCommand("bypass", new BypassCommand(plugin));
        registerSubCommand("setwarningtitle", new SetWarningTitleCommand(plugin));
        registerSubCommand("setwarningsubtitle", new SetWarningSubtitleCommand(plugin));
        registerSubCommand("setdamagetitle", new SetDamageTitleCommand(plugin));
        registerSubCommand("setdamagesubtitle", new SetDamageSubtitleCommand(plugin));
        registerSubCommand("setkicktitle", new SetKickTitleCommand(plugin));
        registerSubCommand("setkicksubtitle", new SetKickSubtitleCommand(plugin));
        registerSubCommand("setparticles", new SetParticlesCommand(plugin));
        registerSubCommand("setsounds", new SetSoundsCommand(plugin));
        registerSubCommand("setvisualparticles", new SetVisualParticlesCommand(plugin));
        
        HelpCommand helpCommand = new HelpCommand(plugin);
        helpCommand.setSubCommands(subCommands);
        registerSubCommand("help", helpCommand);
    }

    private void registerSubCommand(String name, SubCommand subCommand) {
        subCommands.put(name.toLowerCase(), subCommand);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("customworldborder.use")) {
            sender.sendMessage(ChatColor.RED + "Vous n'avez pas la permission d'utiliser cette commande.");
            return true;
        }

        if (args.length == 0) {
            subCommands.get("help").execute(sender, args);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        SubCommand handler = subCommands.get(subCommand);

        if (handler == null) {
            sender.sendMessage(ChatColor.RED + "Sous-commande inconnue: " + subCommand);
            subCommands.get("help").execute(sender, args);
            return true;
        }

        try {
            handler.execute(sender, args);
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Une erreur s'est produite lors de l'exécution de la commande.");
            plugin.getLogger().severe("Erreur lors de l'exécution de la commande " + subCommand + ": " + e.getMessage());
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            completions.addAll(subCommands.keySet());
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            if (!subCommand.equals("list") && !subCommand.equals("help")) {
                completions.addAll(plugin.getBorderManager().getAllBorders().keySet());
            }
        } else if (args.length == 3) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("bypass")) {
                for (org.bukkit.entity.Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            } else if (subCommand.equals("setparticles") || subCommand.equals("setsounds") || subCommand.equals("setvisualparticles")) {
                completions.addAll(Arrays.asList("true", "false"));
            }
        } else if (args.length == 4) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("bypass")) {
                completions.addAll(Arrays.asList("on", "off"));
            }
        }

        return completions.stream()
                .filter(s -> s.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .sorted()
                .toList();
    }
}
