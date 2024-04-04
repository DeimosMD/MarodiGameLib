package marodi.physics;

import java.util.Vector;

public final class CollisionHandler {
    
    private final Vector<TypeRelation> typeRelationList = new Vector<>();
    private final Vector<ObjectRelation> objectRelationList = new Vector<>();
    private final Vector<TypeObjectRelation> typeObjectRelationList = new Vector<>();
    private final Vector<ObjectTypeRelation> objectTypeRelationList = new Vector<>();

    private record TypeRelation(
            Class<?> t1,
            Class<?> t2,
            CollisionType collisionType
    ) {
    }

    private record ObjectRelation(
            PhysicalPositional o1,
            PhysicalPositional o2,
            CollisionType collisionType
    ) {
    }

    private record TypeObjectRelation(
            Class<?> t,
            PhysicalPositional o,
            CollisionType collisionType
    ) {
    }

    private record ObjectTypeRelation (
            PhysicalPositional o,
            Class<?> t,
            CollisionType collisionType
    ) {
    }

    public boolean addRelation(Class<?> t1, Class<?> t2,  CollisionType collisionType) {
        return typeRelationList.add(new TypeRelation(t1, t2, collisionType));
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

    public boolean addRelation(PhysicalPositional o1, PhysicalPositional o2, CollisionType collisionType) {
        return objectRelationList.add(new ObjectRelation(o1, o2, collisionType));
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

    public boolean addRelation(Class<?> t, PhysicalPositional o, CollisionType collisionType) {
        return typeObjectRelationList.add(new TypeObjectRelation(t, o, collisionType));
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

    public boolean addRelation(PhysicalPositional o, Class<?> t, CollisionType collisionType) {
        return objectTypeRelationList.add(new ObjectTypeRelation(o, t, collisionType));
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

    void updateCollision(Vector<PhysicalPositional> physList) {
        for (PhysicalPositional ph : physList) {
            checkHardStoppages(ph);
            ph.colX = ph.getX();
            ph.colY = ph.getY();
        }
        for (ObjectRelation r : objectRelationList)
            r.collisionType().collide(r.o1(), r.o2);
        for (TypeObjectRelation r : typeObjectRelationList)
            for (PhysicalPositional ph : physList)
                if (ph.getClass() == r.t())
                    r.collisionType().collide(ph, r.o);
        for (ObjectTypeRelation r : objectTypeRelationList)
            for (PhysicalPositional ph : physList)
                if (ph.getClass() == r.t())
                    r.collisionType().collide(r.o(), ph);
        for (TypeRelation r : typeRelationList)
            for (PhysicalPositional ph1 : physList)
                if (ph1.getClass() == r.t1())
                    for (PhysicalPositional ph2 : physList)
                         if (ph2.getClass() == r.t2())
                             r.collisionType().collide(ph1, ph2);
    }

    void updateFriction(Vector<PhysicalPositional> physList, float frameProportion) {
        for (PhysicalPositional ph : physList) {
            ph.horizontalCollision = Direction.NONE;
            ph.verticalCollision = Direction.NONE;
            ph.colX = ph.getX() + ph.velocityX*frameProportion;
            ph.colY = ph.getY() + ph.velocityY*frameProportion;
        }
        for (ObjectRelation r : objectRelationList)
            r.collisionType().applyFriction(r.o1(), r.o2(), frameProportion);
        for (TypeObjectRelation r : typeObjectRelationList)
            for (PhysicalPositional ph : physList)
                if (ph.getClass() == r.t())
                    r.collisionType().applyFriction(ph, r.o(), frameProportion);
        for (ObjectTypeRelation r : objectTypeRelationList)
            for (PhysicalPositional ph : physList)
                if (ph.getClass() == r.t())
                    r.collisionType().applyFriction(r.o(), ph, frameProportion);
        for (TypeRelation r : typeRelationList)
            for (PhysicalPositional ph1 : physList)
                if (ph1.getClass() == r.t1())
                    for (PhysicalPositional ph2 : physList)
                        if (ph2.getClass() == r.t2())
                            r.collisionType().applyFriction(ph1, ph2, frameProportion);
    }

    private void checkHardStoppages(PhysicalPositional ph) {
        for (Hitbox h : ph.hitbox) {
            if (ph.rightStoppagePoint != null) {
                Float r = h.getRightSide(ph.getX());
                if (r > ph.rightStoppagePoint) {
                    ph.incX(ph.rightStoppagePoint - r);
                    ph.horizontalCollision = Direction.RIGHT;
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
                    ph.horizontalCollision = Direction.LEFT;
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
                    ph.verticalCollision = Direction.UP;
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
                    ph.verticalCollision = Direction.DOWN;
                    if (ph.downStoppageScript == null)
                        ph.setVelocityY(0);
                    else
                        ph.downStoppageScript.onPhysicalPositionalStoppage(ph);
                }
            }
        }
    }
}
