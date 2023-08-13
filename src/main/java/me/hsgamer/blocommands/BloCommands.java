package me.hsgamer.blocommands;

import me.hsgamer.blocommands.command.MainCommand;
import me.hsgamer.blocommands.listener.InteractListener;
import me.hsgamer.blocommands.manager.ActionManager;
import me.hsgamer.blocommands.manager.BlockManager;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;

import java.util.Collections;
import java.util.List;

public final class BloCommands extends BasePlugin {
    private final ActionManager actionManager = new ActionManager();
    private final BlockManager blockManager = new BlockManager(this);

    @Override
    public void preLoad() {
        MessageUtils.setPrefix("&8[&eBloCommands&8] ");
    }

    @Override
    public void enable() {
        registerListener(new InteractListener(this));
        registerCommand(new MainCommand(this));
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

    @Override
    protected List<Class<?>> getPermissionClasses() {
        return Collections.singletonList(Permissions.class);
    }

    public ActionManager getActionManager() {
        return actionManager;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }
}
