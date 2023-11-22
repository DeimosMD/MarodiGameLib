package GameManager.Main;

public class SecTimer implements Runnable {

    Thread thread;
    PreformanceTracker tracker;

    public SecTimer(PreformanceTracker tracker) {
        thread = new Thread(this);
        this.tracker = tracker;
    }

    @Override
    public void run() {
        tracker.rate++;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        tracker.rate--;
    }
}