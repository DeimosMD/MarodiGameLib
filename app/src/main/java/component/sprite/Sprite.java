package component.sprite;

import control.Game;
import physics.PhysicalPositional;

public abstract class Sprite extends PhysicalPositional implements Updatable, Drawable {
    private int lifespan;
    protected boolean visible = true;
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
}