package dev.forward.casino.engine.vbo.object;

public interface IBufferObject {
    public void generate();

    public void delete();

    public void bind();

    public void unBind();

    public void draw();
}
