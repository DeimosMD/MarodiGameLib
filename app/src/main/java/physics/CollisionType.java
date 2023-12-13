package physics;

public abstract class CollisionType {
    final Collision collision;
    CollisionType(Collision collision) {
        this.collision = collision;
    }
    abstract boolean col(
            PhysicalPositional o1,
            PhysicalPositional o2,
            int direction
    );
}
