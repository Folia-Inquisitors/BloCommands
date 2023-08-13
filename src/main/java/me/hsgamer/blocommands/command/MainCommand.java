package me.hsgamer.blocommands.command;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.Permissions;
import me.hsgamer.hscore.bukkit.command.sub.SubCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class MainCommand extends Command {
    private final SubCommandManager commandManager;

    public MainCommand(BloCommands plugin) {
        super("blocommands", "BloCommands main command", "/blocommands", Collections.singletonList("bloc"));
        setPermission(Permissions.ADMIN.getName());
        commandManager = new SubCommandManager();
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!testPermission(commandSender)) {
            return false;
        }
        return commandManager.onCommand(commandSender, s, strings);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        return commandManager.onTabComplete(sender, alias, args);
    }
}
