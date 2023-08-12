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
        registerAction(PlayerAction.class, "player", PlayerAction::new, PlayerAction::getCommand);
        registerAction(PlayerAction.class, "", PlayerAction::new, PlayerAction::getCommand);
        registerAction(ConsoleAction.class, "console", ConsoleAction::new, ConsoleAction::getCommand);
        registerAction(MessageAction.class, "message", MessageAction::new, MessageAction::getCommand);
    }

    public <T extends Action> void registerAction(Class<T> clazz, String name, Function<String, T> deserializer, Function<T, String> serializer) {
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
            actionName = split[0];
            actionCommand = split[1];
        } else {
            actionName = "";
            actionCommand = command;
        }
        return actionSerializers.stream()
                .filter(actionSerializer -> actionSerializer.name.equalsIgnoreCase(actionName))
                .map(actionSerializer -> actionSerializer.deserializer.apply(actionCommand))
                .findFirst();
    }

    public Optional<String> serialize(Action action) {
        return actionSerializers.stream()
                .filter(actionSerializer -> actionSerializer.clazz.isInstance(action))
                .map(actionSerializer -> actionSerializer.serializer.apply(action))
                .findFirst();
    }

    private static class ActionSerializer {
        private final Class<? extends Action> clazz;
        private final String name;
        private final Function<String, Action> deserializer;
        private final Function<Action, String> serializer;

        private ActionSerializer(Class<? extends Action> clazz, String name, Function<String, Action> deserializer, Function<Action, String> serializer) {
            this.clazz = clazz;
            this.name = name;
            this.deserializer = deserializer;
            this.serializer = serializer;
        }
    }
}
