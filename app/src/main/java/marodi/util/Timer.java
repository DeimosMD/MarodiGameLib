package marodi.util;

public class Timer {

    private double time;
    private double lastTime;
    private OnTimerStop onTimerStopScript = null;

    public Timer() {
        setTime(0);
    }

    public Timer(double t) {
        setTime(t);
    }

    public Timer(double t, OnTimerStop onTimerStopScript) {
        setTime(t);
        this.onTimerStopScript = onTimerStopScript;
    }

    public void setTime(double time) {
        this.time = time;
        lastTime = sysTimeSeconds();
    }

    public void update() {
        double currentTime = sysTimeSeconds();
        double prevTime = time;
        time += currentTime - lastTime;
        lastTime = currentTime;
        if (prevTime < 0 && time >= 0 && onTimerStopScript != null)
            onTimerStopScript.onTimerStop();
    }

    public double getTime() {
        return time;
    }

    private double sysTimeSeconds() {
        return System.nanoTime() / 1E9;
    }
}
