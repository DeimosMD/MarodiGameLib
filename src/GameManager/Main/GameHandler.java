package GameManager.Main;

import GameManager.Elements.ElementHandler;
import GameManager.Sprite.SpriteHandler;

public class GameHandler implements Runnable, RuntimeSettings {

    public static int tickNum;
    public static PreformanceTracker trackTPS;

    Thread gameThread;

    public GameHandler() {
        trackTPS = new PreformanceTracker("TPS", ticksPerSecondPrints);

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        Launch.addDefaultScripts();
        trackTPS.increaseNum();
        long targetTickNanoTime = (long) (1.0 / TPS * 1_000_000_000); // total time per tick in nanoseconds
        long startTime = System.nanoTime(); // System time in nanoseconds
        ElementHandler.start();
        SpriteHandler.start();
        long timeElapsed = startTime - System.nanoTime(); // How long the code took in nanoseconds
        try {
            long sleepTime = Math.max(targetTickNanoTime - timeElapsed, 0) / 1_000_000;
            Thread.sleep(sleepTime); // Sleeps the remaining time in milliseconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (gameThread != null) {
            trackTPS.increaseNum();
            tickNum++;
            startTime = System.nanoTime();
            KeyHandler.update();
            ElementHandler.update();
            SpriteHandler.update();
            timeElapsed = System.nanoTime() - startTime;
            try {
                long sleepTime = (targetTickNanoTime - timeElapsed) / 1_000_000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}