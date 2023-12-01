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
    protected double velocityX = 0;
    protected double velocityY = 0;
    protected double mass = 1;

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
        if (resistance < 0 || resistance > 1) throw (new IllegalArgumentException());
        this.frictionalResistance = resistance;
    }

    double getSpecVerticalResistance(Physics physics) {
        if (physics instanceof PlatformerPhysics) {
            return verticalAirResistance;
        } else if (physics instanceof RoamerPhysics) {
            if (horizontalCollision != 0)
                return resistance + frictionalResistance;
            else
                return resistance;
        }
        throw (new IllegalArgumentException());
    }

    double getSpecHorizontalResistance(Physics physics) {
        if (physics instanceof PlatformerPhysics) {
            if (verticalCollision != 0)
                return horizontalAirResistance + frictionalResistance;
            else
                return horizontalAirResistance;
        } else if (physics instanceof RoamerPhysics) {
            if (verticalCollision != 0)
                return resistance + frictionalResistance;
            else
                return resistance;
        }
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

    protected int getVerticalCollision() {
        return verticalCollision;
    }

    protected int getHorizontalCollision() {
        return horizontalCollision;
    }

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }
}
