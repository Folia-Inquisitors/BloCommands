package me.hsgamer.blocommands.manager;

import fr.skytasul.glowingentities.GlowingBlocks;
import io.github.projectunified.minelib.plugin.base.Loadable;
import io.github.projectunified.minelib.scheduler.common.task.Task;
import io.github.projectunified.minelib.scheduler.entity.EntityScheduler;
import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.hscore.bukkit.utils.VersionUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.logging.Level;

public class FlashManager implements Loadable {
    private final BloCommands plugin;
    private final Map<UUID, FlashTask> taskMap = new HashMap<>();
    private GlowingBlocks glowingBlocks;

    public FlashManager(BloCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public void enable() {
        if (VersionUtils.isAtLeast(17)) {
            try {
                glowingBlocks = new GlowingBlocks(plugin);
            } catch (Throwable throwable) {
                plugin.getLogger().log(Level.WARNING, "Failed to initialize GlowingBlocks. The feature is now disabled", throwable);
                glowingBlocks = null;
            }
        }
    }

    @Override
    public void disable() {
        taskMap.values().forEach(FlashTask::cancel);
        taskMap.clear();
    }

    public void start(Player player) {
        if (taskMap.containsKey(player.getUniqueId())) {
            FlashTask task = taskMap.get(player.getUniqueId());
            if (!task.isCancelled()) {
                task.cancel();
            }
            taskMap.remove(player.getUniqueId());
        }

        if (glowingBlocks == null) return;

        taskMap.put(player.getUniqueId(), new FlashTask(player));
    }

    public void stop(Player player) {
        if (taskMap.containsKey(player.getUniqueId())) {
            FlashTask task = taskMap.get(player.getUniqueId());
            if (!task.isCancelled()) {
                task.cancel();
            }
            taskMap.remove(player.getUniqueId());
        }
    }

    public boolean isFlashing(Player player) {
        return taskMap.containsKey(player.getUniqueId()) && !taskMap.get(player.getUniqueId()).isCancelled();
    }

    private final class FlashRunnable implements BooleanSupplier {
        private final Player player;
        private final List<Location> locations = new ArrayList<>();
        private long lastLocationUpdate = 0;

        private FlashRunnable(Player player) {
            this.player = player;
        }

        @Override
        public boolean getAsBoolean() {
            if (!player.isOnline()) {
                locations.clear();
                return false;
            }

            if (plugin.get(BlockManager.class).getLastLocationUpdate() > lastLocationUpdate) {
                locations.forEach(location -> {
                    try {
                        glowingBlocks.unsetGlowing(location, player);
                    } catch (ReflectiveOperationException e) {
                        // IGNORED
                    }
                });
                locations.clear();
                locations.addAll(plugin.get(BlockManager.class).getBlocksByLocation().keySet());
                lastLocationUpdate = plugin.get(BlockManager.class).getLastLocationUpdate();

                locations.forEach(location -> {
                    try {
                        glowingBlocks.setGlowing(location, player, ChatColor.WHITE);
                    } catch (ReflectiveOperationException e) {
                        // IGNORED
                    }
                });
            }

            return true;
        }
    }

    private final class FlashTask {
        private final Player player;
        private final Task task;
        private final FlashRunnable flashRunnable;

        private FlashTask(Player player) {
            this.player = player;
            this.flashRunnable = new FlashRunnable(player);
            this.task = EntityScheduler.get(plugin, player).runTimer(flashRunnable, 0, 0);
        }

        public void cancel() {
            task.cancel();
            flashRunnable.locations.forEach(location -> {
                try {
                    glowingBlocks.unsetGlowing(location, player);
                } catch (ReflectiveOperationException e) {
                    // IGNORED
                }
            });
        }

        public boolean isCancelled() {
            return task.isCancelled();
        }
    }
}
