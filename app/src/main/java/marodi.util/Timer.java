package marodi.util;

public class Timer {

    private long time;
    private long lastTime;

    public Timer() {
        set(0);
    }

    public Timer(long t) {
        set(t);
    }

    public void set(long time) {
        this.time = time;
        lastTime = sysTimeSeconds();
    }

    public void update() {
        long currentTime = sysTimeSeconds();
        time = currentTime - lastTime;
        lastTime = currentTime;
    }

    public long get() {
        return time;
    }

    private long sysTimeSeconds() {
        return (long) (System.nanoTime() / 1E9);
    }
}
