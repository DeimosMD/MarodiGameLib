package Sprites;

import GameManager.Main.KeyHandler;
import GameManager.Sprite.GameSprite;

import java.awt.event.KeyEvent;

public class Player1 extends GameSprite {

    public void start() {
        x = 200;
        y = 200;
    }

    public void update() {
        if (KeyHandler.isPressed(KeyEvent.VK_W))
            velocityY = -5;
        if (KeyHandler.isPressed(KeyEvent.VK_S))
            velocityY = 5;
        if (KeyHandler.isPressed(KeyEvent.VK_A))
            velocityX = -5;
        if (KeyHandler.isPressed(KeyEvent.VK_D))
            velocityX = 5;
    }

    public void draw() {
        drawImage(imageResource[0], x, y);
    }
}