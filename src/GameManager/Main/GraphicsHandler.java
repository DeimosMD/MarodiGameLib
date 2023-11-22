package GameManager.Main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import GameManager.Elements.ElementHandler;
import GameManager.Sprite.SpriteHandler;

public class GraphicsHandler extends JPanel implements Runnable, RuntimeSettings {

    public static PreformanceTracker trackFPS;
    public static Graphics g;
    public static Graphics2D g2 = (Graphics2D) g;
    public static double TPSFPSRatio = GameHandler.TPS / FPS;

    Thread graphicsThread;

    public GraphicsHandler() {
        this.setDoubleBuffered(true);
        this.setFocusable(true);
        this.setBackground(backgroundColor);
        trackFPS = new PreformanceTracker("FPS", framesPerSecondPrints);

        graphicsThread = new Thread(this);
        graphicsThread.start();
    }

    @Override
    public void run() {
        long targetFrameNanoTime = (long) (1.0 / FPS * 1_000_000_000); // total time per frame in nanoseconds
        while (graphicsThread != null) {
            trackFPS.increaseNum();
            long startTime = System.nanoTime();
            repaint();
            long timeElapsed = System.nanoTime() - startTime;
            try {
                long sleepTime = (targetFrameNanoTime - timeElapsed) / 1_000_000;
                if (sleepTime > 0) {
                    Thread.sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g2 = (Graphics2D) g;
        ElementHandler.draw();
        SpriteHandler.draw();
        g2.dispose();
    }
}