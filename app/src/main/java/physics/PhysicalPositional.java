package physics;

import component.world.Positional;
import control.Game;

public abstract class PhysicalPositional extends Positional {

    private float resistance;
    private float verticalAirResistance;
    private float horizontalAirResistance;
    private float frictionalResistance;
    int verticalCollision;
    int horizontalCollision;
    protected Hitbox[] hitbox =  { new Hitbox(0, 0, 0, 0) };
    protected float velocityX = 0;
    protected float velocityY = 0;
    protected float mass = 1;
    protected boolean noVelo;
    protected boolean noGrav;

    public void setResistance(float resistance, Game game) {
        if (resistance < 0 || resistance > 1)
            throw (new IllegalArgumentException());
        if (game.getPhysics() instanceof RoamerPhysics) {
            this.resistance = resistance;
            return;
        }
        else if (game.getPhysics() instanceof PlatformerPhysics) {
            this.verticalAirResistance = resistance;
            this.horizontalAirResistance = resistance;
            return;
        }
        throw new IllegalArgumentException();
    }

    public void setVerticalAirResistance(float resistance, Game game) {
        if (!(game.getPhysics() instanceof PlatformerPhysics)) throw (new IllegalArgumentException());
        if (resistance < 0 || resistance > 1) throw (new IllegalArgumentException());
        this.verticalAirResistance = resistance;
    }

    public void setHorizontalAirResistance(float resistance, Game game) {
        if (!(game.getPhysics() instanceof PlatformerPhysics)) throw (new IllegalArgumentException());
        if (resistance < 0 || resistance > 1) throw (new IllegalArgumentException());
        this.horizontalAirResistance = resistance;
    }

    public void setFrictionalResistance(float resistance, Game game) {
        if (resistance < 0 || resistance > 1) throw (new IllegalArgumentException());
        this.frictionalResistance = resistance;
    }

    float getSpecVerticalResistance(Physics physics) {
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

    float getSpecHorizontalResistance(Physics physics) {
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

    public float getVelocityX() {
        return velocityX;
    }

    public void setVelocityX(float velocityX) {
        this.velocityX = velocityX;
    }

    public void incVelocityX(float inc) {
        this.velocityX += inc;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocityY(float velocityY) {
        this.velocityY = velocityY;
    }

    public void incVelocityY(float inc) {
        this.velocityY += inc;
    }

    void changePosByVelocity() {
        x += velocityX;
        y += velocityY;
    }

    void changeVelocityWithResistance(float resistanceX, float resistanceY) {
        velocityX *= 1 - resistanceX;
        velocityY *= 1 - resistanceY;
    }

    protected int getVerticalCollision() {
        return verticalCollision;
    }

    protected int getHorizontalCollision() {
        return horizontalCollision;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }
}
