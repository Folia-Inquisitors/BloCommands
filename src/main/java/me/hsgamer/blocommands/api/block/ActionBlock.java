package me.hsgamer.blocommands.api.block;

import io.github.projectunified.minelib.scheduler.async.AsyncScheduler;
import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.hscore.task.BatchRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ActionBlock {
    private final String id;
    private final Map<BlockInteractType, ActionBundle> actionBundleMap = new HashMap<>();
    private Location location;

    public ActionBlock(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Map<BlockInteractType, ActionBundle> getActionBundleMap() {
        return Collections.unmodifiableMap(actionBundleMap);
    }

    public ActionBundle getActionBundle(BlockInteractType type) {
        return actionBundleMap.computeIfAbsent(type, k -> new ActionBundle());
    }

    public void execute(Player player, BlockInteractType type) {
        if (location == null) return;
        ActionBundle actionBundle = actionBundleMap.get(type);
        if (actionBundle == null) return;
        BatchRunnable runnable = actionBundle.createRunnable(player, location);
        AsyncScheduler.get(JavaPlugin.getPlugin(BloCommands.class)).run(runnable);
    }
}
