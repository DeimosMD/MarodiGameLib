package marodi.physics;

import marodi.component.Sprite;
import marodi.control.Game;

import java.awt.*;

public class Hitbox {

    private final float width;
    private final float height;
    private final float xOffSet;
    private final float yOffSet;
    private final Color debugColor;

    public Hitbox(float w, float h, float x, float y) {
        width = w;
        height = h;
        xOffSet = x;
        yOffSet = y;
        debugColor = null;
    }

    public Hitbox(float w, float h, float x, float y, Color color) {
        width = w;
        height = h;
        xOffSet = x;
        yOffSet = y;
        debugColor = color;
    }

    public float getLeftSide(float x) {
        return x + xOffSet;
    }

    public float getRightSide(float x) {
        return x + xOffSet + width;
    }

    public float getBottomSide(float y) {
        return y + yOffSet;
    }

    public float getTopSide(float y) {
        return y + yOffSet + height;
    }

    public void draw(Game game, Sprite spr) {
        if (debugColor != null) {
            game.camera.drawRect(width, height, debugColor, getLeftSide(spr.getX()), getBottomSide(spr.getY()));
        }
    }
}
