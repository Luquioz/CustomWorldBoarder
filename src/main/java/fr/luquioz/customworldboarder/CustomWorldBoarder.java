package fr.luquioz.customworldboarder;

import fr.luquioz.customworldboarder.commands.CWBCommand;
import fr.luquioz.customworldboarder.commands.SubCommand;
import fr.luquioz.customworldboarder.listeners.BorderListener;
import fr.luquioz.customworldboarder.listeners.BorderParticleTask;
import fr.luquioz.customworldboarder.listeners.BorderReduceTask;
import fr.luquioz.customworldboarder.listeners.BorderDamageTask;
import fr.luquioz.customworldboarder.managers.BorderManager;
import fr.luquioz.customworldboarder.managers.BypassManager;
import fr.luquioz.customworldboarder.managers.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class CustomWorldBoarder extends JavaPlugin {

    private static CustomWorldBoarder instance;
    private BorderManager borderManager;
    private BypassManager bypassManager;
    private LanguageManager languageManager;
    private BorderParticleTask particleTask;
    private BorderReduceTask reduceTask;
    private BorderDamageTask damageTask;
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        borderManager = new BorderManager(this);
        borderManager.loadBorders();

        bypassManager = new BypassManager(this);
        bypassManager.loadBypasses();

        languageManager = new LanguageManager(this);
        languageManager.loadLanguage();

        CWBCommand commandExecutor = new CWBCommand(this);
        getCommand("cwb").setExecutor(commandExecutor);
        getCommand("cwb").setTabCompleter(commandExecutor);

        getServer().getPluginManager().registerEvents(new BorderListener(this), this);

        particleTask = new BorderParticleTask(this);
        particleTask.runTaskTimer(this, 0L, 20L);

        reduceTask = new BorderReduceTask(this);
        reduceTask.runTaskTimer(this, 0L, 1L);

        damageTask = new BorderDamageTask(this);
        damageTask.runTaskTimer(this, 0L, 1L);

        getLogger().info("CustomWorldBoarder a été activé avec succès !");
    }

    @Override
    public void onDisable() {
        if (particleTask != null) {
            particleTask.cancel();
        }
        if (reduceTask != null) {
            reduceTask.cancel();
        }
        if (damageTask != null) {
            damageTask.cancel();
        }
        if (borderManager != null) {
            borderManager.saveBorders();
        }
        if (bypassManager != null) {
            bypassManager.saveBypasses();
        }
        getLogger().info("CustomWorldBoarder a été désactivé.");
    }

    public static CustomWorldBoarder getInstance() {
        return instance;
    }

    public BorderManager getBorderManager() {
        return borderManager;
    }

    public BypassManager getBypassManager() {
        return bypassManager;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public Map<String, SubCommand> getSubCommands() {
        return subCommands;
    }

    public void registerSubCommand(String name, SubCommand subCommand) {
        subCommands.put(name.toLowerCase(), subCommand);
    }
}
