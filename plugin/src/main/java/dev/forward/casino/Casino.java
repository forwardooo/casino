package dev.forward.casino;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import dev.forward.casino.event.EventCaller;
import dev.forward.casino.modules.CommandModule;
import dev.forward.casino.modules.Module;
import dev.forward.casino.modules.NetworkModule;
import dev.forward.casino.modules.PlayerDataModule;
import dev.forward.casino.util.FastAccess;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public final class Casino extends JavaPlugin implements FastAccess {
    private final Map<Class<? extends Module>, Module> modules = new HashMap<>();
    @Getter
    private static Casino instance;
    @Override
    public void onEnable() {
        instance = this;
        register(new NetworkModule());
        register(new CommandModule());
        register(new PlayerDataModule());
        this.getServer().getPluginManager().registerEvents(new EventCaller(), this);
    }
    private void register(Module module) {
        module.load();
        modules.put(module.getClass(), module);
    }
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> moduleClass) {
        return (T) modules.get(moduleClass);
    }
    @Override
    public void onDisable() {
        modules.values().forEach(Module::unload);
    }
}
