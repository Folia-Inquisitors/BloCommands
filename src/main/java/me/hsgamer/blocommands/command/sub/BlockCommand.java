package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.ActionBlock;
import me.hsgamer.blocommands.manager.BlockManager;
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

    protected abstract boolean onSubCommand(@NotNull CommandSender sender, @NotNull ActionBlock actionBlock, @NotNull String label, @NotNull String... args);

    protected boolean isProperUsage(@NotNull CommandSender sender, @NotNull ActionBlock actionBlock, @NotNull String label, @NotNull String... args) {
        return true;
    }

    protected List<String> onTabComplete(@NotNull CommandSender sender, @NotNull ActionBlock actionBlock, @NotNull String label, @NotNull String... args) {
        return Collections.emptyList();
    }

    @Override
    public final void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        String id = args[0];
        Optional<ActionBlock> optional = plugin.get(BlockManager.class).getBlock(id);
        if (!optional.isPresent()) {
            MessageUtils.sendMessage(sender, "&cThe block is not found");
            return;
        }
        ActionBlock actionBlock = optional.get();
        if (onSubCommand(sender, actionBlock, label, Arrays.copyOfRange(args, 1, args.length))) {
            plugin.get(BlockManager.class).save();
            plugin.get(BlockManager.class).loadBlockByLocation();
        }
    }

    @Override
    public final boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 0) {
            return false;
        }
        String id = args[0];
        Optional<ActionBlock> optional = plugin.get(BlockManager.class).getBlock(id);
        return optional.map(location -> isProperUsage(sender, location, label, Arrays.copyOfRange(args, 1, args.length))).orElse(true);
    }

    @Override
    public final @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 0) {
            return Collections.emptyList();
        }
        if (args.length == 1) {
            return plugin.get(BlockManager.class).getBlocks().stream().map(ActionBlock::getId).collect(Collectors.toList());
        }
        String id = args[0];
        Optional<ActionBlock> optional = plugin.get(BlockManager.class).getBlock(id);
        return optional.map(location -> onTabComplete(sender, location, label, Arrays.copyOfRange(args, 1, args.length))).orElse(Collections.emptyList());
    }
}
