package marodi.physics;

import marodi.control.Game;

public abstract class Physics {

    public final Collision collision = new Collision(this);
    float baseFrictionalResistance;

    public void update(Game game) {
        for (PhysicalPositional ph : game.getActivePhysicalPositionals()) {
            if (ph.noVelo) {
                ph.velocityX = 0;
                ph.velocityY = 0;
            } else {
                ph.changeVelocityWithResistance(getHorizontalResistance(ph), getVerticalResistance(ph), game.getFrameTime());
                ph.changePosByVelocity(game.getFrameTime());
            }
        }
        collision.update(game);
    }

    abstract float getHorizontalResistance(PhysicalPositional ph);

    abstract float getVerticalResistance(PhysicalPositional ph);

    public float getBaseFrictionalResistance() {
        return baseFrictionalResistance;
    }

    public void setBaseFrictionalResistance(float baseFrictionalResistance) {
        if (baseFrictionalResistance < 0 || baseFrictionalResistance > 1)
            throw new IllegalArgumentException("baseFrictionalResistance must be '0 <= x <= 1'");
        this.baseFrictionalResistance = baseFrictionalResistance;
    }
}
