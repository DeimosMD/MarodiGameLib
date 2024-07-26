package marodi.physics;

import marodi.control.Game;

import java.util.Vector;

public class Physics {

    public final CollisionHandler collisionHandler = new CollisionHandler();
    private float baseFrictionalResistance = 0.0f;
    private float baseVerticalResistance = 0.0f;
    private float baseHorizontalResistance = 0.0f;
    private float gravity = 0.0f;

    public void update(Game game) {
        Vector<PhysicalPositional> physList = game.getActivePhysicalPositionals();
        for (PhysicalPositional ph : physList)
            ph.updatePhysicalVariables(game);
        collisionHandler.updateCollision(physList, getBaseFrictionalResistance());
    }

    static double radiansToDegrees(double radians) {
        return radians * (180/Math.PI);
    }

    static double degreesToRadians(double degrees) {
        return degrees * (Math.PI/180);
    }

    public float getGravity() {
        return gravity;
    }

    public void setGravity(float gravity) {
        this.gravity = gravity;
    }

    public float getBaseFrictionalResistance() {
        return baseFrictionalResistance;
    }

    public void setBaseFrictionalResistance(float baseFrictionalResistance) {
        if (baseFrictionalResistance < 0 || baseFrictionalResistance > 1) throw (new IllegalArgumentException());
        this.baseFrictionalResistance = baseFrictionalResistance;
    }

    public float getBaseVerticalResistance() {
        return baseVerticalResistance;
    }

    public void setBaseVerticalResistance(float baseVerticalResistance) {
        if (baseVerticalResistance < 0 || baseVerticalResistance > 1) throw (new IllegalArgumentException());
        this.baseVerticalResistance = baseVerticalResistance;
    }

    public float getBaseHorizontalResistance() {
        return baseHorizontalResistance;
    }

    public void setBaseHorizontalResistance(float baseHorizontalResistance) {
        if (baseHorizontalResistance < 0 || baseHorizontalResistance > 1) throw (new IllegalArgumentException());
        this.baseHorizontalResistance = baseHorizontalResistance;
    }
}
