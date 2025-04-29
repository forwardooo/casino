package dev.forward.casino.util.render;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.shader.ShaderProgram;
import dev.forward.casino.engine.vbo.object.IBufferObject;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.math.V3;

public class CarvedRectangleRender {
    public static void draw(V3 size, Color color, double carveSize, Boolean[] corners, double outline, Color outlineColor, Boolean[] outLines, Color outlineGradientColor, double outlineGradientHeight) {
        CarvedRectangleRender.draw(size, color, carveSize, corners, outline, outlineColor, 0.0, null, new V3(), outLines, outlineGradientColor, outlineGradientHeight);
    }

    public static void draw(V3 size, Color color, double carveSize, Boolean[] corners, double outline, Color outlineColor, double circleRadius, Color circleColor, V3 circlePos, Boolean[] outLines, Color outlineGradientColor, double outlineGradientHeight) {
        IBufferObject quadVbo = Engine.getVboManager().getQuadObject();
        ShaderProgram shader = Engine.getShaderManager().getCarvedProgram();
        quadVbo.bind();
        shader.use();
        shader.uniformColor("color", color);
        shader.uniform2f("size", (float)size.getX(), (float)size.getY());
        shader.uniform1f("carveSize", (float)carveSize);
        shader.uniform1iv("corners", new int[]{corners[0] ? 1 : 0, corners[1] ? 1 : 0, corners[2] ? 1 : 0, corners[3] ? 1 : 0});
        shader.uniform1iv("outLines", new int[]{outLines[0] ? 1 : 0, outLines[1] ? 1 : 0, outLines[2] ? 1 : 0, outLines[3] ? 1 : 0});
        shader.uniformMatrix4("modelViewMatrix", false, GLUtils.getModelViewMatrix());
        shader.uniformMatrix4("projectMatrix", false, GLUtils.getProjectionMatrix());
        shader.uniform1f("outline", (float)outline);
        if (outline > 0.0) {
            shader.uniformColor("outlineColor", outlineColor);
            if (outlineGradientColor == null) {
                shader.uniformColor("outlineGradientColor", new Color(0, 0, 0, 0));
            } else {
                shader.uniformColor("outlineGradientColor", outlineGradientColor);
            }

            shader.uniform1f("outlineGradientHeight", (float)outlineGradientHeight);
        }

        if (circleRadius > 0.0 && circleColor != null) {
            shader.uniform1f("circleRadius", (float)circleRadius);
            shader.uniform4f("circleColor", (float)circleColor.getR(), (float)circleColor.getG(), (float)circleColor.getB(), (float)circleColor.getAlpha());
            shader.uniform2f("circlePos", (float)circlePos.getX(), (float)circlePos.getY());
        } else {
            shader.uniform1f("circleRadius", 0.0F);
        }

        quadVbo.draw();
        shader.unUse();
        quadVbo.unBind();
    }

    public static void drawShadow(V3 size, V3 offset, float shadowSize, double carveSize, Color color) {
        IBufferObject quadVbo = Engine.getVboManager().getQuadObject();
        ShaderProgram shader = Engine.getShaderManager().getShadowProgram();
        quadVbo.bind();
        shader.use();
        GlStateManager.pushMatrix();
        GlStateManager.translated((double)(-shadowSize) + offset.getX(), (double)(-shadowSize) + offset.getY(), 0.0);
        shader.uniformColor("color", color);
        shader.uniform2f("size", (float)size.getX() + shadowSize * 2.0f, (float)size.getY() + shadowSize * 2.0f);
        shader.uniform1f("radius", shadowSize);
        shader.uniform1f("carveSize", (float)carveSize);
        shader.uniformMatrix4("modelViewMatrix", false, GLUtils.getModelViewMatrix());
        shader.uniformMatrix4("projectMatrix", false, GLUtils.getProjectionMatrix());
        quadVbo.draw();
        GlStateManager.popMatrix();
        quadVbo.unBind();
        shader.unUse();
    }
}
