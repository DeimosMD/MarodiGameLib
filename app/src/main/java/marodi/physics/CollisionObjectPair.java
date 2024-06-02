package marodi.physics;

record CollisionObjectPair(PhysicalPositional o1, PhysicalPositional o2, CollisionType col) {

    boolean runVerticalCollision() {
        return col.collideVertical(o1, o2);
    }

    boolean runHorizontalCollision() {
        return col.collideHorizontal(o1, o2);
    }

    boolean runApplyFriction(float frameProportion) {
        return col.applyFriction(o1, o2, frameProportion);
    }

    boolean isDuplicate(CollisionObjectPair o) {
        if (o == this) return false;
        if (o.o1() == o1 && o.o2() == o2 && o.col() == col) return true;
        if (o.o1() == o2 && o.o2() == o1 && o.col() == col && col.isNotOneWay()) return true;
        return false;
    }
}
