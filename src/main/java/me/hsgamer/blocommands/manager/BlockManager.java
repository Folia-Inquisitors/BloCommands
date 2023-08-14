package me.hsgamer.blocommands.manager;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.Action;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.blocommands.api.block.BlockInteractType;
import me.hsgamer.blocommands.api.block.BlockLocation;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.common.CollectionUtils;
import me.hsgamer.hscore.config.Config;
import me.hsgamer.hscore.config.PathString;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BlockManager {
    private static final PathString BLOCKS_PATH = new PathString("blocks");
    private static final String LOCATION_PATH = "location";
    private static final String ACTION_PATH = "action";

    private final Config config;
    private final BloCommands plugin;
    private final Map<String, BlockLocation> byNameMap = new HashMap<>();
    private final Map<Location, BlockLocation> byLocationMap = new HashMap<>();

    public BlockManager(BloCommands plugin) {
        this.config = new BukkitConfig(plugin, "blocks.yml");
        this.plugin = plugin;
    }

    public void setup() {
        config.setup();
        loadBlocks();
    }

    public void reload() {
        clearBlocks();
        config.reload();
        loadBlocks();
    }

    public void save() {
        saveBlocks();
        config.save();
    }

    public void clearBlocks() {
        byNameMap.clear();
        byLocationMap.clear();
    }

    private void loadBlocks() {
        loadBlockByName(PathString.toPathMap(".", config.getNormalizedValues(BLOCKS_PATH, false)));
        loadBlockByLocation();
    }

    private void saveBlocks() {
        config.set(BLOCKS_PATH, saveBlockByName());
    }

    private void loadBlockByName(Map<String, Object> map) {
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String id = entry.getKey();
            Object rawValue = entry.getValue();
            if (rawValue instanceof Map) {
                Map<?, ?> settings = (Map<?, ?>) rawValue;
                BlockLocation blockLocation = new BlockLocation(id);

                try {
                    //noinspection unchecked
                    Map<String, Object> locationMap = (Map<String, Object>) settings.get(LOCATION_PATH);
                    Location location = Location.deserialize(locationMap);
                    blockLocation.setLocation(location);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.WARNING, "Error when loading location for " + id, e);
                }

                Object rawActionValue = settings.get(ACTION_PATH);
                if (rawActionValue instanceof Map) {
                    Map<?, ?> actionMap = (Map<?, ?>) rawActionValue;
                    for (Map.Entry<?, ?> actionEntry : actionMap.entrySet()) {
                        String rawInteractType = Objects.toString(actionEntry.getKey());
                        Object rawActionList = actionEntry.getValue();

                        BlockInteractType interactType;
                        try {
                            interactType = BlockInteractType.valueOf(rawInteractType.toUpperCase(Locale.ROOT));
                        } catch (Exception e) {
                            plugin.getLogger().log(Level.WARNING, "Invalid interact type for " + id, e);
                            continue;
                        }

                        List<String> stringActionList = CollectionUtils.createStringListFromObject(rawActionList);
                        List<Action> actionList = plugin.getActionManager().deserialize(stringActionList);
                        blockLocation.getActionBundle(interactType).addAction(actionList);
                    }
                }

                byNameMap.put(id, blockLocation);
            }
        }
    }

    private Map<String, Object> saveBlockByName() {
        Map<String, Object> map = new LinkedHashMap<>();

        for (BlockLocation blockLocation : byNameMap.values()) {
            Map<String, Object> settings = new LinkedHashMap<>();

            settings.put(LOCATION_PATH, Optional.ofNullable(blockLocation.getLocation()).map(Location::serialize).orElse(null));

            Map<String, Object> actionMap = new LinkedHashMap<>();
            for (Map.Entry<BlockInteractType, ActionBundle> actionEntry : blockLocation.getActionBundleMap().entrySet()) {
                List<Action> actionList = actionEntry.getValue().getActions();
                if (actionList.isEmpty()) continue;

                actionMap.put(actionEntry.getKey().name(), plugin.getActionManager().serialize(actionList));
            }
            settings.put(ACTION_PATH, actionMap);

            map.put(blockLocation.getId(), settings);
        }

        return map;
    }

    public void loadBlockByLocation() {
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

    public Collection<BlockLocation> getBlocks(World world) {
        return byNameMap.values().stream()
                .filter(blockLocation -> {
                    Location location = blockLocation.getLocation();
                    return location != null && Objects.equals(location.getWorld(), world);
                })
                .collect(Collectors.toList());
    }

    public Optional<BlockLocation> getBlock(String id) {
        return Optional.ofNullable(byNameMap.get(id));
    }

    public Optional<BlockLocation> getBlock(Location location) {
        return Optional.ofNullable(byLocationMap.get(location));
    }

    public void createBlock(String id) {
        BlockLocation blockLocation = new BlockLocation(id);
        byNameMap.put(id, blockLocation);
    }

    public void removeBlock(String id) {
        byNameMap.remove(id);
        loadBlockByLocation();
    }

    public void removeBlocks(World world) {
        for (BlockLocation blockLocation : getBlocks(world)) {
            byNameMap.remove(blockLocation.getId());
        }
        loadBlockByLocation();
    }
}
