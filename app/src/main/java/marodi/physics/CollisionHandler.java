package marodi.physics;

import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

public final class CollisionHandler {

    private Vector<PhysicalPositional> recentPhysList = new Vector<>();

    private final CopyOnWriteArrayList<TypeRelation> typeRelationList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<ObjectRelation> objectRelationList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<TypeObjectRelation> typeObjectRelationList = new CopyOnWriteArrayList<>();
    private final CopyOnWriteArrayList<ObjectTypeRelation> objectTypeRelationList = new CopyOnWriteArrayList<>();

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

    public void addRelation(Class<?> t1, Class<?> t2,  CollisionType collisionType) {
        typeRelationList.add(new TypeRelation(t1, t2, collisionType));
        updateCollisionObjectPairList(recentPhysList);
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

    public void addRelation(PhysicalPositional o1, PhysicalPositional o2, CollisionType collisionType) {
        objectRelationList.add(new ObjectRelation(o1, o2, collisionType));
        updateCollisionObjectPairList(recentPhysList);
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

    public void addRelation(Class<?> t, PhysicalPositional o, CollisionType collisionType) {
        typeObjectRelationList.add(new TypeObjectRelation(t, o, collisionType));
        updateCollisionObjectPairList(recentPhysList);
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

    public void addRelation(PhysicalPositional o, Class<?> t, CollisionType collisionType) {
        objectTypeRelationList.add(new ObjectTypeRelation(o, t, collisionType));
        updateCollisionObjectPairList(recentPhysList);
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

    void updateCollision(Vector<PhysicalPositional> physList, float frameProportion) {
        for (PhysicalPositional ph : physList) {
            ph.horizontalCollision = Direction.NONE;
            ph.verticalCollision = Direction.NONE;
        }
        if (!physList.equals(recentPhysList)) {
            recentPhysList = new Vector<>(physList);
            updateCollisionObjectPairList(recentPhysList);
        }
        updateVerticalCollision(physList);
        updateHorizontalCollision(physList);
        updateFriction(physList, frameProportion);
    }

    private void updateVerticalCollision(Vector<PhysicalPositional> physList) {
        // loops again if any are colliding in case one collision causes another
        Vector<PhysicalPositional> toEvaluate = new Vector<>(physList); // colliding in the current iteration
        Vector<PhysicalPositional> toBeReevaluated = new Vector<>(); // to collide in the next iteration
        // isn't affected by these collisions, so it doesn't have to be set after every iteration
        for (PhysicalPositional ph : physList) {
            ph.colX = ph.getX();
        }
        int maxIterations = 0; // a VERY generous maximum number of iterations
        for (PhysicalPositional ph : toEvaluate) {
            maxIterations += 4; // for hard stoppages
            maxIterations += ph.collisionObjectPairListVertical.size();
        }
        // everything should be evaluated a maximum of three times
        // otherwise there is probably an impossible scenario
        maxIterations *= 3;
        int iterations = 0;
        while (!toEvaluate.isEmpty() && iterations < maxIterations) {
            iterations++;
            for (PhysicalPositional ph : physList) {
                ph.colY = ph.getY();
            }
            for (PhysicalPositional ph : toEvaluate) {
                for (CollisionObjectPair c : ph.collisionObjectPairListVertical)
                    if (c.runVerticalCollision()) {
                        toBeReevaluated.add(c.o1());
                        if (c.col().isNotOneWay())
                            toBeReevaluated.add(c.o2());
                    }
                if (checkHardStoppagesVertical(ph))
                    toBeReevaluated.add(ph);
            }
            // checks and removes any duplicates in toBeReevaluated
            for (int i = 0; i < toBeReevaluated.size(); i++) {
                for (int j = 0; j < toBeReevaluated.size(); j++) {
                    if (toBeReevaluated.get(i) == toBeReevaluated.get(j) && i != j) {
                        toBeReevaluated.remove(j);
                        j--;
                    }
                }
            }
            toEvaluate = new Vector<>(toBeReevaluated);
            toBeReevaluated = new Vector<>();
        }
    }

    private void updateHorizontalCollision(Vector<PhysicalPositional> physList) {
        // loops again if any are colliding in case one collision causes another
        Vector<PhysicalPositional> toEvaluate = new Vector<>(physList); // colliding in the current iteration
        Vector<PhysicalPositional> toBeReevaluated = new Vector<>(); // to collide in the next iteration
        // isn't affected by these collisions, so it doesn't have to be set after every iteration
        for (PhysicalPositional ph : physList) {
            ph.colY = ph.getY();
        }
        int maxIterations = 0;
        for (PhysicalPositional ph : toEvaluate) {
            maxIterations += 2; // for hard stoppages
            maxIterations += ph.collisionObjectPairListHorizontal.size();
        }
        // everything should be evaluated a maximum of three times
        // otherwise there is probably an impossible scenario
        maxIterations *= 3;
        int iterations = 0;
        while (!toEvaluate.isEmpty() && iterations < maxIterations) {
            iterations++;
            for (PhysicalPositional ph : physList) {
                ph.colX = ph.getX();
            }
            for (PhysicalPositional ph : toEvaluate) {
                for (CollisionObjectPair c : ph.collisionObjectPairListHorizontal)
                    if (c.runHorizontalCollision()) {
                        toBeReevaluated.add(c.o1());
                        if (c.col().isNotOneWay())
                            toBeReevaluated.add(c.o2());
                    }
                if (checkHardStoppagesHorizontal(ph))
                    toBeReevaluated.add(ph);
            }
            // checks and removes any duplicates in toBeReevaluated
            for (int i = 0; i < toBeReevaluated.size(); i++) {
                for (int j = 0; j < toBeReevaluated.size(); j++) {
                    if (toBeReevaluated.get(i) == toBeReevaluated.get(j) && i != j) {
                        toBeReevaluated.remove(j);
                        j--;
                    }
                }
            }
            toEvaluate = new Vector<>(toBeReevaluated);
            toBeReevaluated = new Vector<>();
        }
    }

    private void updateFriction(Vector<PhysicalPositional> physList, float frameProportion) {
        for (PhysicalPositional ph : physList) {
            ph.colX = ph.getX() + ph.velocityX*frameProportion;
            ph.colY = ph.getY() + ph.velocityY*frameProportion;
        }
        for (PhysicalPositional ph : physList) {
            for (CollisionObjectPair c : ph.collisionObjectPairListFrictional) {
                c.runApplyFriction(frameProportion);
            }
        }
    }

    private void updateCollisionObjectPairList(Vector<PhysicalPositional> physList) {
        // resets CollisionObjectPair lists
        for (PhysicalPositional ph : physList) {
            ph.collisionObjectPairListVertical = new CopyOnWriteArrayList<>();
            ph.collisionObjectPairListHorizontal = new CopyOnWriteArrayList<>();
            ph.collisionObjectPairListFrictional = new CopyOnWriteArrayList<>();
        }
        // adds the PhysicalPositionals' involved collisions to their CollisionObjectPair lists
        for (ObjectRelation r : objectRelationList) {
            CollisionObjectPair c = new CollisionObjectPair(r.o1(), r.o2(), r.collisionType);
            if (r.collisionType().getDirection().isAnyHorizontal()) {
                c.o1().collisionObjectPairListHorizontal.add(c);
                c.o2().collisionObjectPairListHorizontal.add(c);
            }
            if (r.collisionType().getDirection().isAnyVertical()) {
                c.o1().collisionObjectPairListVertical.add(c);
                c.o2().collisionObjectPairListVertical.add(c);
            }
            if (r.collisionType().isFrictional()) {
                c.o1().collisionObjectPairListFrictional.add(c);
                c.o2().collisionObjectPairListFrictional.add(c);
            }
        }
        for (TypeObjectRelation r : typeObjectRelationList) {
            for (PhysicalPositional ph : physList) {
                if (r.t().isInstance(ph)) {
                    CollisionObjectPair c = new CollisionObjectPair(ph, r.o(), r.collisionType);
                    if (r.collisionType().getDirection().isAnyHorizontal()) {
                        c.o1().collisionObjectPairListHorizontal.add(c);
                        c.o2().collisionObjectPairListHorizontal.add(c);
                    }
                    if (r.collisionType().getDirection().isAnyVertical()) {
                        c.o1().collisionObjectPairListVertical.add(c);
                        c.o2().collisionObjectPairListVertical.add(c);
                    }
                    if (r.collisionType().isFrictional()) {
                        c.o1().collisionObjectPairListFrictional.add(c);
                        c.o2().collisionObjectPairListFrictional.add(c);
                    }
                }
            }
        }
        for (ObjectTypeRelation r : objectTypeRelationList)
            for (PhysicalPositional ph : physList)
                if (r.t().isInstance(ph)) {
                    CollisionObjectPair c = new CollisionObjectPair(r.o(), ph, r.collisionType);
                    if (r.collisionType().getDirection().isAnyHorizontal()) {
                        c.o1().collisionObjectPairListHorizontal.add(c);
                        c.o2().collisionObjectPairListHorizontal.add(c);
                    }
                    if (r.collisionType().getDirection().isAnyVertical()) {
                        c.o1().collisionObjectPairListVertical.add(c);
                        c.o2().collisionObjectPairListVertical.add(c);
                    }
                    if (r.collisionType().isFrictional()) {
                        c.o1().collisionObjectPairListFrictional.add(c);
                        c.o2().collisionObjectPairListFrictional.add(c);
                    }
                }
        for (TypeRelation r : typeRelationList)
            for (PhysicalPositional ph1 : physList)
                if (r.t1().isInstance(ph1))
                    for (PhysicalPositional ph2 : physList)
                        if (r.t2().isInstance(ph2)) {
                            CollisionObjectPair c = new CollisionObjectPair(ph1, ph2, r.collisionType);
                            if (r.collisionType().getDirection().isAnyHorizontal()) {
                                c.o1().collisionObjectPairListHorizontal.add(c);
                                c.o2().collisionObjectPairListHorizontal.add(c);
                            }
                            if (r.collisionType().getDirection().isAnyVertical()) {
                                c.o1().collisionObjectPairListVertical.add(c);
                                c.o2().collisionObjectPairListVertical.add(c);
                            }
                            if (r.collisionType().isFrictional()) {
                                c.o1().collisionObjectPairListFrictional.add(c);
                                c.o2().collisionObjectPairListFrictional.add(c);
                            }
                        }
        // checks for and removes any identical CollisionObjectPairs
        for (PhysicalPositional ph : physList) {
            for (CollisionObjectPair c1 : ph.collisionObjectPairListVertical) {
                for (CollisionObjectPair c2 : ph.collisionObjectPairListVertical) {
                    if (c1.isDuplicate(c2))
                        ph.collisionObjectPairListVertical.remove(c1);
                }
            }
            for (CollisionObjectPair c1 : ph.collisionObjectPairListHorizontal) {
                for (CollisionObjectPair c2 : ph.collisionObjectPairListHorizontal) {
                    if (c1.isDuplicate(c2))
                        ph.collisionObjectPairListHorizontal.remove(c1);
                }
            }
            for (CollisionObjectPair c1 : ph.collisionObjectPairListFrictional) {
                for (CollisionObjectPair c2 : ph.collisionObjectPairListFrictional) {
                    if (c1.isDuplicate(c2))
                        ph.collisionObjectPairListFrictional.remove(c1);
                }
            }
        }
    }

    private boolean checkHardStoppagesVertical(PhysicalPositional ph) {
        boolean b = false;
        for (Hitbox h : ph.hitbox) {
            if (ph.upStoppagePoint != null) {
                Float r = h.getTopSide(ph.getY());
                if (r > ph.upStoppagePoint) {
                    b = true;
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
                    b = true;
                    ph.incY(ph.downStoppagePoint - r);
                    ph.verticalCollision = Direction.DOWN;
                    if (ph.downStoppageScript == null)
                        ph.setVelocityY(0);
                    else
                        ph.downStoppageScript.onPhysicalPositionalStoppage(ph);
                }
            }
        }
        return b;
    }

    private boolean checkHardStoppagesHorizontal(PhysicalPositional ph) {
        boolean b = false;
        for (Hitbox h : ph.hitbox) {
            if (ph.rightStoppagePoint != null) {
                Float r = h.getRightSide(ph.getX());
                if (r > ph.rightStoppagePoint) {
                    b = true;
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
                    b = true;
                    ph.incX(ph.leftStoppagePoint - r);
                    ph.horizontalCollision = Direction.LEFT;
                    if (ph.leftStoppageScript == null)
                        ph.setVelocityX(0);
                    else
                        ph.leftStoppageScript.onPhysicalPositionalStoppage(ph);
                }
            }
        }
        return b;
    }
}
