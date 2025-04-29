package dev.forward.casino.engine.vbo.object;

import dev.forward.casino.engine.vbo.format.VerticesFormat;
import dev.forward.casino.util.render.GLUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class VertexArrayBufferObject
        implements IBufferObject {
    private final VerticesFormat format;
    private final int mode;
    private final float[] vertices;
    private final int[] indices;
    private int vao = -1;
    private int vbo = -1;
    private int ebo = -1;

    @Override
    public void generate() {
        this.delete();
        this.vao = GL30.glGenVertexArrays();
        this.vbo = GL15.glGenBuffers();
        this.ebo = GL15.glGenBuffers();
        GL30.glBindVertexArray(this.vao);
        GL15.glBindBuffer(34962, this.vbo);
        GL15.glBufferData(34962, GLUtils.wrapFloatBuffer(this.vertices), 35044);
        this.format.setup();
        GL15.glBindBuffer(34963, this.ebo);
        GL15.glBufferData(34963, GLUtils.wrapIntBuffer(this.indices), 35044);
        this.unBind();
        GL15.glBindBuffer(34962, 0);
        GL15.glBindBuffer(34963, 0);
    }

    @Override
    public void delete() {
        if (this.vao != -1) {
            GL30.glDeleteVertexArrays(this.vao);
            this.vao = -1;
        }
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
        if (this.vao == -1) {
            return;
        }
        GL30.glBindVertexArray(this.vao);
    }

    @Override
    public void unBind() {
        GL30.glBindVertexArray(0);
    }

    @Override
    public void draw() {
        GL20.glDrawArrays(this.mode, 0, this.indices.length);
    }

    public VertexArrayBufferObject(VerticesFormat format, int mode, float[] vertices, int[] indices) {
        this.format = format;
        this.mode = mode;
        this.vertices = vertices;
        this.indices = indices;
    }
}
