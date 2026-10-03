package fr.luquioz.customworldboarder.listeners;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Map;
import java.util.UUID;

public class BorderListener implements Listener {

    private final CustomWorldBoarder plugin;

    public BorderListener(CustomWorldBoarder plugin) {
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
        return Color.ORANGE;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getFrom().getBlockX() == event.getTo().getBlockX() &&
            event.getFrom().getBlockZ() == event.getTo().getBlockZ()) {
            return;
        }

        Player player = event.getPlayer();
        Location to = event.getTo();

        Map<String, Border> activeBorders = plugin.getBorderManager().getActiveBorders(to.getWorld());

        for (Border border : activeBorders.values()) {
            double x = to.getX();
            double z = to.getZ();

            if (border.isOutsideBorder(x, z)) {
                if (plugin.getBypassManager().hasBypass(border.getName(), player.getUniqueId())) {
                    continue;
                }

                if (border.getDamagePerTick() > 0) {
                    continue;
                }

                event.setCancelled(true);

                Location center = new Location(
                    to.getWorld(),
                    border.getCenterX(),
                    to.getY(),
                    border.getCenterZ()
                );

                player.teleport(center);

                String kickTitle = border.getKickTitle();
                String kickSubtitle = border.getKickSubtitle();
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', kickTitle), ChatColor.translateAlternateColorCodes('&', kickSubtitle));

                if (border.isBorderSounds()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }

                break;
            } else if (border.isInWarningZone(x, z)) {
                if (plugin.getBypassManager().hasBypass(border.getName(), player.getUniqueId())) {
                    continue;
                }

                if (border.isBorderParticles()) {
                    String colorString = border.getWarningParticleColor();
                    Color color = parseColor(colorString);
                    player.spawnParticle(Particle.DUST, to, 1, 0, 0, 0, 1, new org.bukkit.Particle.DustOptions(color, 1.0f));
                }

                if (border.isBorderSounds()) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5f, 2.0f);
                }

                String warningTitle = border.getWarningTitle();
                String warningSubtitle = border.getWarningSubtitle();
                player.sendTitle(ChatColor.translateAlternateColorCodes('&', warningTitle), ChatColor.translateAlternateColorCodes('&', warningSubtitle));
            }
        }
    }
}
