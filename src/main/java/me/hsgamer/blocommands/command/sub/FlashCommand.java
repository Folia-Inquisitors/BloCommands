package me.hsgamer.blocommands.command.sub;

import me.hsgamer.blocommands.BloCommands;
import me.hsgamer.blocommands.manager.FlashManager;
import me.hsgamer.hscore.bukkit.command.sub.SubCommand;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FlashCommand extends SubCommand {
    private final BloCommands plugin;

    public FlashCommand(BloCommands plugin) {
        super("flash", "Make the blocks glowing", "/<label> flash", null, false);
        this.plugin = plugin;
    }

    @Override
    public void onSubCommand(@NotNull CommandSender sender, @NotNull String label, @NotNull String... args) {
        Player player = (Player) sender;
        FlashManager flashManager = plugin.get(FlashManager.class);
        if (flashManager.isFlashing(player)) {
            flashManager.stop(player);
            MessageUtils.sendMessage(player, "&aFlash stopped");
        } else {
            flashManager.start(player);
            MessageUtils.sendMessage(player, "&aFlash started");
        }
    }
}
