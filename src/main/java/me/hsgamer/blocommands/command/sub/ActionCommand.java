package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.api.action.Action;
import me.hsgamer.blocommands.api.action.ActionBundle;
import me.hsgamer.blocommands.api.block.BlockInteractType;
import me.hsgamer.blocommands.api.block.BlockLocation;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class ActionCommand extends BlockCommand {
    public ActionCommand(BloCommands plugin, @NotNull String name, @NotNull String description, @NotNull String argsUsage, boolean consoleAllowed) {
        super(plugin, name, description, "<type>" + (argsUsage.isEmpty() ? "" : " " + argsUsage), consoleAllowed);
    }

    protected abstract boolean onSubCommand(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args);

    protected boolean isProperUsage(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args) {
        return true;
    }

    protected List<String> onTabComplete(@NotNull CommandSender sender, @NotNull ActionBundle actionBundle, @NotNull String label, @NotNull String... args) {
        return Collections.emptyList();
    }

    @Override
    protected final boolean onSubCommand(@NotNull CommandSender sender, @NotNull BlockLocation blockLocation, @NotNull String label, @NotNull String... args) {
        if (args.length == 0) {
            return false;
        }
        BlockInteractType type;
        try {
            type = BlockInteractType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            MessageUtils.sendMessage(sender, "&cInvalid type");
            return false;
        }
        ActionBundle actionBundle = blockLocation.getActionBundle(type);
        return onSubCommand(sender, actionBundle, label, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    protected final boolean isProperUsage(@NotNull CommandSender sender, @NotNull BlockLocation blockLocation, @NotNull String label, @NotNull String... args) {
        if (args.length == 0) {
            return false;
        }
        BlockInteractType type;
        try {
            type = BlockInteractType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return true;
        }
        ActionBundle actionBundle = blockLocation.getActionBundle(type);
        return isProperUsage(sender, actionBundle, label, Arrays.copyOfRange(args, 1, args.length));
    }

    @Override
    protected final List<String> onTabComplete(@NotNull CommandSender sender, @NotNull BlockLocation blockLocation, @NotNull String label, @NotNull String... args) {
        if (args.length == 1) {
            return Arrays.stream(BlockInteractType.values()).map(Enum::name).collect(Collectors.toList());
        }
        BlockInteractType type;
        try {
            type = BlockInteractType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return Collections.emptyList();
        }
        ActionBundle actionBundle = blockLocation.getActionBundle(type);
        return onTabComplete(sender, actionBundle, label, Arrays.copyOfRange(args, 1, args.length));
    }

    protected final Optional<Action> tryParseAction(CommandSender sender, @NotNull String... args) {
        String actionString = String.join(" ", args);
        Optional<Action> optional = plugin.getActionManager().deserialize(actionString);
        if (!optional.isPresent()) {
            MessageUtils.sendMessage(sender, "&cThe action is invalid");
        }
        return optional;
    }
}
