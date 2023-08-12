package me.hsgamer.blocommands.action;

import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerAction extends CommandAction {
    public PlayerAction(String command) {
        super(command);
    }

    @Override
    protected void execute(Player player, Location blockLocation, String parsedCommand, TaskProcess taskProcess) {
        Scheduler.current().sync().runEntityTask(player, () -> {
            player.performCommand(parsedCommand);
            taskProcess.next();
        });
    }
}
