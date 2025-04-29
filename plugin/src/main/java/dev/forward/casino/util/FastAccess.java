package dev.forward.casino.util;

import dev.forward.casino.Casino;
import dev.forward.casino.modules.PlayerDataModule;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.logging.Logger;

public interface FastAccess {
    String PREFIX = "§x§F§F§5§5§5§5[§x§F§F§6§6§4§4К§x§F§F§7§7§3§3а§x§F§F§8§8§2§2з§x§F§F§9§9§1§1и§x§F§F§A§A§0§0н§x§F§F§A§A§0§0о§x§F§F§A§A§0§0] §r";
    Logger LOGGER = Logger.getLogger("Casino");

    default CraftPlayer isPlayer(CommandSender commandSender) {
        if(commandSender instanceof Player) {
            return (CraftPlayer) commandSender;
        }
        return null;
    }

    default void sendMessage(CommandSender player, Object message) {
        player.sendMessage(PREFIX + message);
    }

    default <T> void setData(Player player, String identifier,T data) {
        Casino.getInstance().getModule(PlayerDataModule.class).get(player).set(identifier, data);
    }

    default void log(Object msg) {
        LOGGER.info(String.valueOf(msg));
    }

    @SuppressWarnings("unchecked")
    default <T> T getData(Player player, String identifier) {
        return (T) Casino.getInstance().getModule(PlayerDataModule.class).get(player).get(identifier);
    }

    default Player getPlayer(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(name)) return player;
        }
        return null;
    }

    default Player getPlayer(UUID uuid) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getUniqueId().equals(uuid)) return player;
        }
        return null;
    }

    default void execute(Runnable task) {
        Bukkit.getScheduler().runTask(Casino.getInstance(), task);
    }
}
