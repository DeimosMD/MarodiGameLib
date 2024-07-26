package marodi.physics;

class CollisionObjectPair {

    final PhysicalPositional o1;
    final PhysicalPositional o2;
    final CollisionType col;

    // false if col has already returned true this frame
    boolean doVeloAndScript = true;

    CollisionObjectPair(PhysicalPositional o1, PhysicalPositional o2, CollisionType col) {
        this.o1 = o1;
        this.o2 = o2;
        this.col = col;
    }

    boolean runVerticalCollision() {
        return col.collideVertical(o1, o2, doVeloAndScript);
    }

    boolean runHorizontalCollision() {
        return col.collideHorizontal(o1, o2, doVeloAndScript);
    }

    boolean runApplyFriction(float frameProportion) {
        return col.applyFriction(o1, o2, frameProportion);
    }

    boolean isDuplicate(CollisionObjectPair o) {
        if (o == this) return false;
        if (o.o1 == o1 && o.o2 == o2 && o.col == col) return true;
        if (o.o1 == o2 && o.o2 == o1 && o.col == col && col.getFunctionType() != CollisionFunctionType.ONE_WAY) return true;
        return false;
    }
}
