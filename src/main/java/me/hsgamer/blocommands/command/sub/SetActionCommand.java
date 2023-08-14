package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.Action;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;

public class SetActionCommand extends ActionCommand {
    public SetActionCommand(BloCommands plugin) {
        super(plugin, "setaction", "Set the action at the index of the bundle of the block", "<index> <action>", true);
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
        Optional<Action> optional = tryParseAction(sender, Arrays.copyOfRange(args, 1, args.length));
        if (!optional.isPresent()) {
            return false;
        }
        Action action = optional.get();
        if (!actionBundle.setAction(index, action)) {
            MessageUtils.sendMessage(sender, "&cThe index is out of range");
            return false;
        }
        MessageUtils.sendMessage(sender, "&aSuccessfully set the action");
        return true;
    }

    @Override
    protected boolean isProperUsage(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args) {
        return args.length >= 2;
    }
}
