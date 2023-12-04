package physics;

public class RoamerPhysics extends Physics {

    private double baseResistance;

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
        if (baseResistance < 0 || baseResistance > 1)
            throw new IllegalArgumentException("baseResistance must be '0 <= x <= 1'");
        this.baseResistance = baseResistance;
    }
}