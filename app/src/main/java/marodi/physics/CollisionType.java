package marodi.physics;

public class CollisionType {

    private static final float EPSILON = 1e-3f; // helps to account for computer rounding errors

    private Direction direction;
    private boolean frictional;
    private float recoil;
    private CollisionFunctionType functionType; // if o2 should push o1 but not vice versa, it should push two ways, or there is no collision adjustment and it is simply detection
    private OnCollision onCollisionScript = null;

    public CollisionType(Direction direction, boolean frictional, float recoil, CollisionFunctionType functionType) {
        this.direction = direction;
        this.frictional = frictional;
        this.recoil = recoil;
        this.functionType = functionType;
    }

    public CollisionType(Direction direction, boolean frictional, float recoil, CollisionFunctionType functionType, OnCollision onCollisionScript) {
        this.direction = direction;
        this.frictional = frictional;
        this.recoil = recoil;
        this.functionType = functionType;
        this.onCollisionScript = onCollisionScript;
    }

    // TODO account for barriers
    boolean collideVertical(PhysicalPositional o1, PhysicalPositional o2, boolean doVeloAndScript) {
        float y = checkY(o2, o1, direction);
        if (y != 0) {
            if (functionType == CollisionFunctionType.ONE_WAY) {
                o2.incY(y);
                if (doVeloAndScript)
                    o2.velocityY = o1.velocityY + (o1.velocityY - o2.velocityY) * recoil;
            } else if (functionType == CollisionFunctionType.TWO_WAY){
                twoWayAdjustY(o1, o2, y, doVeloAndScript);
            }
            if (onCollisionScript != null && doVeloAndScript)
                onCollisionScript.onCollision(o1, o2);
            return true;
        }
        return false;
    }

    boolean collideHorizontal(PhysicalPositional o1, PhysicalPositional o2, boolean doVeloAndScript) {
        float x = checkX(o2, o1, direction);
        if (x != 0) {
            if (functionType == CollisionFunctionType.ONE_WAY) {
                o2.incX(x);
                if (doVeloAndScript)
                    o2.velocityX = o1.velocityX + (o1.velocityX - o2.velocityX) * recoil;
            } else if (functionType == CollisionFunctionType.TWO_WAY){
                twoWayAdjustX(o1, o2, x, doVeloAndScript);
            }
            if (onCollisionScript != null && doVeloAndScript)
                onCollisionScript.onCollision(o1, o2);
            return true;
        }
        return false;
    }

