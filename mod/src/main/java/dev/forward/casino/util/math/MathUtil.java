package dev.forward.casino.util.math;

import dev.forward.casino.mixins.accessors.MinecraftClientAccessor;
import dev.forward.casino.engine.Engine;
import dev.forward.casino.engine.elements.AbstractElement;
import net.minecraft.util.math.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtil {
    public static double lerp(double a, double b, double progress) {
        double c = Math.min(Math.max(progress, 0.0), 1.0) * (b - a);
        c = c <= 1.0E-4 && c >= -1.0E-4 ? b - a : c;
        return a + c;
    }
    public static double deltaTime() {
        return MinecraftClientAccessor.fps() > 0 ? (1.0000 / MinecraftClientAccessor.fps()) : 1;
    }
    public static float fast(float end, float start, float multiple) {
        return (1 - MathHelper.clamp((float) (deltaTime() * multiple), 0, 1)) * end + MathHelper.clamp((float) (deltaTime() * multiple), 0, 1) * start;
    }
    public static double getElementSquaredDistanceToCamera(AbstractElement<?> element) {
        V3 pos = (V3)element.getPos().add(element.getOffset());
        return Engine.getGameSettings().getCameraPosition().squaredDistance(pos);
    }

    public static V3 calculateDifference(V3 currentV3, V3 newV3) {
        double x = currentV3.getX() + (newV3.getX() - currentV3.getX());
        double y = currentV3.getY() + (newV3.getY() - currentV3.getY());
        double z = currentV3.getZ() + (newV3.getZ() - currentV3.getZ());
        return new V3(x, y, z);
    }

    public static V3 calculateDifference(V3 currentV3, V3 newV3, Double progress) {
        double x = currentV3.getX() + (newV3.getX() - currentV3.getX()) * progress;
        double y = currentV3.getY() + (newV3.getY() - currentV3.getY()) * progress;
        double z = currentV3.getZ() + (newV3.getZ() - currentV3.getZ()) * progress;
        return new V3(x, y, z);
    }

    public static boolean intersectRayWithSquare(V3 R1, V3 R2, V3 S1, V3 S2, V3 S3) {
        V3 dS21 = (V3)S2.subtract(S1);
        V3 dS31 = (V3)S3.subtract(S1);
        V3 n = dS21.cross(dS31);
        V3 dR = (V3)R1.subtract(R2);
        double ndotdR = n.dot(dR);
        if (Math.abs(ndotdR) < 9.999999974752427E-7) {
            return false;
        } else {
            double t = -n.dot(R1.subtract(S1)) / ndotdR;
            V3 M = (V3)R1.add(dR.multiply(t));
            V3 dMS1 = (V3)M.subtract(S1);
            double u = dMS1.dot(dS21);
            double v = dMS1.dot(dS31);
            return u >= 0.0 && u <= dS21.dot(dS21) && v >= 0.0 && v <= dS31.dot(dS31) && ((V3)R1.subtract(M)).getLength() <= dR.getLength();
        }
    }
    public static double round(double num, double increment) {
        double v = (double)Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
