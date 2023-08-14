package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.Action;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AddActionCommand extends ActionCommand {
    public AddActionCommand(BloCommands plugin) {
        super(plugin, "addaction", "Add an action to the block", "<action>", true);
    }

    @Override
    protected boolean onSubCommand(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args) {
        Optional<Action> optional = tryParseAction(sender, args);
        if (!optional.isPresent()) {
            return false;
        }
        Action action = optional.get();
        actionBundle.addAction(action);
        MessageUtils.sendMessage(sender, "&aThe action is added");
        return true;
    }

    @Override
    protected boolean isProperUsage(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args) {
        return args.length > 0;
    }
}
