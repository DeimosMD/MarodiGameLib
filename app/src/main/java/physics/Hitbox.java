package physics;

public class Hitbox {

    private final float width;
    private final float height;
    private final float xOffSet;
    private final float yOffSet;

    public Hitbox(float w, float h, float x, float y) {
        width = w;
        height = h;
        xOffSet = x;
        yOffSet = y;
    }

    float getLeftSide(float x) {
        return x + xOffSet;
    }

    float getRightSide(float x) {
        return x + xOffSet + width;
    }

    float getBottomSide(float y) {
        return y + yOffSet;
    }

    float getTopSide(float y) {
        return y + yOffSet + height;
    }
}
