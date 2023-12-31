package marodi.physics;

public class RoamerPhysics extends Physics {

    private float baseResistance;

    float getHorizontalResistance(PhysicalPositional ph) {
        float a = getBaseResistance() + ph.getSpecHorizontalResistance(this);
        if (ph.horizontalCollision != 0)
            a += baseFrictionalResistance;
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    float getVerticalResistance(PhysicalPositional ph) {
        float a = getBaseResistance() + ph.getSpecVerticalResistance(this);
        if (ph.verticalCollision != 0)
            a += baseFrictionalResistance;
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    public float getBaseResistance() {
        return baseResistance;
    }

    public void setBaseResistance(float baseResistance) {
        if (baseResistance < 0 || baseResistance > 1)
            throw new IllegalArgumentException("baseResistance must be '0 <= x <= 1'");
        this.baseResistance = baseResistance;
    }
}