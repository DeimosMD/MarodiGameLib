package physics;

import control.Game;

public class PlatformerPhysics extends Physics {

    private double baseVerticalAirResistance;
    private double baseHorizontalAirResistance;


    @Override
    public void update(Game game) {

    }

    double getHorizontalResistance(PhysicalPositional ph) {
        double a;
        if (ph.verticalCollision != 0)
            a = getBaseHorizontalAirResistance() + getBaseFrictionalResistance() + ph.getSpecHorizontalResistance(this);
        else
            a = getBaseHorizontalAirResistance() + ph.getSpecHorizontalResistance(this);
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    double getVerticalResistance(PhysicalPositional ph) {
        double a = ph.getSpecVerticalResistance(this) + getBaseVerticalAirResistance();
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    public double getBaseVerticalAirResistance() {
        return baseVerticalAirResistance;
    }

    public void setBaseVerticalAirResistance(double baseVerticalAirResistance) {
        this.baseVerticalAirResistance = baseVerticalAirResistance;
    }

    public double getBaseHorizontalAirResistance() {
        return baseHorizontalAirResistance;
    }

    public void setBaseHorizontalAirResistance(double baseHorizontalAirResistance) {
        this.baseHorizontalAirResistance = baseHorizontalAirResistance;
    }
}