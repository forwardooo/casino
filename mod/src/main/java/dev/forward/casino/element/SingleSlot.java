package dev.forward.casino.element;

import dev.forward.casino.engine.elements.Button;
import dev.forward.casino.engine.elements.Image;
import dev.forward.casino.util.math.*;
import lombok.Getter;
import net.minecraft.util.Identifier;

@Getter
public class SingleSlot extends Button {
    private SlotEnum slot;
    public SingleSlot(SlotEnum slot) {
        super("");
        this.slot = slot;
        setInteractive(false);
        setSize(new V3(110, 110));
        smoothChangeColor(slot.getColor(), 0);
        this.setOutline(2);
        Identifier identif = slot.getTexture();
        Image img = new Image(identif).setSize(this.getSize()).setOriginAndAlign(Relative.CENTER);
        addChild(img);
    }
    public SingleSlot(SlotEnum slot, V3 size) {
        super("");
        this.slot = slot;
        setInteractive(false);
        setSize(size);
        smoothChangeColor(slot.getColor(), 0);
        this.setOutline(2);
        Identifier identif = slot.getTexture();
        Image img = new Image(identif).setSize(this.getSize()).setOriginAndAlign(Relative.CENTER);
        addChild(img);
    }

    public SingleSlot update(SlotEnum slot) {
        this.slot = slot;
        clearChilds();
        smoothChangeColor(slot.getColor(), 0);

        addChild(new Image(slot.getTexture()).setSize(this.getSize()).setOriginAndAlign(Relative.CENTER));
        return this;
    }
}
