package control;

import java.awt.Color;

public class RuntimeSettings {

    // SCREEN
    private String windowTitle = "Game Window";
    private int screenWidth = 900;
    private int screenHeight = 900;
    private int FPS = 60;
    private Color backgroundColor = Color.BLACK;
    private boolean resizable = false;

    // DEBUG
    private boolean printFramesPerSecond = false;

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public int getFPS() {
        return FPS;
    }

    public void setFPS(int FPS) {
        this.FPS = FPS;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isPrintFramesPerSecond() {
        return printFramesPerSecond;
    }

    public void setPrintFramesPerSecond(boolean printFramesPerSecond) {
        this.printFramesPerSecond = printFramesPerSecond;
    }
}