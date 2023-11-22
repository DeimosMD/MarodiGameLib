package physics;

public class Collision {

    public final static int ALL = 0;
    public final static int UP = 1;
    public final static int DOWN = 2;
    public final static int LEFT = 3;
    public final static int RIGHT = 4;
    public final static int VERTICAL = 5;
    public final static int HORIZONTAL = 6;

    // checks if two one dimensional ranges have any common solutions and returns true if there are any
    private static boolean rangesCollide(double r1a, double r1b, double r2a, double r2b) {

        // Swaps values if in wrong order
        if (r1a > r1b) {
            r1a += r1b;
            r1b = r1a - r1b;
            r1a -= r1b;
        }
        if (r2a > r2b) {
            r2a += r2b;
            r2b = r1a - r2b;
            r2a -= r2b;
        }

        if (r1a > r2a && r1a < r2b) return true;
        if (r1b > r2a && r1b < r2b) return true;
        if (r2a > r1a && r2a < r1b) return true;
        if (r2b > r1a && r2b < r1b) return true;
        if (r1a == r2a && r1b == r2b) return true;
        return false;
    }

    // if colliding on axis return amount to adjust o1 on that axis so that it would no longer be colliding with o2 on that axis
    // else return 0
    private static double getVerticalCollisionOffset(PhysicalPositional o1, PhysicalPositional o2) {
        double y = 0;
        for (Hitbox h1 : o1.hitbox) for (Hitbox h2 : o2.hitbox) {
            if (rangesCollide(h1.getLeftSide(o1.getX()), h1.getRightSide(o1.getX()),
                    h2.getLeftSide(o2.getX()), h2.getRightSide(o2.getX()))) {
                if (h2.getTopSide(o2.getY()) > h1.getBottomSide(o1.getY())
                        && h2.getTopSide(o2.getY()) <= h1.getBottomSide(o1.getY()-o1.getVelocityY())) {
                    double a = h2.getTopSide(o2.getY()) - h1.getBottomSide(o1.getY());
                    if (Math.abs(y) < Math.abs(a))
                        y = a;
                } else if (h2.getBottomSide(o2.getY()) < h1.getTopSide(o1.getY())
                        && h2.getBottomSide(o2.getY()) >= h1.getTopSide(o1.getY()-o1.getVelocityY()))  {
                    double a = h2.getBottomSide(o2.getY()) - h1.getTopSide(o1.getY());
                    if (Math.abs(y) < Math.abs(a))
                        y = a;
                }
            }
        }
        return y;
    }

    private static double getHorizontalCollisionOffset(PhysicalPositional o1, PhysicalPositional o2) {
        double x = 0;
        for (Hitbox h1 : o1.hitbox) for (Hitbox h2 : o2.hitbox) {
            if (rangesCollide(h1.getBottomSide(o1.getY()), h1.getTopSide(o1.getY()),
                    h2.getBottomSide(o2.getY()), h2.getTopSide(o2.getY()))) {
                if (h2.getRightSide(o2.getX()) > h1.getLeftSide(o1.getX())
                        && h2.getRightSide(o2.getX()) <= h1.getLeftSide(o1.getX()-o1.getVelocityX())) {
                    double a = h2.getRightSide(o2.getX()) - h1.getLeftSide(o1.getX());
                    if (Math.abs(x) < Math.abs(a))
                        x = a;
                } else if (h2.getLeftSide(o2.getX()) < h1.getRightSide(o1.getX())
                        && h2.getLeftSide(o2.getX()) >= h1.getRightSide(o1.getX()-o1.getVelocityX()))  {
                    double a = h2.getLeftSide(o2.getX()) - h1.getRightSide(o1.getX());
                    if (Math.abs(x) < Math.abs(a))
                        x = a;
                }
            }
        }
        return x;
    }

    public static boolean collide(PhysicalPositional o1, PhysicalPositional o2, int direction) {
        int r = 0;
        if (direction == DOWN || direction == UP || direction == VERTICAL || direction == ALL) {
            double y = getVerticalCollisionOffset(o1, o2);
            if ((direction == DOWN && y > 0) ||
                    (direction == UP && y < 0) ||
                    ((direction == VERTICAL || direction == ALL) && y != 0)) {
                o1.incY(y);
                o1.setVelocityY(0);
                r += 1;
            }
        }
        if (direction == LEFT || direction == RIGHT || direction == HORIZONTAL || direction == ALL) {
            double x = getHorizontalCollisionOffset(o1, o2);
            if ((direction == LEFT && x > 0) ||
                    (direction == RIGHT && x < 0) ||
                    ((direction == HORIZONTAL || direction == ALL) && x != 0)) {
                o1.incX(x);
                o1.setVelocityX(0);
                r += 1;
            }
        }
        if (r == 0) return false;
        else return true;
    }
}