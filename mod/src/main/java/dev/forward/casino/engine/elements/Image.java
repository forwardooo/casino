package dev.forward.casino.engine.elements;

import com.mojang.blaze3d.platform.GlStateManager;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.shader.ShaderProgram;
import dev.forward.casino.engine.vbo.object.IBufferObject;
import dev.forward.casino.util.color.Color;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.GLUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Image
        extends AbstractElement<Image> {
    protected Identifier texture;
    protected V3 minUV = new V3();
    protected V3 maxUV = new V3(1.0, 1.0);
    protected double carveSize;
    protected Boolean[] carveCorners = new Boolean[]{true, true, true, true};
    protected double outline;
    protected Color outlineColor;

    public Image() {
    }

    public Image(String texture) {
        this.setTexture(texture);
    }

    public Image(Identifier texture) {
        this.texture = texture;
    }

    public String getTexture() {
        return this.texture == null ? "" : this.texture.toString();
    }

    public Image setTexture(Identifier texture) {
        this.texture = texture;
        return this;
    }

    public Image setTexture(String texture) {
        return this.setTexture(new Identifier(texture));
    }

    public Image setMinUV(V3 minUV) {
        this.minUV = minUV;
        return this;
    }

    public Image setMaxUV(V3 maxUV) {
        this.maxUV = maxUV;
        return this;
    }

    public Image setCarveSize(double carveSize) {
        this.carveSize = carveSize;
        return this;
    }

    public Image setOutline(double outline) {
        if (this.is3dRender()) {
            outline /= 2.5;
        }
        this.outline = outline;
        return this;
    }

    public Image setOutlineColor(Color outlineColor) {
        this.outlineColor = outlineColor;
        return this;
    }

    public Image setCarveCorners(Boolean ... carveCorners) {
        this.carveCorners = carveCorners;
        return this;
    }

    @Override
    public void render(MatrixStack stack, double partialTicks, double mouseX, double mouseY) {
        if (this.color.getAlpha() * this.getAbsoluteAlpha() <= 0.0) {
            return;
        }
        if (this.texture == null) {
            return;
        }
        this.texture = new Identifier(this.texture.getNamespace(), this.texture.getPath());
        mc.getTextureManager().bindTexture(this.texture);
        double absoluteAlpha = this.getAbsoluteAlpha();
        Color absoluteColor = (Color)this.color.setAlpha(this.color.getAlpha() * absoluteAlpha);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 771);
        ShaderProgram shader = Engine.getShaderManager().getTexturedCarvedProgram();
        shader.use();
        shader.uniform2f("minUV", (float)this.minUV.getX(), (float)this.minUV.getY());
        shader.uniform2f("maxUV", (float)this.maxUV.getX(), (float)this.maxUV.getY());
        shader.uniform4f("color", (float)absoluteColor.getR(), (float)absoluteColor.getG(), (float)absoluteColor.getB(), (float)absoluteColor.getAlpha());
        shader.uniform2f("size", (float)this.size.getX(), (float)this.size.getY());
        shader.uniform1f("carveSize", (float)this.carveSize);
        shader.uniform1iv("corners", this.carveCorners[0] != false ? 1 : 0, this.carveCorners[1] != false ? 1 : 0, this.carveCorners[2] != false ? 1 : 0, this.carveCorners[3] != false ? 1 : 0);
        shader.uniformMatrix4("modelViewMatrix", false, GLUtils.getModelViewMatrix());
        shader.uniformMatrix4("projectMatrix", false, GLUtils.getProjectionMatrix());
        if (this.outline > 0.0) {
            Color absoluteOutlineColor = this.outlineColor == null ? Palette.getBrighterColor(this.color) : this.outlineColor;
            absoluteOutlineColor = (Color)absoluteOutlineColor.setAlpha(absoluteOutlineColor.getAlpha() * absoluteAlpha);
            shader.uniform1f("outline", (float)this.outline);
            shader.uniform4f("outlineColor", (float)absoluteOutlineColor.getR(), (float)absoluteOutlineColor.getG(), (float)absoluteOutlineColor.getB(), (float)absoluteOutlineColor.getAlpha());
        } else {
            shader.uniform1f("outline", 0.0f);
        }
        IBufferObject object = Engine.getVboManager().getQuadObject();
        object.bind();
        object.draw();
        shader.unUse();
        object.unBind();
        GlStateManager.disableBlend();
    }

    @Override
    public Image copy(Image element) {
        super.copy(element);
        element.setTexture(this.getTexture()).setMinUV(this.getMinUV()).setMaxUV(this.getMaxUV()).setCarveSize(this.getCarveSize()).setOutline(this.getOutline()).setOutlineColor(this.getOutlineColor()).setCarveCorners((Boolean[])this.carveCorners.clone());
        return element;
    }

    @Override
    public Image clone() {
        return this.copy(new Image());
    }

    public V3 getMinUV() {
        return this.minUV;
    }

    public V3 getMaxUV() {
        return this.maxUV;
    }

    public double getCarveSize() {
        return this.carveSize;
    }

    public double getOutline() {
        return this.outline;
    }

    public Color getOutlineColor() {
        return this.outlineColor;
    }
}