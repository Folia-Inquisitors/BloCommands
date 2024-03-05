package me.hsgamer.blocommands;

import io.github.projectunified.minelib.plugin.base.BasePlugin;
import io.github.projectunified.minelib.plugin.command.CommandComponent;
import io.github.projectunified.minelib.plugin.postenable.PostEnableComponent;
import me.hsgamer.blocommands.command.MainCommand;
import me.hsgamer.blocommands.listener.InteractListener;
import me.hsgamer.blocommands.manager.ActionManager;
import me.hsgamer.blocommands.manager.BlockManager;
import me.hsgamer.blocommands.manager.FlashManager;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.Command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class BloCommands extends BasePlugin {
    @Override
    protected List<Object> getComponents() {
        return Arrays.asList(
                new Permissions(this),
                new PostEnableComponent(this),

                new ActionManager(),
                new BlockManager(this),
                new FlashManager(this),

                new CommandComponent(this, this::getCommands),
                new InteractListener(this)
        );
    }

    @Override
    public void load() {
        MessageUtils.setPrefix("&8[&eBloCommands&8] ");
    }

    private List<Command> getCommands() {
        return Collections.singletonList(new MainCommand(this));
    }
}
