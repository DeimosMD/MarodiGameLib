package GameManager.Sprite;

import java.awt.image.BufferedImage;

import GameManager.Main.RuntimeSettings;

public class SpriteProperties implements RuntimeSettings {

    protected BufferedImage[] imageResource = new BufferedImage[128];
    protected double x, y = 0; // position
    protected double velocityX, velocityY = 0; // velocity
    protected double tweenDestinationX, tweenDestinationY = 0;
    protected int tweeningStartTick = 0;
    protected boolean visible = true;
    protected boolean doesTweening = tweenByDefault;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }
}