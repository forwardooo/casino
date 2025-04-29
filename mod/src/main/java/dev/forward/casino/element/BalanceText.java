package dev.forward.casino.element;

import dev.forward.casino.engine.elements.CarvedRectangle;
import dev.forward.casino.engine.elements.Text;
import dev.forward.casino.engine.event.PreRenderEvent;
import dev.forward.casino.util.color.Palette;
import dev.forward.casino.util.math.Relative;
import dev.forward.casino.util.math.V3;
import dev.forward.casino.util.render.Counter;

public class BalanceText extends CarvedRectangle {
    private double current;
    private double target;
    private double increment;
    private final Counter counter = new Counter();
    private long cd;
    private Text balance;
    public BalanceText(int bal) {
        this.current = bal;
        this.target = bal;
        this.increment = target / current;
        balance = new Text("Баланс: " + bal).setOriginAndAlign(Relative.CENTER);
        addChild(balance);
        setColor(Palette.GREY_DARK_62).setOutlineColor(Palette.GREY_MIDDLE_62).setOutline(1);
        setSize(new V3(10 + balance.getSize().getX(), 10 + balance.getSize().getY()));
        balance.registerEvent(PreRenderEvent.class,(pre) -> {
            if (Math.round(current) != Math.round(target) && counter.hasTimeElapsed()) {
                current += increment;
                balance.setValue("Баланс: " + Math.round(current));
                counter.setLastMS(cd);
            }
        });
    }
    public void update(int val, int ms, boolean anim) {
        if (anim) {

            target = val;
            increment = (target - current) / 100;

            if (increment == 0) {
                if (target > current) increment = 1;
                else increment = -1;
            }
            cd = ms / 200;
        } else {
            current = val;
            target = val;
            balance.setValue("Баланс: " + val);
        }
    }
}
