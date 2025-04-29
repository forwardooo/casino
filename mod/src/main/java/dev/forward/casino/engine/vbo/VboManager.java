package dev.forward.casino.engine.vbo;

import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.vbo.format.VerticesFormat;
import dev.forward.casino.engine.vbo.object.IBufferObject;
import dev.forward.casino.engine.vbo.object.VertexArrayBufferObject;
import dev.forward.casino.engine.vbo.object.VertexBufferObject;
import dev.forward.casino.util.FastAccess;
import org.lwjgl.opengl.GL;

import java.util.HashMap;
import java.util.Map;

public class VboManager implements FastAccess {
    private final Map<String, IBufferObject> objects = new HashMap<String, IBufferObject>();
    private final boolean vaoSupport;

    public VboManager() {
        GL.createCapabilities();
        this.vaoSupport = GL.getCapabilities().OpenGL30;
        log("VAO support: " + this.vaoSupport);
        IBufferObject object = this.createBufferObject(VerticesFormat.VERTEX_UV, 7, new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f}, new int[]{0, 1, 2, 3});
        this.register(object, "quad");
    }

    public IBufferObject createBufferObject(VerticesFormat format, int mode, float[] vertices, int[] indices) {
        return this.vaoSupport ? new VertexArrayBufferObject(format, mode, vertices, indices) : new VertexBufferObject(format, mode, vertices, indices);
    }

    public void register(IBufferObject object, String name) {
        this.objects.put(name, object);
        object.generate();
    }

    public void unload() {
        for (IBufferObject object : this.objects.values()) {
            object.delete();
        }
        this.objects.clear();
    }

    public IBufferObject get(String name) {
        return this.objects.get(name);
    }

    public IBufferObject getQuadObject() {
        return this.get("quad");
    }
}
