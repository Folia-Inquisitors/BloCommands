package me.hsgamer.blocommands.manager;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.block.BlockLocation;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.Config;
import org.bukkit.Location;

import java.util.*;

public class BlockManager {
    private final Config config;
    private final Map<String, BlockLocation> byNameMap = new HashMap<>();
    private final Map<Location, BlockLocation> byLocationMap = new HashMap<>();

    public BlockManager(BloCommands plugin) {
        this.config = new BukkitConfig(plugin, "blocks.yml");
    }

    public void setup() {
        config.setup();
        loadBlockByName();
        loadBlockByLocation();
    }

    public void reload() {
        clearBlocks();
        config.reload();
        loadBlockByName();
        loadBlockByLocation();
    }

    public void save() {
        // TODO: Save blocks

        config.save();
        clearBlocks();
    }

    public void clearBlocks() {
        byNameMap.clear();
        byLocationMap.clear();
    }

    private void loadBlockByName() {
        // TODO: Load blocks
    }

    private void loadBlockByLocation() {
        byNameMap.values().forEach(blockLocation -> {
            Location location = blockLocation.getLocation();
            if (location != null) {
                byLocationMap.put(location, blockLocation);
            }
        });
    }

    public Collection<BlockLocation> getBlocks() {
        return Collections.unmodifiableCollection(byNameMap.values());
    }

    public Optional<BlockLocation> getBlock(String name) {
        return Optional.ofNullable(byNameMap.get(name));
    }

    public Optional<BlockLocation> getBlock(Location location) {
        return Optional.ofNullable(byLocationMap.get(location));
    }

    public BlockLocation createBlock(String name, Location location) {
        BlockLocation blockLocation = new BlockLocation(name);
        blockLocation.setLocation(location);
        byNameMap.put(name, blockLocation);
        loadBlockByLocation();
        return blockLocation;
    }

    public void removeBlock(String name) {
        byNameMap.remove(name);
        loadBlockByLocation();
    }
}
