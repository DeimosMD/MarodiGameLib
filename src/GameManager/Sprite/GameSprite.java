package GameManager.Sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import GameManager.Elements.*;
import GameManager.Main.GameHandler;
import GameManager.Main.GraphicsHandler;

public class GameSprite extends SpriteProperties implements Start, Update, Draw {

    protected Graphics2D graphics = GraphicsHandler.g2;

    public void setDefaults() {
        this.x = screenWidth / 2;
        this.y = screenHeight / 2;
        this.pathToImage(0, "Res/debug-textures/default-sprite.png");
    }

    public void pathToImage(int resNum, String filePath) {
        try {
            this.imageResource[resNum] = ImageIO
                    .read(getClass().getClassLoader().getResource(filePath));
            System.out.println(
                    "SPRITE INDEX " + SpriteHandler.sprites.indexOf(this)
                            + " IMAGE RESOURCE " + resNum + " " + filePath
                            + " LOADED");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void drawImage(BufferedImage img, double x, double y) {
        graphics.drawImage(img, (int) x, (int) y, null);
    }

    protected void calculateTweening() {
        if (this.tweeningStartTick != GameHandler.tickNum) {
            this.tweeningStartTick = GameHandler.tickNum;
            this.x = this.tweenDestinationX;
            this.y = this.tweenDestinationY;
            this.tweenDestinationX = this.x + this.velocityX;
            this.tweenDestinationY = this.y + this.velocityY;
        }
        this.x += this.velocityX / GraphicsHandler.TPSFPSRatio;
        this.y += this.velocityY / GraphicsHandler.TPSFPSRatio;
    }
}