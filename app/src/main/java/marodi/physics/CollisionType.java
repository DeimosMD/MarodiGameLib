package marodi.physics;

import marodi.control.Game;

public class CollisionType {

    private Direction direction;
    private boolean frictional;
    private float recoil;
    private boolean oneWay;
    private OnCollision onCollisionScript = null;

    public CollisionType(Direction direction, boolean frictional, float recoil, boolean oneWay) {
        this.direction = direction;
        this.frictional = frictional;
        this.recoil = recoil;
        this.oneWay = oneWay;
    }

    public CollisionType(Direction direction, boolean frictional, float recoil, boolean oneWay, OnCollision onCollisionScript) {
        this.direction = direction;
        this.frictional = frictional;
        this.recoil = recoil;
        this.oneWay = oneWay;
        this.onCollisionScript = onCollisionScript;
    }

    // Collides two objects if colliding based on collision rules, if oneWay then o1 pushes o2
    public boolean collide(PhysicalPositional o1, PhysicalPositional o2, Game game) {
        float x = checkX(o2, o1, direction);
        float y = checkY(o2, o1, direction);
        if (x != 0 || y != 0) {
            if (oneWay) {
                if (frictional) {
                    float frictionalCoefficient = (1 - o1.getFrictionalResistance()) * (1 - o2.getFrictionalResistance());
                    if (x != 0) {
                        float normalForce = Math.abs(o1.velocityX - o2.velocityX); // velocity at which they are colliding, as weight is not considered
                        float frictionalForce = normalForce * frictionalCoefficient;
                        if (Math.abs(o1.velocityY - o2.velocityY) <= frictionalForce * game.getFrameProportion()) {
                            o2.velocityY = o1.velocityY;
                        } else {
                            if (o1.velocityY < o2.velocityY) {
                                o2.velocityY -= frictionalForce * game.getFrameProportion();
                            } else {
                                o2.velocityY += frictionalForce * game.getFrameProportion();
                            }
                        }
                    }
                    if (y != 0) {
                        float normalForce = Math.abs(o1.velocityY - o2.velocityY);
                        float frictionalForce = normalForce * frictionalCoefficient;
                        if (Math.abs(o1.velocityX - o2.velocityX) <= frictionalForce * game.getFrameProportion()) {
                            o2.velocityX = o1.velocityX;
                        } else {
                            if (o1.velocityX < o2.velocityX) {
                                o2.velocityX -= frictionalForce * game.getFrameProportion();
                            } else {
                                o2.velocityX += frictionalForce * game.getFrameProportion();
                            }
                        }
                    }
                }
                if (x != 0) {
                    o2.incX(x);
                    o2.velocityX = o1.velocityX + (o1.velocityX - o2.velocityX) * recoil;
                }
                if (y != 0) {
                    o2.incY(y);
                    o2.velocityY = o1.velocityY + (o1.velocityY - o2.velocityY) * recoil;
                }
            } else {
                if (x != 0) {
                    twoWayAdjustX(o1, o2, x);
                }
                if (y != 0) {
                    twoWayAdjustY(o1, o2, y);
                }
            }
            onCollisionScript.onCollision(o1, o2);
            return true;
        }
        return false;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isFrictional() {
        return frictional;
    }

    public void setFrictional(boolean frictional) {
        this.frictional = frictional;
    }

    public float getRecoil() {
        return recoil;
    }

    public void setRecoil(float recoil) {
        this.recoil = recoil;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    // tests if two objects are colliding on an axis that's being tested according to the direction
    // If so returns offset to make them not collide otherwise returns 0
    private float checkY(PhysicalPositional pushed, PhysicalPositional pusher, Direction direction) {
        if (direction == Direction.DOWN || direction == Direction.UP || direction == Direction.VERTICAL || direction == Direction.ALL) {
            float y = getVerticalCollisionOffset(pushed, pusher);
            if ((direction == Direction.DOWN || direction == Direction.VERTICAL || direction == Direction.ALL) && y > 0) {
                pushed.verticalCollision = Direction.DOWN;
                pusher.verticalCollision = Direction.UP;
                return y;
            } else if ((direction == Direction.UP || direction == Direction.VERTICAL || direction == Direction.ALL) && y < 0) {
                pushed.verticalCollision = Direction.UP;
                pusher.verticalCollision = Direction.DOWN;
                return y;
            }
        }
        return 0;
    }

    private float checkX(PhysicalPositional pushed, PhysicalPositional pusher, Direction direction) {
        if (direction == Direction.LEFT || direction == Direction.RIGHT || direction == Direction.HORIZONTAL || direction == Direction.ALL) {
            float x = getHorizontalCollisionOffset(pushed, pusher);
            if ((direction == Direction.LEFT || direction == Direction.HORIZONTAL || direction == Direction.ALL) && x > 0) {
                pushed.horizontalCollision = Direction.LEFT;
                pusher.horizontalCollision = Direction.RIGHT;
                return x;
            } else if ((direction == Direction.RIGHT || direction == Direction.HORIZONTAL || direction == Direction.ALL) && x < 0) {
                pushed.horizontalCollision = Direction.RIGHT;
                pusher.horizontalCollision = Direction.LEFT;
                return x;
            }
        }
        return 0;
    }

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
    private float getVerticalCollisionOffset(PhysicalPositional o1, PhysicalPositional o2) {
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

    private float getSign(float n) {
        if (n > 0) return 1;
        if (n < 0) return -1;
        return 0;
    }

    private boolean isSameSign(float n1, float n2) {
        if ((n1 == 0 && n2 != 0) || (n2 == 0 && n1 != 0)) return false;
        if (n1 == 0 && (n2 == 0)) return true;
        return Math.abs(n1)/n1 == Math.abs(n2)/n2;
    }

    private void twoWayAdjustX(PhysicalPositional o1, PhysicalPositional o2, float x) {
        float momentum = o1.mass * o1.velocityX + o2.mass * o2.velocityX;
        float totalMass = o1.mass + o2.mass;
        if (isSameSign(o1.getVelocityX(), momentum))
            o2.incX(-x);
        else if (isSameSign(o2.getVelocityX(), momentum))
            o1.incX(x);
        else {
            o1.incX(x/2);
            o2.incX(-x/2);
        }
        o1.setVelocityX(momentum/totalMass*(1-recoil)-momentum/o1.mass*recoil*getSign(o1.getVelocityX()));
        o2.setVelocityX(momentum/totalMass*(1-recoil)-momentum/o2.mass*recoil*getSign(o2.getVelocityX()));
    }

    private void twoWayAdjustY(PhysicalPositional o1, PhysicalPositional o2, float y) {
        float momentum = o1.mass * o1.velocityY + o2.mass * o2.velocityY;
        float totalMass = o1.mass + o2.mass;
        if (isSameSign(o1.getVelocityY(), momentum))
            o2.incY(-y);
        else if (isSameSign(o2.getVelocityY(), momentum))
            o1.incY(y);
        else {
            o1.incY(y/2);
            o2.incY(-y/2);
        }
        o1.setVelocityY(momentum/totalMass*(1-recoil)-momentum/o1.mass*recoil*getSign(o1.getVelocityY()));
        o2.setVelocityY(momentum/totalMass*(1-recoil)-momentum/o2.mass*recoil*getSign(o2.getVelocityY()));
    }
}
