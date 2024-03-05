package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.ActionBlock;
import me.hsgamer.blocommands.manager.BlockManager;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CheckWorldCommand extends SubCommand {
    private final BloCommands plugin;

    public CheckWorldCommand(BloCommands plugin) {
        super("checkworld", "Get the list of IDs of the blocks in the world", "/<label> checkworld <world>", null, true);
        this.plugin = plugin;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        World world = Bukkit.getWorld(args[0]);
        if (world == null) {
            MessageUtils.sendMessage(sender, "&cThe world is not found");
            return;
        }
        Collection<ActionBlock> actionBlocks = plugin.get(BlockManager.class).getBlocks(world);
        if (actionBlocks.isEmpty()) {
            MessageUtils.sendMessage(sender, "&cThere is no block in the world");
        } else {
            MessageUtils.sendMessage(sender, "&aThe blocks in the world:");
            actionBlocks.forEach(actionBlock -> MessageUtils.sendMessage(sender, "&e- &f" + actionBlock.getId()));
        }
    }

    @Override
    public boolean isProperUsage(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        return args.length >= 1;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return Bukkit.getWorlds().stream().map(world -> world.getName().toLowerCase()).collect(Collectors.toList());
        }
        return super.onTabComplete(sender, label, args);
    }
}
