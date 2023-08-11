package me.hsgamer.blocommands.api.action;

import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface Action {
    void execute(Player player, Location blockLocation, TaskProcess taskProcess);
}
