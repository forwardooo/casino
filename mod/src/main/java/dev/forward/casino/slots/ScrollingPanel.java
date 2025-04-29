package dev.forward.casino.slots;

import dev.forward.casino.engine.elements.ScrollVerticalScrollView;
import dev.forward.casino.engine.elements.VerticalLayout;
import dev.forward.casino.engine.event.PreRenderEvent;
import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.engine.animation.EasingFunc;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.Relative;
import dev.forward.casino.util.math.V3;

import java.util.Collections;
import java.util.List;

public class ScrollingPanel extends ScrollVerticalScrollView<VerticalLayout> {
    private List<SlotEnum> slotsToSwap;
    private List<Integer> winnedSlots;
    public ScrollingPanel(List<SlotEnum> slots) {
        super(VerticalLayout.class);
        Collections.shuffle(slots);
        this.setOriginAndAlign(Relative.CENTER).setSize(new V3(110, 345));
        this.getLayout().setSpacing(5.0);
        this.getBar().setEnabled(false);
        for (SlotEnum slot : slots) {
            SingleSlot carved = new SingleSlot(slot);
            this.addChild(carved);
        }
        this.getLayout().registerEvent(PreRenderEvent.class,( pre) -> {
            if (slotsToSwap != null) {
                SingleSlot first = (SingleSlot) this.getLayout().getChilds().get(0);
                SingleSlot second = (SingleSlot) this.getLayout().getChilds().get(1);
                SingleSlot third = (SingleSlot) this.getLayout().getChilds().get(2);

                first.update(slotsToSwap.get(0));
                second.update(slotsToSwap.get(1));
                third.update(slotsToSwap.get(2));

                slotsToSwap = null;
            }
        });
    }
   public void scroll(List<SlotEnum> newSlots, List<Integer> winningSlots,boolean fast, Runnable afterScroll) {
        VerticalLayout horizontalLayout = this.getLayout();
        double spacing = horizontalLayout.getSpacing();
        int winningIndex = 30;
        double totalOffset = (double)winningIndex * (100.0 + spacing) - this.getSize().getY() / 2.0;
        double sizeLayout = horizontalLayout.getSize().getY();
        V3 pos = new V3(0.0, 0.0, 0.0);
        horizontalLayout.setPos(pos);
        double[] initialPos = new double[]{pos.getY()};
        double finalOffset = (totalOffset + 13) / (sizeLayout - this.getSize().getY());
        HouseScreen screen = (HouseScreen) this.getUpperParent().getUpperParent();
        double duration = fast ? 1 + (screen.getSlots().indexOf(this) * 0.25) : 2 + (screen.getSlots().indexOf(this) * 0.5);
        Animation.play(this, "play", duration, EasingFunc.EASE_OUT_QUINT, (element, progress) -> {
            this.setScrollProgress(finalOffset * (1 - progress));
            double currentPosY = pos.getY();
            if (currentPosY <= initialPos[0] - (100.0 + spacing)) {
                initialPos[0] = (100.0 + spacing) * (double)((int)(currentPosY / (100.0 + spacing)));
            }
        }).onComplete((layout) -> {
            afterScroll.run();
            for (int index : winnedSlots) {
                SingleSlot slot = (SingleSlot) this.getLayout().getChilds().get(index);
                slot.smoothChangeColor(slot.getSlot().getBrighterColor(), 1.5);
            }
        });
        this.slotsToSwap = newSlots;
        this.winnedSlots = winningSlots;
    }
    public void scroll(List<SlotEnum> newSlots,List<Integer> winningSlots, boolean fast) {
        VerticalLayout horizontalLayout = this.getLayout();
        double spacing = horizontalLayout.getSpacing();
        int winningIndex = 30;
        double totalOffset = (double)winningIndex * (100.0 + spacing) - this.getSize().getY() / 2.0;
        double sizeLayout = horizontalLayout.getSize().getY();
        V3 pos = new V3(0.0, 0.0, 0.0);
        horizontalLayout.setPos(pos);
        double[] initialPos = new double[]{pos.getY()};
        double finalOffset = (totalOffset + 13) / (sizeLayout - this.getSize().getY());
        HouseScreen screen = (HouseScreen) this.getUpperParent().getUpperParent();
        double duration = fast ? 1 + (screen.getSlots().indexOf(this) * 0.25) : 2 + (screen.getSlots().indexOf(this) * 0.5);
        Animation.play(this, "play", duration, EasingFunc.EASE_OUT_QUINT, (element, progress) -> {
            this.setScrollProgress(finalOffset * (1 - progress));
            double currentPosY = pos.getY();
            if (currentPosY <= initialPos[0] - (100.0 + spacing)) {
                initialPos[0] = (100.0 + spacing) * (double)((int)(currentPosY / (100.0 + spacing)));
            }
        }).onComplete((layou) -> {
            for (int index : winnedSlots) {
                SingleSlot slot = (SingleSlot) this.getLayout().getChilds().get(index);
                slot.smoothChangeColor(slot.getSlot().getBrighterColor(), 1.5);
            }
        });
        this.slotsToSwap = newSlots;
        this.winnedSlots = winningSlots;
    }
}
