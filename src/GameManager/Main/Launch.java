package GameManager.Main;

import javax.swing.JFrame;

import GameManager.Sprite.SpriteHandler;
import Sprites.Player1;
import Sprites.Player2;

public class Launch implements RuntimeSettings {

    public static JFrame window = new JFrame(windowTitle);
    public static GameHandler gameHandler = new GameHandler();
    public static GraphicsHandler graphicsHandler = new GraphicsHandler();
    public static KeyHandler keyHandler = new KeyHandler();

    public static void main(String[] args) {
        window.add(graphicsHandler);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(screenWidth, screenHeight);
        window.setResizable(resizable);
        keyHandler.start(window);
        window.setVisible(true);
    }

    // Create and add every sprite/element that needs to be created before the
    // first tick
    public static void addDefaultScripts() {
        Player1 player1 = new Player1();
        SpriteHandler.addSprite(player1);
        Player2 player2 = new Player2();
        SpriteHandler.addSprite(player2);
    }
}