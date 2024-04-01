package marodi.physics;

import marodi.component.Positional;
import marodi.control.Game;

public abstract class PhysicalPositional extends Positional {

    private float resistance; // used for roamer physics
    private float verticalAirResistance; // used for platformer physics
    private float horizontalAirResistance; // used for platformer physics
    private float frictionalResistance; // used for platformer or roamer physics
    Direction verticalCollision = Direction.NONE;
    Direction horizontalCollision = Direction.NONE;
    protected Hitbox[] hitbox =  {};
    protected float velocityX = 0;
    protected float velocityY = 0;
    protected float mass = 1;
    protected boolean noVelo; // disables velocity
    protected boolean noGrav; // disables gravity

    // Points at which the object will collide with and scripts that can run at these points
    protected Float leftStoppagePoint = null;
    protected Float rightStoppagePoint = null;
    protected Float downStoppagePoint = null;
    protected Float upStoppagePoint = null;
    protected OnPhysicalPositionalStoppage leftStoppageScript = null;
    protected OnPhysicalPositionalStoppage rightStoppageScript = null;
    protected OnPhysicalPositionalStoppage downStoppageScript = null;
    protected OnPhysicalPositionalStoppage upStoppageScript = null;

    // Position before most recent time effected by velocity; used for calculating collision
    float prevX;
    float prevY;

    // direction in radians
    private double directionRadians = 0;

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
        assert game.getPhysics() instanceof PlatformerPhysics;
        assert resistance <= 1 && resistance >= 0;
        this.verticalAirResistance = resistance;
    }

    public void setHorizontalAirResistance(float resistance, Game game) {
        assert game.getPhysics() instanceof PlatformerPhysics;
        assert resistance <= 1 && resistance >= 0;
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
            return resistance;
        }
        throw (new IllegalArgumentException());
    }

    float getSpecHorizontalResistance(Physics physics) {
        if (physics instanceof PlatformerPhysics) {
            return horizontalAirResistance;
        } else if (physics instanceof RoamerPhysics) {
            return resistance;
        }
        throw (new IllegalArgumentException());
    }

    void changePosByVelocity(float frameTime) {
        x += velocityX * frameTime;
        y += velocityY * frameTime;
    }

    void changeVelocityWithResistance(float resistanceX, float resistanceY, float frameTime) {
        velocityX *= (float) Math.pow(1 - resistanceX, frameTime);
        velocityY *= (float) Math.pow(1 - resistanceY, frameTime);
    }

    public void faceTowards(float x, float y) {
        directionRadians = Math.atan2(y-this.y, x-this.x);
    }

    public void faceTowards(Positional positional) {
        directionRadians = Math.atan2(positional.getY()-this.y, positional.getX()-this.x);
    }

    public void accAtAngle(float acc, double degrees, float frameTime) {
        velocityX += frameTime * acc * (float) Math.cos(Physics.degreesToRadians(degrees));
        velocityY += frameTime * acc * (float) Math.sin(Physics.degreesToRadians(degrees));
    }

    public void setVeloAtAngle(float velo, double degrees) {
        velocityX = velo * (float) Math.cos(Physics.degreesToRadians(degrees));
        velocityY = velo * (float) Math.sin(Physics.degreesToRadians(degrees));
    }

    public void accForward(float acc, float frameTime) {
        velocityX += frameTime * acc * (float) Math.cos(directionRadians);
        velocityY += frameTime * acc * (float) Math.sin(directionRadians);
    }

    public void setVeloForward(float velo) {
        velocityX = velo * (float) Math.cos(directionRadians);
        velocityY = velo * (float) Math.sin(directionRadians);
    }

    // Uses hypotenuse formula with the differences of the x values and y values
    public float distanceTo(float x, float y) {
        return (float) Math.sqrt(Math.pow(x-this.x, 2) + Math.pow(y-this.y, 2));
    }

    public float distanceTo(Positional positional) {
        return (float) Math.sqrt(Math.pow(positional.getX()-this.x, 2) + Math.pow(positional.getY()-this.y, 2));
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

    protected Direction getVerticalCollision() {
        return verticalCollision;
    }

    protected Direction getHorizontalCollision() {
        return horizontalCollision;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public Float getLeftStoppagePoint() {
        return leftStoppagePoint;
    }

    public void setLeftStoppagePoint(Float leftStoppagePoint) {
        this.leftStoppagePoint = leftStoppagePoint;
    }

    public Float getRightStoppagePoint() {
        return rightStoppagePoint;
    }

    public void setRightStoppagePoint(Float rightStoppagePoint) {
        this.rightStoppagePoint = rightStoppagePoint;
    }

    public Float getDownStoppagePoint() {
        return downStoppagePoint;
    }

    public void setDownStoppagePoint(Float downStoppagePoint) {
        this.downStoppagePoint = downStoppagePoint;
    }

    public Float getUpStoppagePoint() {
        return upStoppagePoint;
    }

    public void setUpStoppagePoint(Float upStoppagePoint) {
        this.upStoppagePoint = upStoppagePoint;
    }

    protected float getFrictionalResistance() {
        return frictionalResistance;
    }

    protected void setFrictionalResistance(float frictionalResistance) {
        this.frictionalResistance = frictionalResistance;
    }

    public void setDirectionDegrees(double directionDegrees) {
        directionRadians = Physics.degreesToRadians(directionDegrees);
    }

    public double getDirectionDegrees() {
        return Physics.radiansToDegrees(directionRadians);
    }

    public void incDirectionDegrees(double directionDegrees) {
        setDirectionDegrees(getDirectionDegrees() + directionDegrees);
    }

    public double getDirectionRadians() {
        return directionRadians;
    }

    public void setDirectionRadians(double directionRadians) {
        this.directionRadians = directionRadians;
    }
}
