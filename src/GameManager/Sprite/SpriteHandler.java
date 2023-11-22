package GameManager.Sprite;

import java.util.ArrayList;
import GameManager.Main.GraphicsHandler;

public class SpriteHandler {

    public static ArrayList<GameSprite> sprites = new ArrayList<>();

    // Called at the first tick
    public static void start() {
        for (GameSprite sprite : sprites) {
            sprite.setDefaults();
        }
        for (GameSprite sprite : sprites) {
            sprite.start();
            sprite.tweenDestinationX = sprite.x + sprite.velocityX;
            sprite.tweenDestinationY = sprite.y + sprite.velocityY;
        }
    }

    // Called every tick after the first tick
    public static void update() {
        for (GameSprite sprite : sprites) {
            sprite.velocityX = 0;
            sprite.velocityY = 0;
        }
        for (GameSprite sprite : sprites) {
            sprite.update();
            if (!sprite.doesTweening) {
                sprite.x += sprite.velocityX;
                sprite.y += sprite.velocityY;
            }
        }
    }

    // Called once every frame
    public static void draw() {
        for (GameSprite sprite : sprites) {
            sprite.graphics = GraphicsHandler.g2;
            if (sprite.isVisible())
                if (sprite.doesTweening) {
                    sprite.calculateTweening();
                }
            sprite.draw();
        }
    }

    public static void addSprite(GameSprite s) {
        sprites.add(s);
    }

    public static void removeSprite(GameSprite s) {
        int indexToRemove = sprites.indexOf(s);
        if (indexToRemove < sprites.size()) {
            sprites.remove(indexToRemove);
            for (int i = indexToRemove; i < sprites.size(); i++) {
                sprites.set(i, sprites.get(i + 1));
            }
            sprites.remove(sprites.size() - 1);
        } else {
            System.out.println("Invalid index: " + indexToRemove);
        }
    }
}