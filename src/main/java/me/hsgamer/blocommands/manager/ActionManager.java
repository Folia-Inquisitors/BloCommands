package me.hsgamer.blocommands.manager;

import me.hsgamer.blocommands.action.ConsoleAction;
import me.hsgamer.blocommands.action.MessageAction;
import me.hsgamer.blocommands.action.PlayerAction;
import me.hsgamer.blocommands.api.action.Action;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ActionManager {
    private final List<ActionSerializer> actionSerializers = new ArrayList<>();

    public ActionManager() {
        registerAction(PlayerAction.class, PlayerAction::new, PlayerAction::getCommand, "player", "");
        registerAction(ConsoleAction.class, ConsoleAction::new, ConsoleAction::getCommand, "console");
        registerAction(MessageAction.class, MessageAction::new, MessageAction::getCommand, "message");
    }

    public <T extends Action> void registerAction(Class<T> clazz, Function<String, T> deserializer, Function<T, String> serializer, String... name) {
        actionSerializers.add(new ActionSerializer(clazz, name, deserializer::apply, action -> {
            try {
                return serializer.apply(clazz.cast(action));
            } catch (ClassCastException e) {
                throw new IllegalArgumentException("The action is not " + clazz.getName());
            }
        }));
    }

    public Optional<Action> deserialize(String command) {
        String[] split = command.split(":", 2);
        String actionName;
        String actionCommand;
        if (split.length > 1) {
            actionName = split[0].trim();
            actionCommand = split[1].trim();
        } else {
            actionName = "";
            actionCommand = command.trim();
        }
        return actionSerializers.stream()
                .filter(actionSerializer -> {
                    for (String name : actionSerializer.name) {
                        if (name.equalsIgnoreCase(actionName)) {
                            return true;
                        }
                    }
                    return false;
                })
                .map(actionSerializer -> actionSerializer.deserializer.apply(actionCommand))
                .findFirst();
    }

    public List<Action> deserialize(List<String> commands) {
        List<Action> actions = new ArrayList<>();
        commands.forEach(command -> deserialize(command).ifPresent(actions::add));
        return actions;
    }

    public Optional<String> serialize(Action action) {
        return actionSerializers.stream()
                .filter(actionSerializer -> actionSerializer.clazz.isInstance(action))
                .map(actionSerializer -> {
                    try {
                        return actionSerializer.name[0] + ": " + actionSerializer.serializer.apply(actionSerializer.clazz.cast(action));
                    } catch (ClassCastException e) {
                        throw new IllegalArgumentException("The action is not " + actionSerializer.clazz.getName());
                    }
                })
                .findFirst();
    }

    public List<String> serialize(List<Action> actions) {
        List<String> commands = new ArrayList<>();
        actions.forEach(action -> serialize(action).ifPresent(commands::add));
        return commands;
    }

    private static class ActionSerializer {
        private final Class<? extends Action> clazz;
        private final String[] name;
        private final Function<String, Action> deserializer;
        private final Function<Action, String> serializer;

        private ActionSerializer(Class<? extends Action> clazz, String[] name, Function<String, Action> deserializer, Function<Action, String> serializer) {
            this.clazz = clazz;
            this.name = name;
            this.deserializer = deserializer;
            this.serializer = serializer;
        }
    }
}