    public boolean applyFriction(PhysicalPositional o1, PhysicalPositional o2, float frameProportion) {
        if (frictional) {
            float x = checkX(o2, o1, direction);
            float y = checkY(o2, o1, direction);
            if (x != 0 || y != 0) {
                float frictionalCoefficient = (float)
                        Math.pow(
                                1 - (1 - o1.getSpecificFrictionalCoefficient()) * (1 - o2.getSpecificFrictionalCoefficient()),
                                frameProportion
                        );
                if (x != 0) {
                    if (functionType == CollisionFunctionType.ONE_WAY) {
                        float normalForce = Math.abs(o1.velocityX - o2.velocityX); // velocity at which they are colliding, as weight is not considered
                        float frictionalForce = normalForce * frictionalCoefficient;
                        // uses y velocity because that is the axis on which they are experiencing friction, not colliding
                        if (Math.abs(o1.velocityY - o2.velocityY) <= frictionalForce) {
                            o2.velocityY = o1.velocityY;
                        } else {
                            if (o1.velocityY < o2.velocityY) {
                                o2.velocityY -= frictionalForce;
                            } else {
                                o2.velocityY += frictionalForce;
                            }
                        }
                    }
                }
                if (y != 0) {
                    if (functionType == CollisionFunctionType.ONE_WAY) {
                        float normalForce = Math.abs(o1.velocityY - o2.velocityY);
                        float frictionalForce = normalForce * frictionalCoefficient;
                        if (Math.abs(o1.velocityX - o2.velocityX) <= frictionalForce) {
                            o2.velocityX = o1.velocityX;
                        } else {
                            if (o1.velocityX < o2.velocityX) {
                                o2.velocityX -= frictionalForce;
                            } else {
                                o2.velocityX += frictionalForce;
                            }
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }

    // tests if two objects are colliding on an axis that's being tested according to the direction
    // If so returns offset to make them not collide otherwise returns 0
    private float checkY(PhysicalPositional pushed, PhysicalPositional pusher, Direction direction) {
        if (direction == Direction.DOWN || direction == Direction.UP || direction == Direction.VERTICAL || direction == Direction.ALL) {
            float y = getVerticalCollisionOffset(pushed, pusher);
            if ((direction == Direction.DOWN || direction == Direction.VERTICAL || direction == Direction.ALL) && y > 0) {
                pushed.collidingDown = true;
                pusher.collidingUp = true;
                return y;
            } else if ((direction == Direction.UP || direction == Direction.VERTICAL || direction == Direction.ALL) && y < 0) {
                pushed.collidingUp = true;
                pusher.collidingDown = true;
                return y;
            }
        }
        return 0;
    }

    private float checkX(PhysicalPositional pushed, PhysicalPositional pusher, Direction direction) {
        if (direction == Direction.LEFT || direction == Direction.RIGHT || direction == Direction.HORIZONTAL || direction == Direction.ALL) {
            float x = getHorizontalCollisionOffset(pushed, pusher);
            if ((direction == Direction.LEFT || direction == Direction.HORIZONTAL || direction == Direction.ALL) && x > 0) {
                pushed.collidingLeft = true;
                pusher.collidingRight = true;
                return x;
            } else if ((direction == Direction.RIGHT || direction == Direction.HORIZONTAL || direction == Direction.ALL) && x < 0) {
                pushed.collidingRight = true;
                pusher.collidingLeft = true;
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

        if (r1a-EPSILON > r2a && r1a < r2b-EPSILON) return true;
        if (r1b-EPSILON > r2a && r1b < r2b-EPSILON) return true;
        if (r2a-EPSILON > r1a && r2a < r1b-EPSILON) return true;
        if (r2b-EPSILON > r1a && r2b < r1b-EPSILON) return true;
        if (Math.abs(r1a-r2a) < EPSILON && Math.abs(r1b-r2b) < EPSILON) return true;
        return false;
    }

    // if colliding on axis then return amount to adjust o1 on that axis so that it would no longer be colliding with o2 on that axis
    // else return 0
    private float getVerticalCollisionOffset(PhysicalPositional o1, PhysicalPositional o2) {
        float y = 0;
        for (Hitbox h1 : o1.hitbox)
            for (Hitbox h2 : o2.hitbox) {
                if (rangesCollide(h1.getLeftSide(o1.colX), h1.getRightSide(o1.colX),
                        h2.getLeftSide(o2.colX), h2.getRightSide(o2.colX))) {
                    if (h2.getTopSide(o2.colY)+EPSILON > h1.getBottomSide(o1.colY)
                            && h2.getTopSide(o2.prevY) <= h1.getBottomSide(o1.prevY)+EPSILON) {
                        float a = h2.getTopSide(o2.colY) - h1.getBottomSide(o1.colY);
                        if (Math.abs(y) < Math.abs(a))
                            y = a;
                    } else if (h2.getBottomSide(o2.colY) < h1.getTopSide(o1.colY)+EPSILON
                            && h2.getBottomSide(o2.prevY)+EPSILON >= h1.getTopSide(o1.prevY)) {
                        float a = h2.getBottomSide(o2.colY) - h1.getTopSide(o1.colY);
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
                if (rangesCollide(h1.getBottomSide(o1.colY), h1.getTopSide(o1.colY),
                        h2.getBottomSide(o2.colY), h2.getTopSide(o2.colY))) {
                    if (h2.getRightSide(o2.colX)+EPSILON > h1.getLeftSide(o1.colX)
                            && h2.getRightSide(o2.prevX) <= h1.getLeftSide(o1.prevX)+EPSILON) {
                        float a = h2.getRightSide(o2.colX) - h1.getLeftSide(o1.colX);
                        if (Math.abs(x) < Math.abs(a))
                            x = a;
                    } else if (h2.getLeftSide(o2.colX) < h1.getRightSide(o1.colX)+EPSILON
                            && h2.getLeftSide(o2.prevX)+EPSILON >= h1.getRightSide(o1.prevX)) {
                        float a = h2.getLeftSide(o2.colX) - h1.getRightSide(o1.colX);
                        if (Math.abs(x) < Math.abs(a))
                            x = a;
                    }
                }
            }
        return x;
    }

    private void twoWayAdjustX(PhysicalPositional o1, PhysicalPositional o2, float x, boolean doVeloAndScript) {
        float totalMomentum = o1.getMomentumX() + o2.getMomentumX();
        float totalMass = o1.mass + o2.mass;
        if (Math.abs(o1.getMomentumX()) > Math.abs(o2.getMomentumX()))
            o2.incX(x);
        else if (Math.abs(o1.getMomentumX()) < Math.abs(o2.getMomentumX()))
            o1.incX(-x);
        else {
            o1.incX(-x/2);
            o2.incX(x/2);
        }
        if (doVeloAndScript) {
            o1.setVelocityX(totalMomentum / totalMass + (o2.getMomentumX() - o1.getMomentumX()) * recoil / o1.mass);
            o2.setVelocityX(totalMomentum / totalMass + (o1.getMomentumX() - o2.getMomentumX()) * recoil / o2.mass);
        }
    }

    private void twoWayAdjustY(PhysicalPositional o1, PhysicalPositional o2, float y, boolean doVeloAndScript) {
        float totalMomentum = o1.getMomentumY() + o2.getMomentumY();
        float totalMass = o1.mass + o2.mass;
        if (Math.abs(o1.getMomentumY()) > Math.abs(o2.getMomentumY()))
            o2.incY(y);
        else if (Math.abs(o1.getMomentumY()) < Math.abs(o2.getMomentumY()))
            o1.incY(-y);
        else {
            o1.incY(-y/2);
            o2.incY(y/2);
        }
        if (doVeloAndScript) {
            o1.setVelocityY(totalMomentum / totalMass + (o2.getMomentumY() - o1.getMomentumY()) * recoil / o1.mass);
            o2.setVelocityY(totalMomentum / totalMass + (o1.getMomentumY() - o2.getMomentumY()) * recoil / o2.mass);
        }
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

    public CollisionFunctionType getFunctionType() {
        return functionType;
    }

    public void setFunctionType(CollisionFunctionType functionType) {
        this.functionType = functionType;
    }
}
