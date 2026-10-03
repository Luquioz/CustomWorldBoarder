package fr.luquioz.customworldboarder.managers;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class LanguageManager {
    private final CustomWorldBoarder plugin;
    private FileConfiguration langConfig;
    private final File langFile;

    public LanguageManager(CustomWorldBoarder plugin) {
        this.plugin = plugin;
        this.langFile = new File(plugin.getDataFolder(), "lang.yml");
    }

    public void loadLanguage() {
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);
        plugin.getLogger().info("Langue chargée.");
    }

    public String getMessage(String key) {
        String message = langConfig.getString(key);
        if (message == null) {
            plugin.getLogger().warning("Message introuvable: " + key);
            return key;
        }
        return message;
    }

    public String getMessage(String key, String... placeholders) {
        String message = getMessage(key);
        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                message = message.replace("{" + placeholders[i] + "}", placeholders[i + 1]);
            }
        }
        return message;
    }

    public void reloadLanguage() {
        if (langFile.exists()) {
            langConfig = YamlConfiguration.loadConfiguration(langFile);
        }
    }
}
