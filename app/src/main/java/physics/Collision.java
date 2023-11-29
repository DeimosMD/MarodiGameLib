package physics;

import control.Game;

import java.util.Vector;

public class Collision {

    public final static int ALL = 0;
    public final static int UP = 1;
    public final static int DOWN = 2;
    public final static int LEFT = 3;
    public final static int RIGHT = 4;
    public final static int VERTICAL = 5;
    public final static int HORIZONTAL = 6;
    private final Vector<TypeRelation>  typeRelationList = new Vector<>();
    private final Vector<ObjectRelation>  objectRelationList = new Vector<>();
    private final Physics physics;

    public Collision(Physics physics) {
        this.physics = physics;
    }

    // checks if two one dimensional ranges have any common solutions and returns true if there are any
    private boolean rangesCollide(double r1a, double r1b, double r2a, double r2b) {

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
    private double getVerticalCollisionOffset(PhysicalPositional o1, PhysicalPositional o2) {
        double y = 0;
        double r1 = 1/(1-physics.getVerticalResistance(o1));
        double r2 = 1/(1-physics.getVerticalResistance(o2));
        for (Hitbox h1 : o1.hitbox) for (Hitbox h2 : o2.hitbox) {
            if (rangesCollide(h1.getLeftSide(o1.getX()), h1.getRightSide(o1.getX()),
                    h2.getLeftSide(o2.getX()), h2.getRightSide(o2.getX()))) {
                if (h2.getTopSide(o2.getY()) > h1.getBottomSide(o1.getY())
                        && h2.getTopSide(o2.getY()-o2.getVelocityY()*r2) <= h1.getBottomSide(o1.getY()-o1.getVelocityY()*r1)) {
                    double a = h2.getTopSide(o2.getY()) - h1.getBottomSide(o1.getY());
                    if (Math.abs(y) < Math.abs(a))
                        y = a;
                } else if (h2.getBottomSide(o2.getY()) < h1.getTopSide(o1.getY())
                        && h2.getBottomSide(o2.getY()-o2.getVelocityY()*r2) >= h1.getTopSide(o1.getY()-o1.getVelocityY()*r1))  {
                    double a = h2.getBottomSide(o2.getY()) - h1.getTopSide(o1.getY());
                    if (Math.abs(y) < Math.abs(a))
                        y = a;
                }
            }
        }
        return y;
    }

    private double getHorizontalCollisionOffset(PhysicalPositional o1, PhysicalPositional o2) {
        double x = 0;
        double r1 = 1/(1-physics.getHorizontalResistance(o1));
        double r2 = 1/(1-physics.getHorizontalResistance(o2));
        for (Hitbox h1 : o1.hitbox) for (Hitbox h2 : o2.hitbox) {
            if (rangesCollide(h1.getBottomSide(o1.getY()), h1.getTopSide(o1.getY()),
                    h2.getBottomSide(o2.getY()), h2.getTopSide(o2.getY()))) {
                if (h2.getRightSide(o2.getX()) > h1.getLeftSide(o1.getX())
                        && h2.getRightSide(o2.getX()-o2.getVelocityX()*r2) <= h1.getLeftSide(o1.getX()-o1.getVelocityX()*r1)) {
                    double a = h2.getRightSide(o2.getX()) - h1.getLeftSide(o1.getX());
                    if (Math.abs(x) < Math.abs(a))
                        x = a;
                } else if (h2.getLeftSide(o2.getX()) < h1.getRightSide(o1.getX())
                        && h2.getLeftSide(o2.getX()-o2.getVelocityX()*r2) >= h1.getRightSide(o1.getX()-o1.getVelocityX()*r1))  {
                    double a = h2.getLeftSide(o2.getX()) - h1.getRightSide(o1.getX());
                    if (Math.abs(x) < Math.abs(a))
                        x = a;
                }
            }
        }
        return x;
    }

     int collide(PhysicalPositional o1, PhysicalPositional o2, int direction) {
        int r = 1;
        if (direction == DOWN || direction == UP || direction == VERTICAL || direction == ALL) {
            double y = getVerticalCollisionOffset(o1, o2);
            if ((direction == DOWN || direction == VERTICAL || direction == ALL) && y > 0) {
                o1.verticalCollision = DOWN;
                o2.verticalCollision = UP;
                o1.incY(y);
                r *= VERTICAL;
            } else if ((direction == UP || direction == VERTICAL || direction == ALL) && y < 0) {
                o1.verticalCollision = UP;
                o2.verticalCollision = DOWN;
                o1.incY(y);
                r *= VERTICAL;
            }
        }
        if (direction == LEFT || direction == RIGHT || direction == HORIZONTAL || direction == ALL) {
            double x = getHorizontalCollisionOffset(o1, o2);
            if ((direction == LEFT || direction == HORIZONTAL || direction == ALL) && x > 0) {
                o1.horizontalCollision = LEFT;
                o2.horizontalCollision = RIGHT;
                o1.incX(x);
                r *= HORIZONTAL;
            } else if ((direction == RIGHT || direction == HORIZONTAL || direction == ALL) && x < 0) {
                o1.horizontalCollision = RIGHT;
                o2.horizontalCollision = LEFT;
                o1.incX(x);
                r *= HORIZONTAL;
            }
        }
        return r;
    }

    private record TypeRelation(Class<?> t1, Class<?> t2, int direction, CollisionType collisionType) {}

    private record ObjectRelation(PhysicalPositional o1, PhysicalPositional o2, int direction, CollisionType collisionType) {}

    public boolean addRelation(Class<?> t1, Class<?> t2, int direction, CollisionType collisionType) {
        return typeRelationList.add(new TypeRelation(t1, t2, direction, collisionType));
    }

    public boolean removeRelation(Class<?> t1, Class<?> t2) {
        int a = 0;
        for (TypeRelation r : typeRelationList)
            if (t1 == r.t1 && t2 == r.t2) {
                typeRelationList.remove(r);
                a++;
            }
        return a > 0;
    }

    public boolean removeRelation(PhysicalPositional o1, PhysicalPositional o2) {
        int a = 0;
        for (ObjectRelation r : objectRelationList)
            if (o1 == r.o1 && o2 == r.o2) {
                objectRelationList.remove(r);
                a++;
            }
        return a > 0;
    }

    public boolean addRelation(PhysicalPositional o1, PhysicalPositional o2, int direction, CollisionType collisionType) {
        return objectRelationList.add(new ObjectRelation(o1, o2, direction, collisionType));
    }

    void update(Game game) {
        for (PhysicalPositional ph: game.getActivePhysicalPositionals()) {
            ph.horizontalCollision = 0;
            ph.verticalCollision = 0;
        }
        for (ObjectRelation r : objectRelationList) {
            r.collisionType.col(r.o1, r.o2, r.direction);
        }
        for (TypeRelation r : typeRelationList) {
            for (PhysicalPositional ph1 : game.getActivePhysicalPositionals()) {
                for (PhysicalPositional ph2 : game.getActivePhysicalPositionals()) {
                    if (ph1.getClass() == r.t1() && ph2.getClass() == r.t2())
                        r.collisionType.col(ph1, ph2, r.direction);
                }
            }
        }
    }

    public static class MonoPush implements CollisionType {

        private final Collision collision;

        private MonoPush(Collision collision) {
            this.collision = collision;
        }

        public boolean col(PhysicalPositional pushed, PhysicalPositional pusher, int direction) {
            int r = collision.collide(pushed, pusher, direction);
            if (r % VERTICAL == 0) {
                pushed.velocityY = pusher.velocityY;
            }
            if (r % HORIZONTAL == 0) {
                pushed.velocityX = pusher.velocityX;
            }
            return r != 1;
        }
    }

    public MonoPush monoPush() {
        return new MonoPush(this);
    }
}