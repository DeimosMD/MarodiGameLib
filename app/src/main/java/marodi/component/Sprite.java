package marodi.component;

import marodi.control.Game;
import marodi.physics.PhysicalPositional;

public abstract class Sprite extends PhysicalPositional implements Updatable, Drawable {
    private int lifespan;
    protected boolean visible = true;
    protected float depth = 0; // higher depth means drawn before other sprites

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    public boolean isVisible() {
        return visible;
    }
    abstract public void start(Game game);
    public int getLifespan() {
        return lifespan;
    }
    public void increaseLifespan() {
        lifespan++;
    }
    public float getDepth() {
        return depth;
    }
    public void setDepth(float depth) {
        this.depth = depth;
    }
}