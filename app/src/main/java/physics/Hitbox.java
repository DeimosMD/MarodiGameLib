package physics;

public class Hitbox {

    private final double width;
    private final double height;
    private final double xOffSet;
    private final double yOffSet;

    public Hitbox(double w, double h, double x, double y) {
        width = w;
        height = h;
        xOffSet = x;
        yOffSet = y;
    }

    double getLeftSide(double x) {
        return x + xOffSet;
    }

    double getRightSide(double x) {
        return x + xOffSet + width;
    }

    double getBottomSide(double y) {
        return y + yOffSet;
    }

    double getTopSide(double y) {
        return y + yOffSet + height;
    }
}
