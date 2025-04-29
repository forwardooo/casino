package dev.forward.casino.element;

import dev.forward.casino.engine.animation.Animation;
import dev.forward.casino.engine.elements.*;
import dev.forward.casino.engine.event.ButtonLeftActionEvent;
import dev.forward.casino.engine.event.MouseLeftClickEvent;
import dev.forward.casino.engine.event.PreRenderEvent;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.Relative;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.network.ModTransfer;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MainScreen extends AbstractGuiScreen {
    private final List<ScrollingPanel> slots = new ArrayList<>();
    private final HorizontalLayout scrolling;
    private final BalanceText balance;
    private boolean openedInformation;
    private CarvedRectangle info;
    private V3 nonInfoSize;
    private VerticalLayout bg;
    public MainScreen(List<SlotEnum> stacks, int bal) {
        super();
        VerticalLayout mainLayout = new VerticalLayout().setPosY(10);
        HorizontalLayout casino = new HorizontalLayout(5);
        CarvedRectangle background = new CarvedRectangle(5)
                .setOriginAndAlign(Relative.CENTER)
                .setColor(Palette.GREY_DARK_62).setOutlineColor(Palette.GREY_MIDDLE_62).setOutline(3);
        addChild(background);
        scrolling = new HorizontalLayout(5);
        scrolling.setOriginAndAlign(Relative.LEFT);
        for (int i = 0; i < 5; i++) {
            ScrollingPanel slot = new ScrollingPanel(stacks);
            scrolling.addChild(slot);
            slots.add(slot);
        }

        VerticalLayout rightSide = new VerticalLayout(10);
        Button startButton = new Button("Прокрутить").setOriginAndAlign(Relative.BOTTOM);
        Scroller panel = new Scroller(10,100,5, 55)
                .setSize(new V3(130, 8))
                .setCarveSize(1).setPosY(-10)
                .setOutline(2.0)
                .setColor(Palette.BLUE)
                .setOutlineColor(Palette.BLUE_LIGHT)
                .setOriginAndAlign(Relative.BOTTOM);

        startButton.registerEvent(ButtonLeftActionEvent.class, (click) -> {
            ScrollingPanel slot = (ScrollingPanel) scrolling.getChilds().get(4);
            if (slot.getScrollProgress() == 0) {
                new ModTransfer().writeInt((int) panel.getValue()).send("house:scroll");
            }
        });
        CarvedRectangle scrollerBackground = new CarvedRectangle().setSize(new V3(150, 50)).setOriginAndAlign(Relative.TOP).setColor(Palette.GREY_DARK_62).setOutlineColor(Palette.GREY_MIDDLE_62).setOutline(1);
        Text text = new Text("Ставка: 55").setOriginAndAlign(Relative.TOP).setPosY(10);
        text.registerEvent(PreRenderEvent.class, (pre) -> {
            text.setValue("Ставка: " + (int) panel.getValue());
        });
        scrollerBackground.addChild(text);
        scrollerBackground.addChild(panel);
        balance = new BalanceText(bal);
        balance.setSize(new V3(startButton.getSize().getX(), balance.getSize().getY()));
        Button openInformation = new Button("Информация");

        openInformation.registerEvent(MouseLeftClickEvent.class, (click) -> {
            if (!Animation.hasAnimation(this, "information")) {
                V3 start = background.getSize();
                if (openedInformation) {
                    openedInformation = false;

                    double deltaX = start.getX() - nonInfoSize.getX();
                    double deltaY = start.getY() - nonInfoSize.getY();

                    Animation.play(this, "info", 0.3, (element, value) -> {
                        double newX = start.getX() - (deltaX * value);
                        double newY = start.getY() - (deltaY * value);
                        background.setSize(new V3(newX, newY));
                        info.setScale(1 - value, 1 - value, 1);
                        if (value > 0.6) {
                            info.setEnabled(false);
                        }
                    });
                } else {
                    openedInformation = true;
                    info.setEnabled(true);
                    info.setScale(0,0,0);
                    V3 newSize = bg.getSize().add(20,10,0);
                    double deltaX = newSize.getX() - start.getX();
                    double deltaY = newSize.getY() - start.getY();
                    Animation.play(this, "info", 0.3,(element, value) -> {
                        double newX = start.getX() + (deltaX * value);
                        double newY = start.getY() + (deltaY * value);
                        background.setSize(new V3(newX, newY));
                        if (value > 0.3) {
                            info.setEnabled(true);
                            info.setScale(value, value, 1);
                        }
                    });
                }
            }
        });
        rightSide.setPosY(5);
        rightSide.addChild(balance);
        rightSide.addChild(scrollerBackground);
        rightSide.addChild(startButton);
        rightSide.addChild(openInformation);

        casino.addChild(scrolling);
        casino.addChild(rightSide);
        mainLayout.addChild(casino);
        mainLayout.setOriginAndAlign(Relative.TOP);
        bg = new VerticalLayout(20).setOriginAndAlign(Relative.TOP);
        bg.addChild(mainLayout);
        background.addChild(bg);
        nonInfoSize = bg.getSize().add(20,20,0);
        background.setSize(nonInfoSize);
        bg.addChild(info = info());
    }

    public void scroll(List<SlotEnum> winningItems, int winningBalance, List<Integer> winningSlots) {
        List<List<SlotEnum>> result = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            List<SlotEnum> panel = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                panel.add(winningItems.get(i * 3 + j));
            }
            result.add(panel);
        }

        ScrollingPanel mainSlot = (ScrollingPanel) scrolling.getChilds().get(4);

        if (mainSlot.getScrollProgress() == 0) {
            for (int i = 0; i < scrolling.getChilds().size(); i++) {
                AbstractElement<?> child = scrolling.getChilds().get(i);
                if (child instanceof ScrollingPanel) {
                    ScrollingPanel slot = (ScrollingPanel) child;
                    List<SlotEnum> panelItems = result.get(i);

                    List<Integer> localWinningIndices = new ArrayList<>();
                    for (int row = 0; row < 3; row++) {
                        int globalIndex = i * 3 + row;
                        if (winningSlots.contains(globalIndex)) {
                            localWinningIndices.add(row);
                        }
                    }

                    if (child == mainSlot) {
                        slot.scroll(panelItems, localWinningIndices, true, () -> {
                            if (winningBalance > 0) {
                                updateBalance(winningBalance, 1000, true);
                            }
                        });
                    } else {
                        slot.scroll(panelItems, localWinningIndices, true);
                    }
                }
            }
        }
    }
    public void updateBalance(int bal, int ms, boolean anim) {
        balance.update(bal, ms, anim);
    }
    private CarvedRectangle info() {
        CarvedRectangle bg = new AnimatedInfoBackground().setOriginAndAlign(Relative.CENTER)
                .setColor(Palette.GREY_DARK_86).setOutlineColor(Palette.GREY_MIDDLE_86).setOutline(3);
        VerticalLayout information = getInformationLayout();
        bg.addChild(information);
        bg.setSize(information.getSize().add(20,20, 0));
        bg.setEnabled(false);
        return bg;
    }
    private VerticalLayout getInformationLayout() {
        VerticalLayout layout = new VerticalLayout(10).setOriginAndAlign(Relative.CENTER);
        layout.addChild(new Text("x - Ставка"));
        HorizontalLayout first = new HorizontalLayout(5).setOriginAndAlign(Relative.CENTER);
        HorizontalLayout second = new HorizontalLayout(5).setOriginAndAlign(Relative.CENTER);
        for (int i = 0; i < 5; i++) {
            first.addChild(getInformationField(SlotEnum.values()[i]));
        }
        for (int i = 0; i < 5; i++) {
            second.addChild(getInformationField(SlotEnum.values()[5 + i]));
        }
        layout.addChild(first, second);
        return layout;
    }
    private CarvedRectangle getInformationField(SlotEnum slot) {
        CarvedRectangle bg = new CarvedRectangle().setColor(Palette.GREY_DARK_62).setOutlineColor(Palette.GREY_MIDDLE_62).setOutline(1);
        HorizontalLayout layout = new HorizontalLayout(10).setOriginAndAlign(Relative.CENTER);
        layout.addChild(new SingleSlot(slot, new V3(90,90)));
        VerticalLayout textLayout = new VerticalLayout(5);
        textLayout.addChild(new Text(String.format("5 - %sx", slot.getWin5())));
        textLayout.addChild(new Text(String.format("4 - %sx", slot.getWin4())));
        textLayout.addChild(new Text(String.format("3 - %sx", slot.getWin3())));
        textLayout.addChild(new Text(String.format("2 - %sx", slot.getWin2())));
        layout.addChild(textLayout);
        bg.setSize(new V3(202, 100));
        bg.addChild(layout);
        return bg;
    }

}
