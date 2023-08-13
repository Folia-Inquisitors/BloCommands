package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.BlockLocation;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LocationCommand extends BlockCommand {
    public LocationCommand(BloCommands plugin) {
        super(plugin, "location", "Set the location of the block to the looking block", "", false);
    }

    @Override
    protected boolean onSubCommand(@NotNull CommandSender sender, @NotNull BlockLocation blockLocation, @NotNull String label, @NotNull String... args) {
        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, 10);
        if (block.getType().isBlock()) {
            blockLocation.setLocation(block.getLocation());
            sender.sendMessage("§aThe location of the block has been set to the looking block");
            return true;
        } else {
            sender.sendMessage("§cYou are not looking at a block");
            return false;
        }
    }
}
