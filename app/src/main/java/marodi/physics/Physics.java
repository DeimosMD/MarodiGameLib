package marodi.physics;

import marodi.control.Game;

public abstract class Physics {

    public final CollisionHandler collisionHandler = new CollisionHandler();
    float baseFrictionalResistance;

    public void update(Game game) {
        for (PhysicalPositional ph : game.getActivePhysicalPositionals()) {
            if (ph.noVelo) {
                ph.velocityX = 0;
                ph.velocityY = 0;
            } else {
                ph.prevX = ph.getX();
                ph.prevY = ph.getY();
                ph.changeVelocityWithResistance(getHorizontalResistance(ph), getVerticalResistance(ph), game.getFrameProportion());
                ph.changePosByVelocity(game.getFrameProportion());
            }
        }
        collisionHandler.update(game);
    }

    abstract float getHorizontalResistance(PhysicalPositional ph);

    abstract float getVerticalResistance(PhysicalPositional ph);

    public float getBaseFrictionalResistance() {
        return baseFrictionalResistance;
    }

    public void setBaseFrictionalResistance(float baseFrictionalResistance) {
        assert baseFrictionalResistance <= 1 && baseFrictionalResistance >= 0;
        this.baseFrictionalResistance = baseFrictionalResistance;
    }
}
