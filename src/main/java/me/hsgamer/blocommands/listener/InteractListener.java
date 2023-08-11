package me.hsgamer.blocommands.listener;

import me.hsgamer.blocommands.BloCommands;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractListener implements Listener {
    private final BloCommands plugin;

    public InteractListener(BloCommands plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // TODO
    }
}
