package physics;

import component.world.Positional;
import component.world.Vector2D;
import control.Game;

public class RoamerPhysics extends Physics {

    private double baseResistance;

    @Override
    public void update(Game game) {
        Vector2D v = (Vector2D) game.getCurrentWorld().getVector2D().clone();
        for (Positional p : v)
            if (!(p instanceof PhysicalPositional)) v.remove(p);
        for (Positional p : v) {
            PhysicalPositional ph = (PhysicalPositional) p;
            ph.changeVelocityWithResistance(getHorizontalResistance(ph), getVerticalResistance(ph));
            ph.changePosByVelocity();
        }
    }

    double getHorizontalResistance(PhysicalPositional ph) {
        double a = getBaseResistance() + ph.getSpecHorizontalResistance(this);
        if (ph.horizontalCollision != 0)
            a += baseFrictionalResistance;
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    double getVerticalResistance(PhysicalPositional ph) {
        double a = getBaseResistance() + ph.getSpecVerticalResistance(this);
        if (ph.verticalCollision != 0)
            a += baseFrictionalResistance;
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    public double getBaseResistance() {
        return baseResistance;
    }

    public void setBaseResistance(double baseResistance) {
        this.baseResistance = baseResistance;
    }
}