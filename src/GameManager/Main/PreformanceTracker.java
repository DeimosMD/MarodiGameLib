package GameManager.Main;

public class PreformanceTracker {

    public int rate = 0;
    public boolean prints = false;
    public String label;

    public PreformanceTracker(String label, Boolean prints) {
        this.label = label;
        this.prints = prints;
    }

    public PreformanceTracker(String labal) {
        this.label = labal;
    }

    public PreformanceTracker(Boolean prints, String label) {
        this.label = label;
        this.prints = prints;
    }

    public PreformanceTracker(Boolean prints) {
        this.prints = prints;
    }

    public void increaseNum() {
        SecTimer timer = new SecTimer(this);
        timer.thread.start();

        if (GameHandler.trackTPS.rate > 0 && GraphicsHandler.trackFPS.rate > 0)
            GraphicsHandler.TPSFPSRatio = (double) GraphicsHandler.trackFPS.rate / (double) GameHandler.trackTPS.rate;

        if (prints && label != null)
            System.out.println(label + ": " + rate);
    }
}