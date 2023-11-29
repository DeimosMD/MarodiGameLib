package physics;

import control.Game;

public class RoamerPhysics extends Physics {

    private double baseResistance;

    @Override
    public void update(Game game) {
        for (PhysicalPositional ph : game.getActivePhysicalPositionals()) {
            ph.changeVelocityWithResistance(getHorizontalResistance(ph), getVerticalResistance(ph));
            ph.changePosByVelocity();
        }
        collision.update(game);
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