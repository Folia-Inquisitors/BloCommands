package me.hsgamer.blocommands.command;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.Permissions;
import me.hsgamer.blocommands.command.sub.CreateCommand;
import me.hsgamer.blocommands.command.sub.LocationCommand;
import me.hsgamer.blocommands.command.sub.RemoveCommand;
import me.hsgamer.hscore.bukkit.command.sub.SubCommandManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MainCommand extends Command {
    private final SubCommandManager commandManager;

    public MainCommand(BloCommands plugin) {
        super("blocommands", "BloCommands main command", "/blocommands", Collections.singletonList("bloc"));
        setPermission(Permissions.ADMIN.getName());
        commandManager = new SubCommandManager();
        commandManager.registerSubcommand(new CreateCommand(plugin));
        commandManager.registerSubcommand(new RemoveCommand(plugin));
        commandManager.registerSubcommand(new LocationCommand(plugin));
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String label, String[] args) {
        if (!testPermission(commandSender)) {
            return false;
        }
        return commandManager.onCommand(commandSender, label, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String[] args) throws IllegalArgumentException {
        return commandManager.onTabComplete(sender, label, args);
    }
}
