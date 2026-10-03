package fr.luquioz.customworldboarder.listeners;

import fr.luquioz.customworldboarder.CustomWorldBoarder;
import fr.luquioz.customworldboarder.models.Border;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;

public class BorderReduceTask extends BukkitRunnable {

    private final CustomWorldBoarder plugin;

    public BorderReduceTask(CustomWorldBoarder plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (Border border : plugin.getBorderManager().getAllBorders().values()) {
            if (border.isReducing() && border.isActive()) {
                border.setReduceElapsed(border.getReduceElapsed() + 1);

                if (border.getReduceElapsed() >= border.getReduceDuration()) {
                    border.setSize(border.getTargetSize());
                    border.setReducing(false);
                    border.setReduceElapsed(0);
                    plugin.getBorderManager().saveBorders();
                    plugin.getLogger().info("Bordure '" + border.getName() + "' réduite à " + border.getSize());
                } else {
                    double progress = (double) border.getReduceElapsed() / border.getReduceDuration();
                    double totalReduction = border.getInitialSize() - border.getTargetSize();
                    double currentReduction = totalReduction * progress;
                    double newSize = border.getInitialSize() - currentReduction;
                    border.setSize(newSize);
                }
            }
        }
    }
}
