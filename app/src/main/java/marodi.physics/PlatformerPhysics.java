package marodi.physics;

import marodi.control.Game;

public class PlatformerPhysics extends Physics {

    private float baseVerticalAirResistance;
    private float baseHorizontalAirResistance;
    private float gravity = 0.0f;


    @Override
    public void update(Game game) {
        for (PhysicalPositional ph : game.getActivePhysicalPositionals())
            if (!(ph.noVelo || ph.noGrav))
                ph.velocityY -= ph.getWorld().getGravity(game) * game.getFrameTime();
        super.update(game);
    }

    float getHorizontalResistance(PhysicalPositional ph) {
        float a;
        if (ph.verticalCollision != 0)
            a = getBaseHorizontalAirResistance() + getBaseFrictionalResistance() + ph.getSpecHorizontalResistance(this);
        else
            a = getBaseHorizontalAirResistance() + ph.getSpecHorizontalResistance(this);
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    float getVerticalResistance(PhysicalPositional ph) {
        float a = ph.getSpecVerticalResistance(this) + getBaseVerticalAirResistance();
        if (a >= 1) return 1;
        if (a <= 0) return 0;
        return a;
    }

    public float getBaseVerticalAirResistance() {
        return baseVerticalAirResistance;
    }

    public void setBaseVerticalAirResistance(float baseVerticalAirResistance) {
        if (baseVerticalAirResistance < 0 || baseVerticalAirResistance > 1)
            throw new IllegalArgumentException("baseVerticalAirResistance must be '0 <= x <= 1'");
        this.baseVerticalAirResistance = baseVerticalAirResistance;
    }

    public float getBaseHorizontalAirResistance() {
        return baseHorizontalAirResistance;
    }

    public void setBaseHorizontalAirResistance(float baseHorizontalAirResistance) {
        if (baseHorizontalAirResistance < 0 || baseHorizontalAirResistance > 1)
            throw new IllegalArgumentException("baseHorizontalAirResistance must be '0 <= x <= 1'");
        this.baseHorizontalAirResistance = baseHorizontalAirResistance;
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }
}