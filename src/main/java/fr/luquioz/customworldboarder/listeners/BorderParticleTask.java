package fr.luquioz.customworldboarder.listeners;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BorderParticleTask extends BukkitRunnable {

    private final CustomWorldBoarder plugin;

    public BorderParticleTask(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    private Color parseColor(String colorString) {
        try {
            String[] parts = colorString.split(",");
            if (parts.length == 3) {
                int r = Integer.parseInt(parts[0].trim());
                int g = Integer.parseInt(parts[1].trim());
                int b = Integer.parseInt(parts[2].trim());
                return Color.fromRGB(r, g, b);
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Format de couleur invalide: " + colorString);
        }
        return Color.GREEN;
    }

    @Override
    public void run() {
        for (World world : plugin.getServer().getWorlds()) {
            Map<String, Border> activeBorders = plugin.getBorderManager().getActiveBorders(world);

            for (Border border : activeBorders.values()) {
                if (!border.isBorderVisualParticles()) {
                    continue;
                }

                double centerX = border.getCenterX();
                double centerZ = border.getCenterZ();
                double size = border.getSize();
                int points = 144;
                double angleStep = 2 * Math.PI / points;

                String colorString = border.getVisualParticleColor();
                Color color = parseColor(colorString);

                for (int i = 0; i < points; i++) {
                    double angle = i * angleStep;
                    double x = centerX + size * Math.cos(angle);
                    double z = centerZ + size * Math.sin(angle);

                    for (int y = 60; y < 130; y += 4) {
                        Location particleLoc = new Location(world, x, y, z);
                        world.spawnParticle(Particle.DUST, particleLoc, 3, 0, 0, 0, 1, new Particle.DustOptions(color, 1.0f));
                    }
                }
            }
        }
    }
}
