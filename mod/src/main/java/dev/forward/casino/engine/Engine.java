package dev.forward.casino.engine;

import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.engine.component.ComponentManager;
import dev.forward.casino.engine.contexts.ContextManager;
import dev.forward.casino.engine.elements.AbstractGuiScreen;
import dev.forward.casino.engine.elements.GuiModal;
import dev.forward.casino.engine.shader.ShaderManager;
import dev.forward.casino.engine.tooltip.CustomTooltip;
import dev.forward.casino.engine.vbo.VboManager;
import dev.forward.casino.util.FastAccess;
import dev.forward.casino.util.GameSettings;
import dev.forward.casino.util.interactive.InteractionManager;
import dev.forward.casino.util.render.BlurRender;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Tessellator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Getter
public class Engine implements FastAccess {
    private static Engine instance;
    private final CustomTooltip customTooltip;
    private final BlurRender blurRender ;
    private final ShaderManager shaderManager;
    private final VboManager vboManager;
    private final InteractionManager interactionManager;
    private final ComponentManager componentManager ;
    private final GameSettings gameSettings;
    private final ContextManager contextManager;
    public static BlurRender getBlurRender() {
        return instance.blurRender;
    }
    public static CustomTooltip getCustomTooltip() {
        return instance.customTooltip;
    }
    public static GameSettings getGameSettings() {
        return instance.gameSettings;
    }
    public static InteractionManager getInteractionManager() {
        return instance.interactionManager;
    }
    public static ComponentManager getComponentManager() {
        return instance.componentManager;
    }
    public static VboManager getVboManager() {
        return instance.vboManager;
    }
    public static ContextManager getContextManager() {
        return instance.contextManager;
    }
    public static ShaderManager getShaderManager() {
        return instance.shaderManager;
    }
    public Engine() {
        instance = this;
        this.shaderManager = new ShaderManager();
        this.vboManager = new VboManager();
        this.blurRender = new BlurRender();
        this.contextManager = new ContextManager();
        this.componentManager = new ComponentManager();
        this.interactionManager = new InteractionManager();
        this.gameSettings = new GameSettings();
        this.customTooltip = new CustomTooltip();
    }
    public static void internalInitialize() {
        if (instance == null) {
            instance = new Engine();
            Animation.registerEvents();
        }
    }
    public static void setCurrentModal(GuiModal guiModal) {
        instance.contextManager.setCurrentModal(guiModal);
    }
    public static GuiModal getCurrentModal() {
        return instance.contextManager.getCurrentModal();
    }
    public static <T extends AbstractGuiScreen> T getCurrentScreen() {
        return (T) instance.contextManager.getCurrentScreen();
    }
    public static void setCurrentScreen(AbstractGuiScreen screen) {
        instance.contextManager.setCurrentScreen(screen);
    }
}
