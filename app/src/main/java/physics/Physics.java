package physics;

import control.Game;

public abstract class Physics {

    public final Collision collision = new Collision(this);
    double baseFrictionalResistance;

    public void update(Game game) {
        for (PhysicalPositional ph : game.getActivePhysicalPositionals()) {
            ph.changeVelocityWithResistance(getHorizontalResistance(ph), getVerticalResistance(ph));
            ph.changePosByVelocity();
        }
        collision.update(game);
    }

    abstract double getHorizontalResistance(PhysicalPositional ph);

    abstract double getVerticalResistance(PhysicalPositional ph);

    public double getBaseFrictionalResistance() {
        return baseFrictionalResistance;
    }

    public void setBaseFrictionalResistance(double baseFrictionalResistance) {
        if (baseFrictionalResistance < 0 || baseFrictionalResistance > 1)
            throw new IllegalArgumentException("baseFrictionalResistance must be '0 <= x <= 1'");
        this.baseFrictionalResistance = baseFrictionalResistance;
    }
}
