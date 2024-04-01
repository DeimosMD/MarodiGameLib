package marodi.physics;

public class RoamerPhysics extends Physics {

    private float baseResistance;

    float getHorizontalResistance(PhysicalPositional ph) {
        float a = getBaseResistance() + ph.getSpecHorizontalResistance(this);
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    float getVerticalResistance(PhysicalPositional ph) {
        float a = getBaseResistance() + ph.getSpecVerticalResistance(this);
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    public float getBaseResistance() {
        return baseResistance;
    }

    public void setBaseResistance(float baseResistance) {
        assert baseResistance <= 1 && baseResistance >= 0;
        this.baseResistance = baseResistance;
    }
}