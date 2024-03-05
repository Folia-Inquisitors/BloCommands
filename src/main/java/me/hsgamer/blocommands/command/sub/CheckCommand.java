package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.ActionBlock;
import me.hsgamer.blocommands.manager.BlockManager;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class CheckCommand extends SubCommand {
    private final BloCommands plugin;

    public CheckCommand(BloCommands plugin) {
        super("check", "Check if the looking block is registered", "/<label> check", null, false);
        this.plugin = plugin;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, 10);
        Optional<ActionBlock> optional = plugin.get(BlockManager.class).getBlock(block.getLocation());
        if (optional.isPresent()) {
            ActionBlock actionBlock = optional.get();
            MessageUtils.sendMessage(sender, "&aThe block is registered as &e" + actionBlock.getId());
        } else {
            MessageUtils.sendMessage(sender, "&cThe block is not registered");
        }
    }
}
