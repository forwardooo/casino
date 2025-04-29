package dev.forward.casino.modules;

import dev.forward.casino.Casino;
import dev.forward.casino.event.EventBus;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataModule extends Module {
    @Override
    public void load() {
        EventBus.of(PlayerJoinEvent.class).register((PlayerJoinEvent join) -> loadPlayerData(join.getPlayer()));
        EventBus.of(PlayerQuitEvent.class).register((quit) -> savePlayerData(quit.getPlayer()));
    }
    @Override
    public void unload() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            savePlayerData(player);
        }
    }

    private final Map<UUID, FileConfiguration> playerData = new HashMap<>();
    @SneakyThrows
    public void loadPlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        File file = new File(Casino.getInstance().getDataFolder(), "players/" + uuid + ".yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        addDefault(config, "money", 1000.0);
        addDefault(config,"lastWin", 0);
        addDefault(config, "bet", 55);
        playerData.put(uuid, config);
    }
    @SneakyThrows
    public void savePlayerData(Player player) {
        UUID uuid = player.getUniqueId();
        File file = new File(Casino.getInstance().getDataFolder(), "players/" + uuid + ".yml");
        playerData.get(uuid).save(file);
        playerData.remove(player.getUniqueId());
    }

    public FileConfiguration get(Player player) {
        return playerData.get(player.getUniqueId());
    }
    private void addDefault(FileConfiguration config, String identifier, Object value) {
        if (!config.contains(identifier)) {
            config.set(identifier, value);
        }
    }
}
