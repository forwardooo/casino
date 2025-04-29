package dev.forward.casino.engine.animation;
import dev.forward.casino.event.impl.render.RenderTickPost;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.elements.AbstractElement;
import dev.forward.casino.util.FastAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
public class Animation implements FastAccess {
    public static final Function<Double, Double> DEFAULT_OUT_ANIMATION = EasingFunc.EASE_OUT_CIRC;
    public static final Function<Double, Double> DEFAULT_IN_ANIMATION = EasingFunc.EASE_IN_CIRC;
    private static Animation instance;
    private final HashMap<AbstractElement<?>, Map<String, AnimationClip<?>>> clips = new HashMap<>();

    public static void registerEvents() {
        RenderTickPost.BUS.register(event -> Animation.get().onRender(mc.getTickDelta()));
    }

    public static void unload() {
        instance = null;
    }

    public static <T extends AbstractElement<T>> AnimationClip<T> play(AnimationClip<T> clip) {
        Map<String, AnimationClip<?>> elementClips;
        if (Animation.hasAnimation(clip.getElement(), clip.getId())) {
            Animation.stop(clip.getElement(), clip.getId());
        }
        if ((elementClips = Animation.getElementClips(clip.getElement())) == null) {
            elementClips = new HashMap<>();
        }
        clip.setup();
        elementClips.put(clip.getId(), clip);
        Animation.get().clips.putIfAbsent(clip.getElement(), elementClips);
        clip.tick();
        return clip;
    }

    public static <T extends AbstractElement<T>> AnimationClip<T> play(T element, String id, double duration, double delay, Function<Double, Double> easingFunc, BiConsumer<T, Double> consumer) {
        AnimationClip<T> clip = new AnimationClip<T>(element, id, duration, delay, easingFunc, consumer);
        return Animation.play(clip);
    }

    public static <T extends AbstractElement<T>> AnimationClip<T> play(T element, String id, double duration, Function<Double, Double> easingFunc, BiConsumer<T, Double> consumer) {
        return Animation.play(element, id, duration, 0.0, easingFunc, consumer);
    }

    public static <T extends AbstractElement<T>> AnimationClip<T> play(T element, String id, double duration, BiConsumer<T, Double> consumer) {
        return Animation.play(element, id, duration, EasingFunc.EASE_OUT_CUBIC, consumer);
    }

    public static void stop(AbstractElement<?> element, String id) {
        Animation.removeClip(element, id);
    }

    public static void stop(AbstractElement<?> element) {
        Animation.get().clips.remove(element);
    }

    public static boolean hasAnimation(AbstractElement<?> element, String id) {
        Map<?, ?> elementClips = Animation.get().clips.getOrDefault(element, null);
        if (elementClips == null) {
            return false;
        }
        return elementClips.containsKey(id);
    }

    public static Map<String, AnimationClip<?>> getElementClips(AbstractElement<?> element) {
        return Animation.get().clips.get(element);
    }

    private static void removeClip(AbstractElement<?> element, String id) {
        Map<String, AnimationClip<?>> elementClips = Animation.getElementClips(element);
        if (elementClips == null || !elementClips.containsKey(id)) {
            return;
        }
        AnimationClip<?> clip = elementClips.remove(id);
        if (elementClips.isEmpty()) {
            Animation.get().clips.remove(element);
        }
        clip.stopped();
    }

    public static Animation get() {
        if (instance == null) {
            instance = new Animation();
        }
        return instance;
    }

    public void onRender(double partialTicks) {
        HashMap<AbstractElement<?>, String> toRemove = new HashMap<>();
        for (AbstractElement<?> abstractElement : new ArrayList<>(this.clips.keySet())) {
            ArrayList<AnimationClip<?>> clipsList = new ArrayList<>(this.clips.get(abstractElement).values());
            for (AnimationClip<?> animationClip : clipsList) {
                animationClip.tick();
                if (!animationClip.isCompleted()) continue;
                toRemove.put(abstractElement, animationClip.getId());
            }
        }
        for (Map.Entry<AbstractElement<?>, String> entry : toRemove.entrySet()) {
            AnimationClip<?> clip;
            AbstractElement<?> element = entry.getKey();
            String clipId = entry.getValue();
            Map<String, AnimationClip<?>> map = Animation.getElementClips(element);
            if (map == null || (clip = map.getOrDefault(clipId, null)) == null || !clip.isCompleted()) continue;
            Animation.removeClip(element, clipId);
        }
    }
}