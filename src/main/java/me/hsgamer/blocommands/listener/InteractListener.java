package me.hsgamer.blocommands.listener;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.ActionBlock;
import me.hsgamer.blocommands.api.block.BlockInteractType;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class InteractListener implements Listener {
    private final BloCommands plugin;

    public InteractListener(BloCommands plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;

        Player player = event.getPlayer();
        Location location = block.getLocation();

        Optional<ActionBlock> optional = plugin.getBlockManager().getBlock(location);
        if (!optional.isPresent()) return;
        ActionBlock actionBlock = optional.get();

        Action action = event.getAction();
        if (action == Action.PHYSICAL) {
            actionBlock.execute(player, BlockInteractType.PHYSICAL);
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            event.setCancelled(true);
            actionBlock.execute(player, BlockInteractType.RIGHT_CLICK);
        } else if (action == Action.LEFT_CLICK_BLOCK) {
            event.setCancelled(true);
            actionBlock.execute(player, BlockInteractType.LEFT_CLICK);
        }
    }
}
