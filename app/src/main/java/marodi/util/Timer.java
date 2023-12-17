package marodi.util;

public class Timer {

    private double time;
    private double lastTime;

    public Timer() {
        setTime(0);
    }

    public Timer(double t) {
        setTime(t);
    }

    public void setTime(double time) {
        this.time = time;
        lastTime = sysTimeSeconds();
    }

    public void update() {
        double currentTime = sysTimeSeconds();
        time -= currentTime - lastTime;
        lastTime = currentTime;
    }

    public double getTime() {
        return time;
    }

    private double sysTimeSeconds() {
        return System.nanoTime() / 1E9;
    }
}
