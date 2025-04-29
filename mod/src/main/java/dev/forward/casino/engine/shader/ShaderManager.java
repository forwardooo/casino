package dev.forward.casino.engine.shader;

import dev.forward.casino.engine.Engine;
import dev.forward.casino.util.FastAccess;

import java.util.HashMap;
import java.util.Map;

public class ShaderManager implements FastAccess {
    private final Map<String, ShaderProgram> programs = new HashMap<String, ShaderProgram>();

    public ShaderManager() {
        this.generate("carved", "/shader/vertex.vsh", "/shader/carved/carved.fsh");
        this.generate("textured_carved", "/shader/vertex.vsh", "/shader/carved/textured_carved.fsh");
        this.generate("shadow", "/shader/vertex.vsh", "/shader/shadow/shadow.fsh");
        this.generate("blur", "/shader/vertex.vsh", "/shader/blur/blur.fsh");
        this.generate("uv_texture", "/shader/vertex.vsh", "/shader/texture/uv_texture.fsh");
    }

    public void generate(String name, String vertexShaderFile, String fragmentShaderFile) {
        ShaderProgram program = new ShaderProgram();
        this.programs.put(name, program);
        try {
            program.generate(vertexShaderFile, fragmentShaderFile);
        }
        catch (Exception e) {
            log(String.format("Create shader program %s error: %s", name, e.getMessage()));
            program.delete();
        }
    }

    public void unload() {
        for (ShaderProgram program : this.programs.values()) {
            program.delete();
        }
        this.programs.clear();
    }

    public ShaderProgram get(String name) {
        return this.programs.get(name);
    }

    public ShaderProgram getCarvedProgram() {
        return this.get("carved");
    }

    public ShaderProgram getTexturedCarvedProgram() {
        return this.get("textured_carved");
    }

    public ShaderProgram getShadowProgram() {
        return this.get("shadow");
    }

    public ShaderProgram getBlurProgram() {
        return this.get("blur");
    }

    public ShaderProgram getUVTextureProgram() {
        return this.get("uv_texture");
    }
}
