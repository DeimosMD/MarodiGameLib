package physics;

import control.Game;

public abstract class Physics {

    public final Collision collision = new Collision(this);
    double baseFrictionalResistance;

    public abstract void update(Game game);

    abstract double getHorizontalResistance(PhysicalPositional ph);

    abstract double getVerticalResistance(PhysicalPositional ph);

    public double getBaseFrictionalResistance() {
        return baseFrictionalResistance;
    }

    public void setBaseFrictionalResistance(double baseFrictionalResistance) {
        this.baseFrictionalResistance = baseFrictionalResistance;
    }
}
