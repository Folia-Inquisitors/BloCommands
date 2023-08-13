package me.hsgamer.blocommands;

import me.hsgamer.blocommands.config.MainConfig;
import me.hsgamer.blocommands.config.MessageConfig;
import me.hsgamer.blocommands.listener.InteractListener;
import me.hsgamer.blocommands.manager.ActionManager;
import me.hsgamer.blocommands.manager.BlockManager;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.config.BukkitConfig;
import me.hsgamer.hscore.config.proxy.ConfigGenerator;

public final class BloCommands extends BasePlugin {
    private final MainConfig mainConfig = ConfigGenerator.newInstance(MainConfig.class, new BukkitConfig(this));
    private final MessageConfig messageConfig = ConfigGenerator.newInstance(MessageConfig.class, new BukkitConfig(this, "messages.yml"));
    private final ActionManager actionManager = new ActionManager();
    private final BlockManager blockManager = new BlockManager(this);

    @Override
    public void enable() {
        registerListener(new InteractListener(this));
    }

    @Override
    public void postEnable() {
        blockManager.setup();
    }

    @Override
    public void disable() {
        blockManager.save();
        blockManager.clearBlocks();
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public MessageConfig getMessageConfig() {
        return messageConfig;
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
}
