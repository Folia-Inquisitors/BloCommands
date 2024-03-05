package me.hsgamer.blocommands.action;

import io.github.projectunified.minelib.scheduler.global.GlobalScheduler;
import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleAction extends CommandAction {
    public ConsoleAction(String command) {
        super(command);
    }

    @Override
    protected void execute(Player player, Location blockLocation, String parsedCommand, TaskProcess taskProcess) {
        GlobalScheduler.get(JavaPlugin.getPlugin(BloCommands.class)).run(() -> {
            Bukkit.dispatchCommand(player.getServer().getConsoleSender(), parsedCommand);
            taskProcess.next();
        });
    }
}
