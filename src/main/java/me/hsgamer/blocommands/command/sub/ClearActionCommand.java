package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ClearActionCommand extends ActionCommand {
    public ClearActionCommand(BloCommands plugin) {
        super(plugin, "clearaction", "Clear all actions of the bundle of the block", "", true);
    }

    @Override
    protected boolean onSubCommand(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args) {
        actionBundle.clearActions();
        MessageUtils.sendMessage(sender, "&aThe actions are cleared");
        return true;
    }
}
