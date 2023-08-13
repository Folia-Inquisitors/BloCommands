package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.BlockLocation;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RemoveCommand extends BlockCommand {
    public RemoveCommand(BloCommands plugin) {
        super(plugin, "remove", "Remove the block", "", true);
    }

    @Override
    protected boolean onSubCommand(@NotNull CommandSender sender, @NotNull BlockLocation blockLocation, @NotNull String label, @NotNull String... args) {
        plugin.getBlockManager().removeBlock(blockLocation.getId());
        return false;
    }
}
