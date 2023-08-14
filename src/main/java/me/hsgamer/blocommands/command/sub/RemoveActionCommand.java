package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveActionCommand extends ActionCommand {
    public RemoveActionCommand(BloCommands plugin) {
        super(plugin, "removeaction", "Remove an action at the index of the bundle of the block", "<index>", true);
    }

    @Override
    protected boolean onSubCommand(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args) {
        int index;
        try {
            index = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            MessageUtils.sendMessage(sender, "&cThe index must be a number");
            return false;
        }
        if (!actionBundle.removeAction(index)) {
            MessageUtils.sendMessage(sender, "&cThe index is out of range");
            return false;
        }
        MessageUtils.sendMessage(sender, "&aSuccessfully removed the action at index &e" + index);
        return true;
    }

    @Override
    protected boolean isProperUsage(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args) {
        return args.length >= 1;
    }
}
