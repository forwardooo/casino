package dev.forward.casino.util;

import dev.forward.casino.engine.Engine;
import dev.forward.casino.event.impl.render.RenderTickPre;
import dev.forward.casino.util.math.MutableV3;
import lombok.Getter;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;

public class GameSettings implements FastAccess{
    public GameSettings() {
        RenderTickPre.BUS.register((render) -> {
            updateSettings();
        });

    }
    private void updateSettings() {
        updatePlayerPosition();
    }
    private void updatePlayerPosition() {
        if (player == null) {
            return;
        }
        double partialTicks = mc.getTickDelta();
        this.pos.set(this.interpolate(player.getX(), player.lastRenderX, partialTicks), this.interpolate(player.getY(), player.lastRenderY, partialTicks), this.interpolate(player.getZ(), player.lastRenderZ, partialTicks));
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        cameraPosition.set(camera.x, camera.y, camera.z);
    }
    private double interpolate(double current, double last, double partialTicks) {
        return (current - last) * partialTicks + last;
    }
    private final MutableV3 pos = new MutableV3();
    @Getter
    private final MutableV3 cameraPosition = new MutableV3();
    public MutableV3 getInterpolatedPlayerPos() {
        return this.pos;
    }
}
