package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.ActionBlock;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveCommand extends BlockCommand {
    public RemoveCommand(BloCommands plugin) {
        super(plugin, "remove", "Remove the block", "", true);
    }

    @Override
    protected boolean onSubCommand(@NotNull CommandSender sender, @NotNull ActionBlock actionBlock, @NotNull String label, @NotNull String... args) {
        plugin.getBlockManager().removeBlock(actionBlock.getId());
        MessageUtils.sendMessage(sender, "&aThe block has been removed");
        return false;
    }
}
