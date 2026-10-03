package fr.luquioz.customworldboarder.managers;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BypassManager {
    private final CustomWorldBoarder plugin;
    private final Map<String, Map<UUID, Boolean>> borderBypasses;
    private final Map<String, Map<String, Boolean>> groupBypasses;
    private final File bypassFile;
    private FileConfiguration bypassConfig;

    public BypassManager(CustomWorldBoarder plugin) {
        this.plugin = plugin;
        this.borderBypasses = new HashMap<>();
        this.groupBypasses = new HashMap<>();
        this.bypassFile = new File(plugin.getDataFolder(), "bypass.yml");
    }

    public void loadBypasses() {
        if (!bypassFile.exists()) {
            try {
                bypassFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Impossible de créer le fichier bypass.yml");
                e.printStackTrace();
            }
            return;
        }

        bypassConfig = YamlConfiguration.loadConfiguration(bypassFile);
        ConfigurationSection section = bypassConfig.getConfigurationSection("bypasses");

        if (section == null) {
            return;
        }

        for (String borderName : section.getKeys(false)) {
            ConfigurationSection borderSection = section.getConfigurationSection(borderName);
            if (borderSection == null) continue;

            Map<UUID, Boolean> players = new HashMap<>();
            ConfigurationSection playersSection = borderSection.getConfigurationSection("players");
            if (playersSection != null) {
                for (String uuidString : playersSection.getKeys(false)) {
                    try {
                        UUID uuid = UUID.fromString(uuidString);
                        boolean bypassEnabled = playersSection.getBoolean(uuidString, false);
                        players.put(uuid, bypassEnabled);
                    } catch (IllegalArgumentException e) {
                        plugin.getLogger().warning("UUID invalide: " + uuidString);
                    }
                }
            }

            Map<String, Boolean> groups = new HashMap<>();
            ConfigurationSection groupsSection = borderSection.getConfigurationSection("groups");
            if (groupsSection != null) {
                for (String groupName : groupsSection.getKeys(false)) {
                    boolean bypassEnabled = groupsSection.getBoolean(groupName, false);
                    groups.put(groupName, bypassEnabled);
                }
            }

            borderBypasses.put(borderName, players);
            groupBypasses.put(borderName, groups);
        }

        plugin.getLogger().info(borderBypasses.size() + " bordure(s) avec bypass chargée(s).");
    }

    public void saveBypasses() {
        bypassConfig = new YamlConfiguration();

        ConfigurationSection bypassesSection = bypassConfig.createSection("bypasses");

        for (String borderName : borderBypasses.keySet()) {
            ConfigurationSection borderSection = bypassesSection.createSection(borderName);

            Map<UUID, Boolean> players = borderBypasses.get(borderName);
            if (players != null && !players.isEmpty()) {
                ConfigurationSection playersSection = borderSection.createSection("players");
                for (Map.Entry<UUID, Boolean> playerEntry : players.entrySet()) {
                    playersSection.set(playerEntry.getKey().toString(), playerEntry.getValue());
                }
            }

            Map<String, Boolean> groups = groupBypasses.get(borderName);
            if (groups != null && !groups.isEmpty()) {
                ConfigurationSection groupsSection = borderSection.createSection("groups");
                for (Map.Entry<String, Boolean> groupEntry : groups.entrySet()) {
                    groupsSection.set(groupEntry.getKey(), groupEntry.getValue());
                }
            }
        }

        try {
            bypassConfig.save(bypassFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Impossible de sauvegarder le fichier bypass.yml");
            e.printStackTrace();
        }
    }

    public boolean hasBypass(String borderName, UUID playerUUID) {
        Map<UUID, Boolean> players = borderBypasses.get(borderName);
        if (players != null && players.getOrDefault(playerUUID, false)) {
            return true;
        }

        Map<String, Boolean> groups = groupBypasses.get(borderName);
        if (groups != null) {
            Player player = Bukkit.getPlayer(playerUUID);
            if (player != null) {
                LuckPerms luckPerms = Bukkit.getServicesManager().load(LuckPerms.class);
                if (luckPerms != null) {
                    User user = luckPerms.getUserManager().getUser(playerUUID);
                    if (user != null) {
                        for (Group group : user.getInheritedGroups(user.getQueryOptions())) {
                            if (groups.getOrDefault(group.getName(), false)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    public void setBypass(String borderName, UUID playerUUID, boolean bypassEnabled) {
        borderBypasses.computeIfAbsent(borderName, k -> new HashMap<>());
        borderBypasses.get(borderName).put(playerUUID, bypassEnabled);
        saveBypasses();
    }

    public void setGroupBypass(String borderName, String groupName, boolean bypassEnabled) {
        groupBypasses.computeIfAbsent(borderName, k -> new HashMap<>());
        groupBypasses.get(borderName).put(groupName, bypassEnabled);
        saveBypasses();
    }

    public void removeBypass(String borderName, UUID playerUUID) {
        Map<UUID, Boolean> players = borderBypasses.get(borderName);
        if (players != null) {
            players.remove(playerUUID);
            if (players.isEmpty()) {
                borderBypasses.remove(borderName);
            }
            saveBypasses();
        }
    }

    public void removeGroupBypass(String borderName, String groupName) {
        Map<String, Boolean> groups = groupBypasses.get(borderName);
        if (groups != null) {
            groups.remove(groupName);
            if (groups.isEmpty()) {
                groupBypasses.remove(borderName);
            }
            saveBypasses();
        }
    }

    public Map<String, Map<UUID, Boolean>> getAllBypasses() {
        return new HashMap<>(borderBypasses);
    }

    public Map<String, Map<String, Boolean>> getAllGroupBypasses() {
        return new HashMap<>(groupBypasses);
    }
}
