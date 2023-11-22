package control;

import component.world.World;
import physics.Physics;
import physics.RoamerPhysics;
import resource.Loader;
import component.sprite.ElementHandler;
import component.sprite.SpriteHandler;

public class Game {

    private final GraphicsPanel graphicsPanel;
    private final GameHandler gameHandler;
    protected RuntimeSettings runtimeSettings;
    protected KeyHandler keyHandler;
    protected SpriteHandler spriteHandler;
    protected ElementHandler elementHandler;
    protected Camera camera;
    protected Screen screen;
    protected Loader loader;
    private World currentWorld = new World();
    private double frameTime;
    private Physics physics = new RoamerPhysics();

    public Game() {
        runtimeSettings = new RuntimeSettings();
        screen = new Screen(runtimeSettings);
        gameHandler = new GameHandler(this);
        graphicsPanel = new GraphicsPanel(this);
        keyHandler = new KeyHandler(this);
        spriteHandler = new SpriteHandler(this);
        elementHandler = new ElementHandler(this);
        camera = new Camera(this);
        loader = new Loader(this);
    }

    protected void launch() {
        graphicsPanel.init();
        gameHandler.init();
        keyHandler.init();
        gameHandler.getThread().start();
    }

    public RuntimeSettings getRuntimeSettings() {
        return runtimeSettings;
    }

    public KeyHandler getKeyHandler() {
        return keyHandler;
    }

    public SpriteHandler getSpriteHandler() {
        return spriteHandler;
    }

    public ElementHandler getElementHandler() {
        return elementHandler;
    }

    public Camera getCamera() {
        return camera;
    }

    public Screen getScreen() {
        return screen;
    }

    public Loader getLoader() {
        return loader;
    }

    GraphicsPanel getGraphicsPanel() {
        return graphicsPanel;
    }

    GameHandler getGameHandler() {
        return gameHandler;
    }

    public double getFrameTime() {
        return frameTime;
    }

    void setFrameTime(double frameTime) {
        this.frameTime = frameTime;
    }

    public World getCurrentWorld() {
        return currentWorld;
    }

    public void setCurrentWorld(World currentWorld) {
        this.currentWorld = currentWorld;
    }

    public Physics getPhysics() {
        return physics;
    }

    public void setPhysics(Physics physics) {
        this.physics = physics;
    }
}
