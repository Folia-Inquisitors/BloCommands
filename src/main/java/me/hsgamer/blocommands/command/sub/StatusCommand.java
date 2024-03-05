package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.Action;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.blocommands.api.block.ActionBlock;
import me.hsgamer.blocommands.api.block.BlockInteractType;
import me.hsgamer.blocommands.manager.ActionManager;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class StatusCommand extends BlockCommand {
    public StatusCommand(BloCommands plugin) {
        super(plugin, "status", "Check the status of the block", "", true);
    }

    @Override
    protected boolean onSubCommand(@NotNull CommandSender sender, @NotNull ActionBlock actionBlock, @NotNull String label, @NotNull String... args) {
        Location location = actionBlock.getLocation();
        MessageUtils.sendMessage(sender, "&6Location: &f" + (
                location == null
                        ? "null"
                        : location.getWorld().getName() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ()
        ));
        Map<BlockInteractType, ActionBundle> actionBundleMap = actionBlock.getActionBundleMap();
        if (actionBundleMap.isEmpty()) {
            MessageUtils.sendMessage(sender, "&6No action");
        } else {
            MessageUtils.sendMessage(sender, "&6Actions:");
            actionBundleMap.forEach((type, bundle) -> {
                MessageUtils.sendMessage(sender, "&f- &b" + type.name());
                List<Action> actions = bundle.getActions();
                List<String> actionsString = plugin.get(ActionManager.class).serialize(actions);
                for (int i = 0; i < actionsString.size(); i++) {
                    MessageUtils.sendMessage(sender, "&c  " + (i) + ". &f" + actionsString.get(i));
                }
            });
        }
        return false;
    }
}
