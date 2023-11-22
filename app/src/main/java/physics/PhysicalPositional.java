package physics;

import component.world.Positional;
import control.Game;

public abstract class PhysicalPositional extends Positional {

    private double resistance;
    private double verticalAirResistance;
    private double horizontalAirResistance;
    private double frictionalResistance;
    int verticalCollision;
    int horizontalCollision;
    protected Hitbox[] hitbox =  { new Hitbox(0, 0, 0, 0) };
    protected double velocityX;
    protected double velocityY;

    public void setResistance(double resistance, Game game) {
        if (!(game.getPhysics() instanceof RoamerPhysics)) throw (new IllegalArgumentException());
        if (resistance < 0 || resistance > 1) throw (new IllegalArgumentException());
        this.resistance = resistance;
    }

    public void setVerticalAirResistance(double resistance, Game game) {
        if (!(game.getPhysics() instanceof PlatformerPhysics)) throw (new IllegalArgumentException());
        if (resistance < 0 || resistance > 1) throw (new IllegalArgumentException());
        this.verticalAirResistance = resistance;
    }

    public void setHorizontalAirResistance(double resistance, Game game) {
        if (!(game.getPhysics() instanceof PlatformerPhysics)) throw (new IllegalArgumentException());
        if (resistance < 0 || resistance > 1) throw (new IllegalArgumentException());
        this.horizontalAirResistance = resistance;
    }

    public void setFrictionalResistance(double resistance, Game game) {
        if (!(game.getPhysics() instanceof PlatformerPhysics)) throw (new IllegalArgumentException());
        if (resistance < 0 || resistance > 1) throw (new IllegalArgumentException());
        this.frictionalResistance = resistance;
    }

     double getSpecVerticalResistance(Physics physics) {
        if (physics instanceof PlatformerPhysics)
            return verticalAirResistance;
        throw (new IllegalArgumentException());
    }

    double getSpecHorizontalResistance(Physics physics) {
        if (physics instanceof PlatformerPhysics) {
            if (verticalCollision == -1)
                return horizontalAirResistance + frictionalResistance;
            else
                return horizontalAirResistance;
        }
        throw (new IllegalArgumentException());
    }

    double getSpecResistance(Physics physics) {
        if (physics instanceof RoamerPhysics) return resistance;
        throw (new IllegalArgumentException());
    }

    protected Hitbox[] getHitbox() {
        return hitbox;
    }

    protected void setHitbox(Hitbox[] hitbox) {
        this.hitbox = hitbox;
    }

    public double getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void incVelocityX(double inc) {
        this.velocityX += inc;
    }

    public double getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void incVelocityY(double inc) {
        this.velocityY += inc;
    }

    void changePosByVelocity() {
        x += velocityX;
        y += velocityY;
    }

    void changeVelocityWithResistance(double resistanceX, double resistanceY) {
        velocityX *= 1 - resistanceX;
        velocityY *= 1 - resistanceY;
    }

    void changeVelocityWithResistance(double resistance) {
        velocityX *= 1 - resistance;
        velocityY *= 1 - resistance;
    }
}
