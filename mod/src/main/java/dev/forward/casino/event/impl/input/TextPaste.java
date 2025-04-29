package dev.forward.casino.event.impl.input;

import dev.forward.casino.event.Event;
import dev.forward.casino.event.EventBus;
import lombok.Getter;

public class TextPaste implements Event {
    public static EventBus<TextPaste> BUS = new EventBus<>();
    private static final TextPaste INSTANCE = new TextPaste();
    @Getter
    private String text;
    public static TextPaste of(String text) {
        INSTANCE.text=  text;
        return INSTANCE;
    }
}
