package dev.forward.casino.command;

import dev.forward.casino.Casino;
import dev.forward.casino.util.FastAccess;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public abstract class CustomCommand implements CommandExecutor, TabCompleter, FastAccess {
    private final String commandName;
    private final boolean consoleAccess;
    public CustomCommand(String commandName, boolean consoleAccess) {
        this.commandName = commandName;
        this.consoleAccess = consoleAccess;
    }
    public void register(JavaPlugin plugin) {
        PluginCommand command = plugin.getCommand(commandName);
        if (command != null) {
            command.setExecutor(this);
            command.setTabCompleter(this);
        } else {
            Casino.getInstance().getLogger().warning(String.format("Command not %s not exists!", commandName));
        }

    }
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String string, String[] strings) {
        Arguments args = new Arguments(strings);
        CraftPlayer player = isPlayer(commandSender);
        if(player == null && !consoleAccess) {
            commandSender.sendMessage("Cannot execute this command as console!");
            return false;
        }
        return onCommand(commandSender, player, command, args);
    }
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        Arguments args = new Arguments(strings);
        CraftPlayer player = isPlayer(commandSender);
        if(player == null) return Collections.emptyList();
        return onTabComplete(commandSender, player, command, args);
    }
    public abstract boolean onCommand(CommandSender sender, CraftPlayer player, Command command, Arguments args);

    public abstract List<String> onTabComplete(CommandSender sender, CraftPlayer player, Command command, Arguments args);
}
