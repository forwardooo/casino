package dev.forward.casino.util;

import dev.forward.casino.engine.Engine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.util.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface FastAccess {
    Logger LOGGER = LogManager.getLogger("Engine");
    MinecraftClient mc = MinecraftClient.getInstance();
    Tessellator tessellator = Tessellator.getInstance();
    Window window = mc.getWindow();
    ClientPlayerEntity player = mc.player;
    default void log(String msg) {
        LOGGER.info(msg);
    }
    default float getPartialTicks() {
        return mc.getTickDelta();
    }
}
