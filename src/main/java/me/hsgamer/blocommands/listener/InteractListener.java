package me.hsgamer.blocommands.listener;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.listener.ListenerComponent;
import me.hsgamer.blocommands.api.block.ActionBlock;
import me.hsgamer.blocommands.api.block.BlockInteractType;
import me.hsgamer.blocommands.manager.BlockManager;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Optional;

public class InteractListener implements ListenerComponent {
    private final BasePlugin plugin;

    public InteractListener(BasePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public BasePlugin getPlugin() {
        return plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;

        Player player = event.getPlayer();
        Location location = block.getLocation();

        Optional<ActionBlock> optional = plugin.get(BlockManager.class).getBlock(location);
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
