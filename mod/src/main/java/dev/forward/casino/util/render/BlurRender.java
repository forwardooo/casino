package dev.forward.casino.util.render;


import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.shader.ShaderProgram;
import dev.forward.casino.engine.vbo.object.IBufferObject;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.math.V3;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;

import java.nio.IntBuffer;

public class BlurRender implements FastAccess {
    private final float[] weight = new float[50];
    private int frameBufferObject = -1;
    private int textureId1 = -1;
    private int textureId2 = -1;
    private int width;
    private int height;
    private float blurSize;
    private int radius;

    public BlurRender() {
        this.initialize();
    }

    public void initialize() {
        try {
            this.createFrameBuffer();
            GLUtils.checkFramebufferComplete();
            GLUtils.unbindFrameBuffer();
        }
        catch (Exception e) {
            log("Cant't initialize BlurDrawer: " + e.getMessage());
        }
    }

    private void createFrameBuffer() {
        GlStateManager.enableDepthTest();
        this.deleteFrameBuffer();
        this.frameBufferObject = GL32.glGenFramebuffers();
        GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, this.frameBufferObject);
        this.textureId1 = this.createTexture();
        this.textureId2 = this.createTexture();
        this.bindTextureToBuffer(this.textureId1);
    }

    private int createTexture() {
        int textureId = GlStateManager.genTextures();
        GlStateManager.bindTexture(textureId);
        GlStateManager.texParameter(3553, 10241, 9728);
        GlStateManager.texParameter(3553, 10240, 9728);
        GlStateManager.texParameter(3553, 10242, 10496);
        GlStateManager.texParameter(3553, 10243, 10496);
        GlStateManager.texImage2D(3553, 0, 32856, 1, 1, 0, 6408, 5121, null);
        GlStateManager.bindTexture(0);
        return textureId;
    }

    private void deleteFrameBuffer() {
        if (this.frameBufferObject > -1) {
            GL32.glDeleteFramebuffers(this.frameBufferObject);
            this.frameBufferObject = -1;
        }
        if (this.textureId1 > -1) {
            GlStateManager.deleteTexture(this.textureId1);
            this.textureId1 = -1;
        }
        if (this.textureId2 > -1) {
            GlStateManager.deleteTexture(this.textureId2);
            this.textureId2 = -1;
        }
    }

    public void unload() {
        this.deleteFrameBuffer();
    }

    private void bindTextureToBuffer(int textureId) {
        GL32.glFramebufferTexture2D(GL32.GL_FRAMEBUFFER, GL32.GL_COLOR_ATTACHMENT0, 3553, textureId, 0);
        GlStateManager.clearColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

    private void bindBuffer(V3 viewport) {
        GL32.glBindFramebuffer(GL32.GL_FRAMEBUFFER, this.frameBufferObject);
        if ((int)viewport.getX() != this.width || (int)viewport.getY() != this.height) {
            this.width = (int)viewport.getX();
            this.height = (int)viewport.getY();
            GlStateManager.bindTexture(this.textureId1);
            GlStateManager.texImage2D(3553, 0, 32856, this.width, this.height, 0, 6408, 5121, (IntBuffer)null);
            GlStateManager.bindTexture(this.textureId2);
            GlStateManager.texImage2D(3553, 0, 32856, this.width, this.height, 0, 6408, 5121, (IntBuffer)null);
        }

        this.bindTextureToBuffer(this.textureId1);
        GlStateManager.matrixMode(5889);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.ortho(0.0, (double)this.width, (double)this.height, 0.0, 1000.0, 3000.0);
        GlStateManager.matrixMode(5888);
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        GlStateManager.translatef(0.0F, 0.0F, -2000.0F);
        GlStateManager.viewport(0, 0, this.width, this.height);
    }

    private void unbindBuffer() {
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5889);
        GlStateManager.popMatrix();
        GlStateManager.matrixMode(5888);
//        Enginex.getLayeredFrameBuffer().bindFrameBuffer();
    }

    public void draw(V3 size, V3 minUV, V3 maxUV, float blurSize, double carveSize, Boolean... carveCorners) {
        if (!((double)blurSize < 0.5)) {
            GlStateManager.enableTexture();
            int scale = (int)(Math.floor((double)blurSize / 5.0) * 0.5 + 1.0);
            V3 viewport = (V3)size.divide((double)scale);
            blurSize /= (float)scale;
            this.makeFullscreenImage(viewport, minUV, maxUV);
            this.drawBlurImage(viewport, size, blurSize, carveSize, carveCorners);
        }
    }

    private void makeFullscreenImage(V3 viewport, V3 minUV, V3 maxUV) {
        this.bindBuffer(viewport);
        if (GLUtils.isScissorEnabled()) {
            GL11.glDisable(3089);
        }
        ShaderProgram uvTextureShader = Engine.getShaderManager().getUVTextureProgram();
        IBufferObject quadVbo = Engine.getVboManager().getQuadObject();
        quadVbo.bind();
        uvTextureShader.use();
        uvTextureShader.uniform2f("minUV", (float)minUV.getX(), (float)minUV.getY());
        uvTextureShader.uniform2f("maxUV", (float)maxUV.getX(), (float)maxUV.getY());
        uvTextureShader.uniform2f("size", (float)viewport.getX(), (float)viewport.getY());
        uvTextureShader.uniformMatrix4("modelViewMatrix", false, GLUtils.getModelViewMatrix());
        uvTextureShader.uniformMatrix4("projectMatrix", false, GLUtils.getProjectionMatrix());
        mc.getFramebuffer().beginRead();
        quadVbo.draw();


        GlStateManager.blendFuncSeparate(770, 771, 1, 771);
        GlStateManager.enableBlend();
//        List<LayeredFrameBuffer.LayerData> layers = Enginex.getLayeredFrameBuffer().getLayersBelowCurrent();
//        Iterator var7 = layers.iterator();
//
//        while(var7.hasNext()) {
//            LayeredFrameBuffer.LayerData layer = (LayeredFrameBuffer.LayerData)var7.next();
//            GlStateManager.bindTexture(layer.textureId);
//            quadVbo.draw();
//        }

        quadVbo.unBind();
        GlStateManager.disableTexture();
        mc.gameRenderer.getLightmapTextureManager().disable();

        DiffuseLighting.disable();
    }

    private void drawBlurImage(V3 viewport, V3 size, float blurSize, double carveSize, Boolean... carveCorners) {
        IBufferObject quadVbo = Engine.getVboManager().getQuadObject();
        ShaderProgram blurShader = Engine.getShaderManager().getBlurProgram();
        blurShader.use();
        quadVbo.bind();
        this.bindTextureToBuffer(this.textureId2);
        GlStateManager.bindTexture(this.textureId1);
        if (this.blurSize != blurSize) {
            this.blurSize = blurSize;
            this.radius = this.fillGaussianWeights(this.weight, blurSize);
        }

        blurShader.uniform1i("radius", this.radius);
        blurShader.uniform1fv("weight", this.weight);
        blurShader.uniform2f("dir", 1.0F, 0.0F);
        blurShader.uniform1f("carveSize", 0.0F);
        blurShader.uniform2f("size", (float)viewport.getX(), (float)viewport.getY());
        blurShader.uniformMatrix4("modelViewMatrix", false, GLUtils.getModelViewMatrix());
        blurShader.uniformMatrix4("projectMatrix", false, GLUtils.getProjectionMatrix());
        quadVbo.draw();
        this.bindTextureToBuffer(this.textureId1);
        GlStateManager.bindTexture(this.textureId2);
        blurShader.uniform2f("dir", 0.0F, 1.0F);
        quadVbo.draw();
        this.unbindBuffer();
        GlStateManager.bindTexture(this.textureId1);
        if (GLUtils.isScissorEnabled()) {
            GL11.glEnable(3089);
        }

        ShaderProgram textureShader = Engine.getShaderManager().getTexturedCarvedProgram();
        textureShader.use();
        textureShader.uniform2f("minUV", 0.0F, 1.0F);
        textureShader.uniform2f("maxUV", 1.0F, 0.0F);
        textureShader.uniform4f("color", 1.0F, 1.0F, 1.0F, 1.0F);
        textureShader.uniform2f("size", (float)size.getX(), (float)size.getY());
        textureShader.uniform1f("outline", 0.0F);
        textureShader.uniform1f("carveSize", (float)carveSize);
        textureShader.uniform1iv("corners", new int[]{carveCorners[0] ? 1 : 0, carveCorners[1] ? 1 : 0, carveCorners[2] ? 1 : 0, carveCorners[3] ? 1 : 0});
        textureShader.uniformMatrix4("modelViewMatrix", false, GLUtils.getModelViewMatrix());
        textureShader.uniformMatrix4("projectMatrix", false, GLUtils.getProjectionMatrix());
        quadVbo.draw();
        textureShader.unUse();
        quadVbo.unBind();
        GlStateManager.disableBlend();
        GlStateManager.bindTexture(0);
    }

    private int fillGaussianWeights(float[] weight, float blurSize) {
        float sigmaQuad = (float)Math.pow((double)blurSize, 2.0);
        float a = (float)(1.0 / Math.sqrt(6.283185307179586 * (double)sigmaQuad));

        for(int x = 0; x < weight.length; ++x) {
            weight[x] = (float)((double)a * Math.pow(Math.E, (double)(-((float)(x * x) / (2.0F * sigmaQuad)))));
            if ((double)weight[x] <= 0.001) {
                return x;
            }
        }

        return weight.length;
    }
}
