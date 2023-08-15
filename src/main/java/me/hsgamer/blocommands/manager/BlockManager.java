package me.hsgamer.blocommands.manager;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.Action;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.blocommands.api.block.ActionBlock;
import me.hsgamer.blocommands.api.block.BlockInteractType;
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
    private final Map<String, ActionBlock> byNameMap = new HashMap<>();
    private final Map<Location, ActionBlock> byLocationMap = new HashMap<>();
    private long lastLocationUpdate = 0L;

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
                ActionBlock actionBlock = new ActionBlock(id);

                try {
                    //noinspection unchecked
                    Map<String, Object> locationMap = (Map<String, Object>) settings.get(LOCATION_PATH);
                    Location location = Location.deserialize(locationMap);
                    actionBlock.setLocation(location);
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
                        actionBlock.getActionBundle(interactType).addAction(actionList);
                    }
                }

                byNameMap.put(id, actionBlock);
            }
        }
    }

    private Map<String, Object> saveBlockByName() {
        Map<String, Object> map = new LinkedHashMap<>();

        for (ActionBlock actionBlock : byNameMap.values()) {
            Map<String, Object> settings = new LinkedHashMap<>();

            settings.put(LOCATION_PATH, Optional.ofNullable(actionBlock.getLocation()).map(Location::serialize).orElse(null));

            Map<String, Object> actionMap = new LinkedHashMap<>();
            for (Map.Entry<BlockInteractType, ActionBundle> actionEntry : actionBlock.getActionBundleMap().entrySet()) {
                List<Action> actionList = actionEntry.getValue().getActions();
                if (actionList.isEmpty()) continue;

                actionMap.put(actionEntry.getKey().name(), plugin.getActionManager().serialize(actionList));
            }
            settings.put(ACTION_PATH, actionMap);

            map.put(actionBlock.getId(), settings);
        }

        return map;
    }

    public void loadBlockByLocation() {
        byLocationMap.clear();
        byNameMap.values().forEach(actionBlock -> {
            Location location = actionBlock.getLocation();
            if (location != null) {
                byLocationMap.put(location, actionBlock);
            }
        });
        lastLocationUpdate = System.currentTimeMillis();
    }

    public Collection<ActionBlock> getBlocks() {
        return Collections.unmodifiableCollection(byNameMap.values());
    }

    public Map<Location, ActionBlock> getBlocksByLocation() {
        return Collections.unmodifiableMap(byLocationMap);
    }

    public long getLastLocationUpdate() {
        return lastLocationUpdate;
    }

    public Collection<ActionBlock> getBlocks(World world) {
        return byNameMap.values().stream()
                .filter(actionBlock -> {
                    Location location = actionBlock.getLocation();
                    return location != null && Objects.equals(location.getWorld(), world);
                })
                .collect(Collectors.toList());
    }

    public Optional<ActionBlock> getBlock(String id) {
        return Optional.ofNullable(byNameMap.get(id));
    }

    public Optional<ActionBlock> getBlock(Location location) {
        return Optional.ofNullable(byLocationMap.get(location));
    }

    public ActionBlock createBlock(String id) {
        ActionBlock actionBlock = new ActionBlock(id);
        byNameMap.put(id, actionBlock);
        return actionBlock;
    }

    public void removeBlock(String id) {
        byNameMap.remove(id);
        loadBlockByLocation();
    }

    public void removeBlocks(World world) {
        for (ActionBlock actionBlock : getBlocks(world)) {
            byNameMap.remove(actionBlock.getId());
        }
        loadBlockByLocation();
    }
}
