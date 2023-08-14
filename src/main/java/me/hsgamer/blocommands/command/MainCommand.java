package me.hsgamer.blocommands.command;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.Permissions;
import me.hsgamer.blocommands.command.sub.*;
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
        commandManager.registerSubcommand(new AddActionCommand(plugin));
        commandManager.registerSubcommand(new SetActionCommand(plugin));
        commandManager.registerSubcommand(new RemoveActionCommand(plugin));
        commandManager.registerSubcommand(new ClearActionCommand(plugin));
        commandManager.registerSubcommand(new StatusCommand(plugin));
        commandManager.registerSubcommand(new CheckCommand(plugin));
        commandManager.registerSubcommand(new CheckWorldCommand(plugin));
        commandManager.registerSubcommand(new PurgeWorldCommand(plugin));
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
