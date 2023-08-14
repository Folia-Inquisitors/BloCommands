package me.hsgamer.blocommands.action;

import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MessageAction extends CommandAction {
    public MessageAction(String command) {
        super(command);
    }

    @Override
    protected void execute(Player player, Location blockLocation, String parsedCommand, TaskProcess taskProcess) {
        MessageUtils.sendMessage(player, parsedCommand, "");
        taskProcess.next();
    }
}
