package fr.luquioz.customworldboarder.managers;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BorderManager {
    private final CustomWorldBoarder plugin;
    private final Map<String, Border> borders;
    private final File bordersFile;
    private FileConfiguration bordersConfig;

    public BorderManager(CustomWorldBoarder plugin) {
        this.plugin = plugin;
        this.borders = new HashMap<>();
        this.bordersFile = new File(plugin.getDataFolder(), "borders.yml");
    }

    public void loadBorders() {
        if (!bordersFile.exists()) {
            try {
                bordersFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().severe("Impossible de créer le fichier borders.yml");
                e.printStackTrace();
            }
            return;
        }

        bordersConfig = YamlConfiguration.loadConfiguration(bordersFile);
        ConfigurationSection section = bordersConfig.getConfigurationSection("borders");

        if (section == null) {
            return;
        }

        for (String key : section.getKeys(false)) {
            ConfigurationSection borderSection = section.getConfigurationSection(key);
            if (borderSection == null) continue;

            String worldName = borderSection.getString("world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                plugin.getLogger().warning("Monde '" + worldName + "' introuvable pour la bordure '" + key + "'");
                continue;
            }

            Border border = new Border(
                key,
                world,
                borderSection.getDouble("centerX"),
                borderSection.getDouble("centerZ"),
                borderSection.getDouble("size")
            );
            border.setWarningDistance(borderSection.getDouble("warningDistance", 5.0));
            border.setActive(borderSection.getBoolean("active", false));
            border.setDamagePerTick(borderSection.getDouble("damagePerTick", 0.0));
            border.setDamageRange(borderSection.getDouble("damageRange", 100.0));
            border.setWarningParticleColor(borderSection.getString("warningParticleColor", "255,85,0"));
            border.setVisualParticleColor(borderSection.getString("visualParticleColor", "0,255,0"));
            border.setBorderParticles(borderSection.getBoolean("borderParticles", true));
            border.setBorderSounds(borderSection.getBoolean("borderSounds", true));
            border.setBorderVisualParticles(borderSection.getBoolean("borderVisualParticles", true));
            border.setWarningTitle(borderSection.getString("warningTitle", "&c⚠ Zone !"));
            border.setWarningSubtitle(borderSection.getString("warningSubtitle", "&cVous sortez de la zone !"));
            border.setDamageMessage(borderSection.getBoolean("damageMessage", true));
            border.setDamageTitle(borderSection.getString("damageTitle", "&cDégâts !"));
            border.setDamageSubtitle(borderSection.getString("damageSubtitle", "&cVous perdez des points de vie !"));
            border.setKickTitle(borderSection.getString("kickTitle", "&cRetour !"));
            border.setKickSubtitle(borderSection.getString("kickSubtitle", "&cVous avez été renvoyé au centre !"));
            border.setReducing(borderSection.getBoolean("isReducing", false));
            border.setInitialSize(borderSection.getDouble("initialSize", border.getSize()));
            border.setTargetSize(borderSection.getDouble("targetSize", border.getSize()));
            border.setReduceDuration(borderSection.getInt("reduceDuration", 0));
            border.setReduceElapsed(borderSection.getInt("reduceElapsed", 0));

            borders.put(key, border);
        }

        plugin.getLogger().info(borders.size() + " bordure(s) chargée(s).");
    }

    public void saveBorders() {
        bordersConfig = new YamlConfiguration();

        ConfigurationSection bordersSection = bordersConfig.createSection("borders");

        for (Map.Entry<String, Border> entry : borders.entrySet()) {
            Border border = entry.getValue();
            ConfigurationSection borderSection = bordersSection.createSection(entry.getKey());

            borderSection.set("world", border.getWorldName());
            borderSection.set("centerX", border.getCenterX());
            borderSection.set("centerZ", border.getCenterZ());
            borderSection.set("size", border.getSize());
            borderSection.set("warningDistance", border.getWarningDistance());
            borderSection.set("active", border.isActive());
            borderSection.set("damagePerTick", border.getDamagePerTick());
            borderSection.set("damageRange", border.getDamageRange());
            borderSection.set("warningParticleColor", border.getWarningParticleColor());
            borderSection.set("visualParticleColor", border.getVisualParticleColor());
            borderSection.set("warningDistance", border.getWarningDistance());
            borderSection.set("borderParticles", border.isBorderParticles());
            borderSection.set("borderSounds", border.isBorderSounds());
            borderSection.set("borderVisualParticles", border.isBorderVisualParticles());
            borderSection.set("warningTitle", border.getWarningTitle());
            borderSection.set("warningSubtitle", border.getWarningSubtitle());
            borderSection.set("damageMessage", border.isDamageMessage());
            borderSection.set("damageTitle", border.getDamageTitle());
            borderSection.set("damageSubtitle", border.getDamageSubtitle());
            borderSection.set("kickTitle", border.getKickTitle());
            borderSection.set("kickSubtitle", border.getKickSubtitle());
            borderSection.set("isReducing", border.isReducing());
            borderSection.set("initialSize", border.getInitialSize());
            borderSection.set("targetSize", border.getTargetSize());
            borderSection.set("reduceDuration", border.getReduceDuration());
            borderSection.set("reduceElapsed", border.getReduceElapsed());
        }

        try {
            bordersConfig.save(bordersFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Impossible de sauvegarder le fichier borders.yml");
            e.printStackTrace();
        }
    }

    public Border createBorder(String name, World world, double centerX, double centerZ, double size) {
        if (borders.containsKey(name)) {
            return null;
        }

        Border border = new Border(name, world, centerX, centerZ, size);
        borders.put(name, border);
        saveBorders();
        return border;
    }

    public Border createBorderFromSelection(String name, World world, double x1, double z1, double x2, double z2) {
        if (borders.containsKey(name)) {
            return null;
        }

        double centerX = (x1 + x2) / 2.0;
        double centerZ = (z1 + z2) / 2.0;
        double size = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(z2 - z1, 2)) / 2.0;

        Border border = new Border(name, world, centerX, centerZ, size);
        borders.put(name, border);
        saveBorders();
        return border;
    }

    public boolean deleteBorder(String name) {
        if (borders.remove(name) != null) {
            saveBorders();
            return true;
        }
        return false;
    }

    public Border getBorder(String name) {
        return borders.get(name);
    }

    public Map<String, Border> getAllBorders() {
        return new HashMap<>(borders);
    }

    public Map<String, Border> getActiveBorders(World world) {
        Map<String, Border> activeBorders = new HashMap<>();
        for (Map.Entry<String, Border> entry : borders.entrySet()) {
            Border border = entry.getValue();
            if (border.isActive() && border.getWorldName().equals(world.getName())) {
                activeBorders.put(entry.getKey(), border);
            }
        }
        return activeBorders;
    }

    public boolean activateBorder(String name) {
        Border border = borders.get(name);
        if (border != null) {
            border.setActive(true);
            saveBorders();
            return true;
        }
        return false;
    }

    public boolean deactivateBorder(String name) {
        Border border = borders.get(name);
        if (border != null) {
            border.setActive(false);
            saveBorders();
            return true;
        }
        return false;
    }

    public boolean borderExists(String name) {
        return borders.containsKey(name);
    }
}
