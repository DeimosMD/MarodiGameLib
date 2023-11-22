package GameManager.Main;

import java.awt.Color;

public interface RuntimeSettings {

    // SCREEN
    String windowTitle = "Game Window";
    int screenWidth = 900;
    int screenHeight = 900;
    int FPS = 60;
    Color backgroundColor = Color.BLACK;
    boolean resizable = false;

    // DEBUG
    boolean ticksPerSecondPrints = false;
    boolean framesPerSecondPrints = true;

    // GAME BUILDING
    int TPS = 20;
    boolean tweenByDefault = true;
}