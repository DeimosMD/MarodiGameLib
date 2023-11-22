package component.world;

public abstract class Positional {

    protected double x;
    protected double y;
    protected Vector2D vector = null;

    public Positional(int x, int y) {
        setPos(x, y);
    }

    public Positional() {}

    public void setPos(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setPos(Positional pos) {
        this.setPos(pos.getX(), pos.getY());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }


    public void incX(double x) {
        this.x += x;
    }

    public void incY(double y) {
        this.y += y;
    }

    // Used by Vector2D for add/remove
    Vector2D getVector() {
        return vector;
    }
    void setVector(Vector2D vector) {
        this.vector = vector;
    }

    @Override
    public String toString() {
        return "("+x+", "+y+")";
    }

    // Used by lib user
    public World getWorld() {
        return vector.getWorld();
    }
    public void setWorld(World world) {
        if (vector != null) {
            vector.remove(this);
        }
        if (world != null)
            world.getVector2D().add(this);
    }
}