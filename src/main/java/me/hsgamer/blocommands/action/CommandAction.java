package me.hsgamer.blocommands.action;

import me.hsgamer.blocommands.api.action.Action;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class CommandAction implements Action {
    private final String command;

    public CommandAction(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    protected abstract void execute(Player player, Location blockLocation, String parsedCommand, TaskProcess taskProcess);

    @Override
    public void execute(Player player, Location blockLocation, TaskProcess taskProcess) {
        String parsedCommand = command
                .replace("{player}", player.getName())
                .replace("{world}", blockLocation.getWorld().getName())
                .replace("{x}", String.valueOf(blockLocation.getBlockX()))
                .replace("{y}", String.valueOf(blockLocation.getBlockY()))
                .replace("{z}", String.valueOf(blockLocation.getBlockZ()));
        execute(player, blockLocation, parsedCommand, taskProcess);
    }
}
