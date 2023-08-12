package me.hsgamer.blocommands.action;

import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ConsoleAction extends CommandAction {
    public ConsoleAction(String command) {
        super(command);
    }

    @Override
    protected void execute(Player player, Location blockLocation, String parsedCommand, TaskProcess taskProcess) {
        Scheduler.current().sync().runTask(() -> {
            Bukkit.dispatchCommand(player.getServer().getConsoleSender(), parsedCommand);
            taskProcess.next();
        });
    }
}
