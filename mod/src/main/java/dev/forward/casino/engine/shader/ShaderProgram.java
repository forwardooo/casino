package dev.forward.casino.engine.shader;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.render.GLUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL20;
public class ShaderProgram implements FastAccess {

    private final Map<String, Object> uniformVariables = new HashMap<String, Object>();
    private int vertexShader = -1;
    private int fragmentShader = -1;
    private int programId = -1;

    public void generate(String vertexShaderFile, String fragmentShaderFile) {
        this.createProgram(vertexShaderFile, fragmentShaderFile);
        this.use();
        this.uniform1i("image", 0);
        this.unUse();
    }

    private void createProgram(String vertexShaderFile, String fragmentShaderFile) {
        try {
            this.vertexShader = this.createShader(GL20.GL_VERTEX_SHADER, vertexShaderFile);
        }
        catch (Exception e) {
            throw new RuntimeException("Error on create vertex shader: " + e.getMessage());
        }
        try {
            this.fragmentShader = this.createShader(GL20.GL_FRAGMENT_SHADER, fragmentShaderFile);
        }
        catch (Exception e) {
            throw new RuntimeException("Error on create fragment shader: " + e.getMessage());
        }
        this.programId = GlStateManager.createProgram();
        GL20.glAttachShader(this.programId, this.vertexShader);
        GL20.glAttachShader(this.programId, this.fragmentShader);
        GL20.glBindAttribLocation(this.programId, 0, "modelViewMatrix");
        GL20.glBindAttribLocation(this.programId, 1, "projectMatrix");
        GL20.glLinkProgram(this.programId);
        this.vertexShader = -1;
        this.fragmentShader = -1;
        if (GL20.glGetProgrami(this.programId, GL20.GL_LINK_STATUS) == 0) {
            this.programId = -1;
            throw new RuntimeException("Can't compile program: ");
        }
    }

    private int createShader(int shaderType, String fileName) throws IOException {
        int shaderId = GL20.glCreateShader(shaderType);
        InputStream stream = mc.getResourceManager().getResource(new Identifier("casino",fileName)).getInputStream();
        String text = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).lines().collect(Collectors.joining("\n"));
        GL20.glShaderSource(shaderId, text);
        GL20.glCompileShader(shaderId);
        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
            throw new RuntimeException("Can't compile shader: ");
        }
        return shaderId;
    }

    public void use() {
        if (this.programId == -1) {
            return;
        }
        GL20.glUseProgram(this.programId);
    }

    public void unUse() {
        GL20.glUseProgram(0);
    }

    public void delete() {
        if (this.vertexShader != -1) {
            GL20.glDeleteShader(this.vertexShader);
            this.vertexShader = -1;
        }
        if (this.fragmentShader != -1) {
            GL20.glDeleteShader(this.fragmentShader);
            this.fragmentShader = -1;
        }
        if (this.programId != -1) {
            GL20.glDeleteProgram(this.programId);
            this.programId = -1;
        }
    }

    private boolean checkAndPutUniform(String name, FloatBuffer buffer) {
        if (buffer.hasArray()) {
            return this.checkAndPutUniform(name, buffer.array());
        }
        return true;
    }

    private boolean checkAndPutUniform(String name, Object value) {
        Object obj = this.uniformVariables.get(name);
        if (value.equals(obj)) {
            return false;
        }
        this.uniformVariables.put(name, obj);
        return true;
    }

    public void uniform1i(String name, int value) {
        if (this.programId == -1 && this.checkAndPutUniform(name, value)) {
            return;
        }
        int location = GL20.glGetUniformLocation(this.programId, name);
        GL20.glUniform1i(location, value);
    }

    public void uniform1iv(String name, int ... values) {
        if (this.programId == -1 && this.checkAndPutUniform(name, values)) {
            return;
        }
        int location = GL20.glGetUniformLocation(this.programId, name);
        GL20.glUniform1iv(location, GLUtils.wrapTempIntBuffer(values));
    }

    public void uniform1f(String name, float value) {
        if (this.programId == -1 && this.checkAndPutUniform(name, Float.valueOf(value))) {
            return;
        }
        int location = GL20.glGetUniformLocation(this.programId, name);
        GL20.glUniform1f(location, value);
    }

    public void uniform2f(String name, float value1, float value2) {
        if (this.programId == -1 && this.checkAndPutUniform(name, new float[]{value1, value2})) {
            return;
        }
        int location = GL20.glGetUniformLocation(this.programId, name);
        GL20.glUniform2f(location, value1, value2);
    }

    public void uniform4f(String name, float value1, float value2, float value3, float value4) {
        if (this.programId == -1 && this.checkAndPutUniform(name, new float[]{value1, value2, value3, value4})) {
            return;
        }
        int location = GL20.glGetUniformLocation(this.programId, name);
        GL20.glUniform4f(location, value1, value2, value3, value4);
    }

    public void uniformColor(String name, Color color) {
        this.uniform4f(name, (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }
    public void uniformColor(String name, dev.forward.casino.util.color.Color color) {
        this.uniform4f(name, (float)color.getIntR() / 255.0f, (float)color.getIntG() / 255.0f, (float)color.getIntB() / 255.0f, (float)color.getIntAlpha() / 255.0f);
    }

    public void uniform1fv(String name, float ... values) {
        if (this.programId == -1 && this.checkAndPutUniform(name, values)) {
            return;
        }
        int location = GL20.glGetUniformLocation(this.programId, name);
        GL20.glUniform1fv(location, GLUtils.wrapTempFloatBuffer(values));
    }

    public void uniformMatrix4(String name, boolean transpose, FloatBuffer buffer) {
        if (this.programId == -1 && this.checkAndPutUniform(name, buffer)) {
            return;
        }
        int location = GL20.glGetUniformLocation(this.programId, name);
        GL20.glUniformMatrix4fv(location, transpose, buffer);
    }
}
