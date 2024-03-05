package me.hsgamer.blocommands.action;

import io.github.projectunified.minelib.scheduler.entity.EntityScheduler;
import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerAction extends CommandAction {
    public PlayerAction(String command) {
        super(command);
    }

    @Override
    protected void execute(Player player, Location blockLocation, String parsedCommand, TaskProcess taskProcess) {
        EntityScheduler.get(JavaPlugin.getPlugin(BloCommands.class), player).run(() -> {
            player.performCommand(parsedCommand);
            taskProcess.next();
        });
    }
}
