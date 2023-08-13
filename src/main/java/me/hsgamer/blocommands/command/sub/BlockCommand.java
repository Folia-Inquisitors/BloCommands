package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.BlockLocation;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class BlockCommand extends SubCommand {
    protected final BloCommands plugin;

    public BlockCommand(BloCommands plugin, @NotNull String name, @NotNull String description, @NotNull String argsUsage, boolean consoleAllowed) {
        super(name, description, "/<label> " + name + " <id>" + (argsUsage.isEmpty() ? "" : " " + argsUsage), null, consoleAllowed);
        this.plugin = plugin;
    }

    protected abstract boolean onSubCommand(@NotNull CommandSender sender, @NotNull BlockLocation blockLocation, @NotNull String label, @NotNull String... args);

    protected boolean isProperUsage(@NotNull CommandSender sender, @NotNull BlockLocation blockLocation, @NotNull String label, @NotNull String... args) {
        return true;
    }

    protected List<String> onTabComplete(@NotNull CommandSender sender, @NotNull BlockLocation blockLocation, @NotNull String label, @NotNull String... args) {
        return Collections.emptyList();
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        String id = args[0];
        Optional<BlockLocation> optional = plugin.getBlockManager().getBlock(id);
        if (!optional.isPresent()) {
            MessageUtils.sendMessage(sender, "&cThe block is not found");
            return;
        }
        BlockLocation blockLocation = optional.get();
        if (onSubCommand(sender, blockLocation, label, Arrays.copyOfRange(args, 1, args.length))) {
            plugin.getBlockManager().save();
            plugin.getBlockManager().loadBlockByLocation();
        }
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 0) {
            return false;
        }
        String id = args[0];
        Optional<BlockLocation> blockLocation = plugin.getBlockManager().getBlock(id);
        return blockLocation.map(location -> isProperUsage(sender, location, label, Arrays.copyOfRange(args, 1, args.length))).orElse(true);
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return plugin.getBlockManager().getBlocks().stream().map(BlockLocation::getId).collect(Collectors.toList());
        }
        String id = args[0];
        Optional<BlockLocation> blockLocation = plugin.getBlockManager().getBlock(id);
        return blockLocation.map(location -> onTabComplete(sender, location, label, Arrays.copyOfRange(args, 1, args.length))).orElse(Collections.emptyList());
    }
}
