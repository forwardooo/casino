package dev.forward.casino.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventCaller implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        EventBus.of(PlayerJoinEvent.class).fire(event);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        EventBus.of(PlayerQuitEvent.class).fire(event);
    }
}
