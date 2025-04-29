package dev.forward.casino.engine.vbo.format;

import org.lwjgl.opengl.GL20;

public enum VerticesFormat {
    VERTEX(() -> {
        GL20.glVertexAttribPointer(0, 3, 5126, false, 12, 0L);
        GL20.glEnableVertexAttribArray(0);
    }, () -> GL20.glDisableVertexAttribArray(0)),
    VERTEX_UV(() -> {
        GL20.glVertexAttribPointer(0, 3, 5126, false, 20, 0L);
        GL20.glVertexAttribPointer(1, 2, 5126, false, 20, 12L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
    }, () -> {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
    }),
    VERTEX_UV_COLOR(() -> {
        GL20.glVertexAttribPointer(0, 3, 5126, false, 32, 0L);
        GL20.glVertexAttribPointer(1, 2, 5126, false, 32, 12L);
        GL20.glVertexAttribPointer(2, 3, 5126, false, 32, 20L);
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }, () -> {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
    });

    private final Runnable setupRunnable;
    private final Runnable unsetupRunnable;

    public void setup() {
        this.setupRunnable.run();
    }

    public void unSetup() {
        this.unsetupRunnable.run();
    }

    private VerticesFormat(Runnable setupRunnable, Runnable unsetupRunnable) {
        this.setupRunnable = setupRunnable;
        this.unsetupRunnable = unsetupRunnable;
    }
}
