package me.hsgamer.blocommands;

import me.hsgamer.blocommands.listener.InteractListener;
import me.hsgamer.hscore.bukkit.baseplugin.BasePlugin;

public final class BloCommands extends BasePlugin {
    @Override
    public void enable() {
        registerListener(new InteractListener(this));
    }
}
