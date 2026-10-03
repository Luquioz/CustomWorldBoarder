package fr.luquioz.customworldboarder.listeners;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BorderDamageTask extends BukkitRunnable {

    private final CustomWorldBoarder plugin;

    public BorderDamageTask(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Map<String, Border> activeBorders = plugin.getBorderManager().getActiveBorders(player.getWorld());

            for (Border border : activeBorders.values()) {
                if (border.getDamagePerTick() > 0) {
                    double x = player.getLocation().getX();
                    double z = player.getLocation().getZ();

                    if (border.isInDamageZone(x, z)) {
                        if (plugin.getBypassManager().hasBypass(border.getName(), player.getUniqueId())) {
                            continue;
                        }

                        player.damage(border.getDamagePerTick());

                        if (border.isDamageMessage()) {
                            String damageTitle = border.getDamageTitle();
                            String damageSubtitle = border.getDamageSubtitle();
                            player.sendTitle(ChatColor.translateAlternateColorCodes('&', damageTitle), ChatColor.translateAlternateColorCodes('&', damageSubtitle));
                        }
                    }
                }
            }
        }
    }
}
