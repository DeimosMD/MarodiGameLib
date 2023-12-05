package component.world;

public abstract class Positional {

    protected float x;
    protected float y;
    protected World world = null;

    public Positional(float x, float y) {
        setPos(x, y);
    }

    public Positional() {}

    public void setPos(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setPos(Positional pos) {
        this.setPos(pos.getX(), pos.getY());
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }


    public void incX(float x) {
        this.x += x;
    }

    public void incY(float y) {
        this.y += y;
    }

    @Override
    public String toString() {
        return "("+x+", "+y+")";
    }

    // Used by lib user
    public World getWorld() {
        return world;
    }
    public void setWorld(World world1) {
        if (world != null) {
            world.remove(this);
        }
        if (world1 != null)
            world1.add(this);
    }
}