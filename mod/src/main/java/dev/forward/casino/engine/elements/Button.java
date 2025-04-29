package dev.forward.casino.engine.elements;

import dev.forward.casino.util.color.ButtonColor;

public class Button
        extends AbstractButton<Button> {
    public Button() {
    }

    public Button(String content) {
        super(content);
    }

    public Button(String content, ButtonColor buttonColor) {
        super(content, buttonColor);
    }

    @Override
    public Button clone() {
        return this.copy(new Button());
    }
}
