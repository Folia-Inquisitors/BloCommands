package me.hsgamer.blocommands.api.action;

import me.hsgamer.hscore.task.BatchRunnable;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActionBundle {
    private final List<Action> actions = new ArrayList<>();

    public void addAction(Action action) {
        actions.add(action);
    }

    public void removeAction(Action action) {
        actions.remove(action);
    }

    public void clearActions() {
        actions.clear();
    }

    public List<Action> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public BatchRunnable createRunnable(Player player, Location blockLocation) {
        BatchRunnable runnable = new BatchRunnable();
        for (Action action : actions) {
            runnable.getTaskPool(0).addLast(taskProcess -> action.execute(player, blockLocation, taskProcess));
        }
        return runnable;
    }
}
