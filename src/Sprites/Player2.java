package Sprites;

import GameManager.Main.KeyHandler;
import GameManager.Sprite.GameSprite;

import java.awt.event.KeyEvent;

public class Player2 extends GameSprite {

    public void start() {
        x = 500;
        y = 200;
    }

    public void update() {
        if (KeyHandler.isPressed(KeyEvent.VK_UP))
            velocityY = -5;
        if (KeyHandler.isPressed(KeyEvent.VK_DOWN))
            velocityY = 5;
        if (KeyHandler.isPressed(KeyEvent.VK_LEFT))
            velocityX = -5;
        if (KeyHandler.isPressed(KeyEvent.VK_RIGHT))
            velocityX = 5;
    }

    public void draw() {
        drawImage(imageResource[0], x, y);
    }
}