package marodi.physics;

import marodi.control.Game;

import java.util.Vector;

public final class Collision {

    public final static int ALL = 0;
    public final static int UP = 1;
    public final static int DOWN = 2;
    public final static int LEFT = 3;
    public final static int RIGHT = 4;
    public final static int VERTICAL = 5;
    public final static int HORIZONTAL = 6;
    private final Vector<TypeRelation> typeRelationList = new Vector<>();
    private final Vector<ObjectRelation> objectRelationList = new Vector<>();
    private final Vector<TypeObjectRelation> typeObjectRelationList = new Vector<>();
    private final Vector<ObjectTypeRelation> objectTypeRelationList = new Vector<>();

    // checks if two one dimensional ranges have any common solutions and returns true if there are any
    private boolean rangesCollide(float r1a, float r1b, float r2a, float r2b) {

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
    float getVerticalCollisionOffset(PhysicalPositional o1, PhysicalPositional o2) {
        float y = 0;
        for (Hitbox h1 : o1.hitbox)
            for (Hitbox h2 : o2.hitbox) {
                if (rangesCollide(h1.getLeftSide(o1.getX()), h1.getRightSide(o1.getX()),
                        h2.getLeftSide(o2.getX()), h2.getRightSide(o2.getX()))) {
                    if (h2.getTopSide(o2.getY()) > h1.getBottomSide(o1.getY())
                            && h2.getTopSide(o2.prevY) <= h1.getBottomSide(o1.prevY)) {
                        float a = h2.getTopSide(o2.getY()) - h1.getBottomSide(o1.getY());
                        if (Math.abs(y) < Math.abs(a))
                            y = a;
                    } else if (h2.getBottomSide(o2.getY()) < h1.getTopSide(o1.getY())
                            && h2.getBottomSide(o2.prevY) >= h1.getTopSide(o1.prevY)) {
                        float a = h2.getBottomSide(o2.getY()) - h1.getTopSide(o1.getY());
                        if (Math.abs(y) < Math.abs(a))
                            y = a;
                    }
                }
            }
        return y;
    }

    private float getHorizontalCollisionOffset(PhysicalPositional o1, PhysicalPositional o2) {
        float x = 0;
        for (Hitbox h1 : o1.hitbox)
            for (Hitbox h2 : o2.hitbox) {
                if (rangesCollide(h1.getBottomSide(o1.getY()), h1.getTopSide(o1.getY()),
                        h2.getBottomSide(o2.getY()), h2.getTopSide(o2.getY()))) {
                    if (h2.getRightSide(o2.getX()) > h1.getLeftSide(o1.getX())
                            && h2.getRightSide(o2.prevX) <= h1.getLeftSide(o1.prevX)) {
                        float a = h2.getRightSide(o2.getX()) - h1.getLeftSide(o1.getX());
                        if (Math.abs(x) < Math.abs(a))
                            x = a;
                    } else if (h2.getLeftSide(o2.getX()) < h1.getRightSide(o1.getX())
                            && h2.getLeftSide(o2.prevX) >= h1.getRightSide(o1.prevX)) {
                        float a = h2.getLeftSide(o2.getX()) - h1.getRightSide(o1.getX());
                        if (Math.abs(x) < Math.abs(a))
                            x = a;
                    }
                }
            }
        return x;
    }

    boolean[] monoCollide(PhysicalPositional o1, PhysicalPositional o2, int direction) {
        boolean[] b = new boolean[2];
        if (direction == DOWN || direction == UP || direction == VERTICAL || direction == ALL) {
            float y = getVerticalCollisionOffset(o1, o2);
            if ((direction == DOWN || direction == VERTICAL || direction == ALL) && y > 0) {
                o1.verticalCollision = DOWN;
                o2.verticalCollision = UP;
                o1.incY(y);
                b[1] = true;
            } else if ((direction == UP || direction == VERTICAL || direction == ALL) && y < 0) {
                o1.verticalCollision = UP;
                o2.verticalCollision = DOWN;
                o1.incY(y);
                b[1] = true;
            }
        }
        if (direction == LEFT || direction == RIGHT || direction == HORIZONTAL || direction == ALL) {
            float x = getHorizontalCollisionOffset(o1, o2);
            if ((direction == LEFT || direction == HORIZONTAL || direction == ALL) && x > 0) {
                o1.horizontalCollision = LEFT;
                o2.horizontalCollision = RIGHT;
                o1.incX(x);
                b[0] = true;
            } else if ((direction == RIGHT || direction == HORIZONTAL || direction == ALL) && x < 0) {
                o1.horizontalCollision = RIGHT;
                o2.horizontalCollision = LEFT;
                o1.incX(x);
                b[0] = true;
            }
        }
        return b;
    }

    private record TypeRelation(
            Class<?> t1,
            Class<?> t2,
            int direction,
            CollisionType collisionType
    ) {
    }

    private record ObjectRelation(
            PhysicalPositional o1,
            PhysicalPositional o2,
            int direction,
            CollisionType collisionType
    ) {
    }

    private record TypeObjectRelation(
            Class<?> t,
            PhysicalPositional o,
            int direction,
            CollisionType collisionType
    ) {
    }

    private record ObjectTypeRelation (
            PhysicalPositional o,
            Class<?> t,
            int direction,
            CollisionType collisionType
    ) {
    }

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

    public boolean addRelation(PhysicalPositional o1, PhysicalPositional o2, int direction, CollisionType collisionType) {
        return objectRelationList.add(new ObjectRelation(o1, o2, direction, collisionType));
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

    public boolean addRelation(Class<?> t, PhysicalPositional o, int direction, CollisionType collisionType) {
        return typeObjectRelationList.add(new TypeObjectRelation(t, o, direction, collisionType));
    }

    public boolean removeRelation(Class<?> t, PhysicalPositional o) {
        int a = 0;
        for (TypeObjectRelation r : typeObjectRelationList)
            if (o == r.o && t == r.t) {
                typeObjectRelationList.remove(r);
                a++;
            }
        return a > 0;
    }

    public boolean addRelation(PhysicalPositional o, Class<?> t, int direction, CollisionType collisionType) {
        return objectTypeRelationList.add(new ObjectTypeRelation(o, t, direction, collisionType));
    }

    public boolean removeRelation(PhysicalPositional o, Class<?> t) {
        int a = 0;
        for (ObjectTypeRelation r : objectTypeRelationList)
            if (o == r.o && t == r.t) {
                objectTypeRelationList.remove(r);
                a++;
            }
        return a > 0;
    }

    void update(Game game) {
        Vector<PhysicalPositional> physList = game.getActivePhysicalPositionals();
        for (PhysicalPositional ph : physList) {
            ph.horizontalCollision = 0;
            ph.verticalCollision = 0;
            checkHardStoppages(ph);
        }
        for (ObjectRelation r : objectRelationList)
            r.collisionType().col(r.o1(), r.o2(), r.direction());
        for (TypeObjectRelation r : typeObjectRelationList)
            for (PhysicalPositional ph : physList)
                if (ph.getClass() == r.t())
                    r.collisionType().col(ph, r.o(), r.direction());
        for (ObjectTypeRelation r : objectTypeRelationList)
            for (PhysicalPositional ph : physList)
                if (ph.getClass() == r.t())
                    r.collisionType().col(r.o(), ph, r.direction());
        for (TypeRelation r : typeRelationList)
            for (PhysicalPositional ph1 : physList)
                if (ph1.getClass() == r.t1())
                    for (PhysicalPositional ph2 : physList)
                         if (ph2.getClass() == r.t2())
                            r.collisionType().col(ph1, ph2, r.direction());
    }

    public static class MonoPush extends CollisionType {
        private MonoPush(Collision collision) {
            super(collision);
        }
        private MonoPush() throws InstantiationException {
            super(null);
            throw new InstantiationException();
        }

        boolean col(PhysicalPositional pusher, PhysicalPositional pushed, int direction) {
            boolean[] b = collision.monoCollide(pushed, pusher, direction);
            if (b[1]) {
                pushed.velocityY = pusher.velocityY;
            }
            if (b[0]) {
                pushed.velocityX = pusher.velocityX;
            }
            return b[1] || b[0];
        }
    }

    public MonoPush monoPush() {
        return new MonoPush(this);
    }

    public static class WeightedPush extends CollisionType {

        private WeightedPush(Collision collision) {
            super(collision);
        }
        private WeightedPush() throws InstantiationException {
            super(null);
            throw new InstantiationException();
        }

        boolean col(PhysicalPositional o1, PhysicalPositional o2, int direction) {
            boolean b = false;
            if (direction == DOWN || direction == UP || direction == VERTICAL || direction == ALL) {
                float y = collision.getVerticalCollisionOffset(o1, o2);
                if ((direction == DOWN || direction == VERTICAL || direction == ALL) && y > 0) {
                    o1.verticalCollision = DOWN;
                    o2.verticalCollision = UP;
                    adjustY(o1, o2, y);
                    b = true;
                } else if ((direction == UP || direction == VERTICAL || direction == ALL) && y < 0) {
                    o1.verticalCollision = UP;
                    o2.verticalCollision = DOWN;
                    adjustY(o1, o2, y);
                    b = true;
                }
            }
            if (direction == LEFT || direction == RIGHT || direction == HORIZONTAL || direction == ALL) {
                float x = collision.getHorizontalCollisionOffset(o1, o2);
                if ((direction == LEFT || direction == HORIZONTAL || direction == ALL) && x > 0) {
                    o1.horizontalCollision = LEFT;
                    o2.horizontalCollision = RIGHT;
                    adjustX(o1, o2, x);
                    b = true;
                } else if ((direction == RIGHT || direction == HORIZONTAL || direction == ALL) && x < 0) {
                    o1.horizontalCollision = RIGHT;
                    o2.horizontalCollision = LEFT;
                    adjustX(o1, o2, x);
                    b = true;
                }
            }
            return b;
        }

        private boolean isSameSign(float n1, float n2) {
            if ((n1 == 0 && n2 != 0) || (n2 == 0 && n1 != 0)) return false;
            if (n1 == 0 && (n2 == 0)) return true;
            return Math.abs(n1)/n1 == Math.abs(n2)/n2;
        }

        private void adjustX(PhysicalPositional o1, PhysicalPositional o2, float x) {
            float v = (o1.mass * o1.velocityX + o2.mass * o2.velocityX) / (o1.mass + o2.mass);
            if (isSameSign(o1.getVelocityX(), v))
                o2.incX(-x);
            else if (isSameSign(o2.getVelocityX(), v))
                o1.incX(x);
            else {
                o1.incY(x/2);
                o2.incY(-x/2);
            }
            o1.setVelocityX(v);
            o2.setVelocityX(v);
        }

        private void adjustY(PhysicalPositional o1, PhysicalPositional o2, float y) {
            float v = (o1.mass * o1.velocityY + o2.mass * o2.velocityY) / (o1.mass + o2.mass);
            if (isSameSign(o1.getVelocityY(), v))
                o2.incY(-y);
            else if (isSameSign(o2.getVelocityY(), v))
                o1.incY(y);
            else {
                o1.incY(y/2);
                o2.incY(-y/2);
            }
            o1.setVelocityY(v);
            o2.setVelocityY(v);
        }
    }

    public WeightedPush weightedPush() {
        return new WeightedPush(this);
    }

    public static class MonoBounce extends CollisionType {

        private final float bounciness;

        private MonoBounce(Collision collision, float bounciness) {
            super(collision);
            this.bounciness = bounciness;
        }

        private MonoBounce() throws InstantiationException {
            super(null);
            throw new InstantiationException();
        }

        boolean col(PhysicalPositional pushed, PhysicalPositional pusher, int direction) {
            boolean[] b = collision.monoCollide(pushed, pusher, direction);
            if (b[1]) {
                pushed.velocityY *= -bounciness;
            }
            if (b[0]) {
                pushed.velocityX *= -bounciness;
            }
            return b[1] || b[0];
        }
    }

    public MonoBounce monoBounce(float bounciness) {
        if (bounciness < 0)
            throw (new IllegalArgumentException());
        return new MonoBounce(this, bounciness);
    }

    private void checkHardStoppages(PhysicalPositional ph) {
        for (Hitbox h : ph.hitbox) {
            if (ph.rightStoppagePoint != null) {
                Float r = h.getRightSide(ph.getX());
                if (r > ph.rightStoppagePoint) {
                    ph.incX(ph.rightStoppagePoint - r);
                    ph.horizontalCollision = RIGHT;
                    if (ph.rightStoppageScript == null)
                        ph.setVelocityX(0);
                    else
                        ph.rightStoppageScript.onPhysicalPositionalStoppage(ph);
                }
            }
            if (ph.leftStoppagePoint != null) {
                Float r = h.getLeftSide(ph.getX());
                if (r < ph.leftStoppagePoint) {
                    ph.incX(ph.leftStoppagePoint - r);
                    ph.horizontalCollision = LEFT;
                    if (ph.leftStoppageScript == null)
                        ph.setVelocityX(0);
                    else
                        ph.leftStoppageScript.onPhysicalPositionalStoppage(ph);
                }
            }
            if (ph.upStoppagePoint != null) {
                Float r = h.getTopSide(ph.getY());
                if (r > ph.upStoppagePoint) {
                    ph.incY(ph.upStoppagePoint - r);
                    ph.verticalCollision = UP;
                    if (ph.upStoppageScript == null)
                        ph.setVelocityY(0);
                    else
                        ph.upStoppageScript.onPhysicalPositionalStoppage(ph);
                }
            }
            if (ph.downStoppagePoint != null) {
                Float r = h.getBottomSide(ph.getY());
                if (r < ph.downStoppagePoint) {
                    ph.incY(ph.downStoppagePoint - r);
                    ph.verticalCollision = DOWN;
                    if (ph.downStoppageScript == null)
                        ph.setVelocityY(0);
                    else
                        ph.downStoppageScript.onPhysicalPositionalStoppage(ph);
                }
            }
        }
    }
}
