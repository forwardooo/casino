package dev.forward.casino.engine.vbo.object;

import dev.forward.casino.engine.vbo.format.VerticesFormat;
import dev.forward.casino.util.render.GLUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class VertexBufferObject
        implements IBufferObject {
    private final VerticesFormat format;
    private final int mode;
    private final float[] vertices;
    private final int[] indices;
    private int vbo = -1;
    private int ebo = -1;

    @Override
    public void generate() {
        this.delete();
        this.vbo = GL15.glGenBuffers();
        this.ebo = GL15.glGenBuffers();
        GL15.glBindBuffer(34962, this.vbo);
        GL15.glBufferData(34962, GLUtils.wrapFloatBuffer(this.vertices), 35044);
        this.format.setup();
        GL15.glBindBuffer(34963, this.ebo);
        GL15.glBufferData(34963, GLUtils.wrapIntBuffer(this.indices), 35044);
        this.unBind();
    }

    @Override
    public void delete() {
        if (this.vbo != -1) {
            GL15.glDeleteBuffers(this.vbo);
            this.vbo = -1;
        }
        if (this.ebo != -1) {
            GL15.glDeleteBuffers(this.ebo);
            this.ebo = -1;
        }
    }

    @Override
    public void bind() {
        if (this.vbo == -1 || this.ebo == -1) {
            return;
        }
        GL15.glBindBuffer(34962, this.vbo);
        GL15.glBindBuffer(34963, this.ebo);
        this.format.setup();
    }

    @Override
    public void unBind() {
        this.format.unSetup();
        GL15.glBindBuffer(34962, 0);
        GL15.glBindBuffer(34963, 0);
    }

    @Override
    public void draw() {
        GL11.glDrawElements(this.mode, this.indices.length, 5125, 0L);
    }

    public VertexBufferObject(VerticesFormat format, int mode, float[] vertices, int[] indices) {
        this.format = format;
        this.mode = mode;
        this.vertices = vertices;
        this.indices = indices;
    }
}
