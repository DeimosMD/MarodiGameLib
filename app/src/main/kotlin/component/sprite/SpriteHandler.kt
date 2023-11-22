package component.sprite

import control.Game

public class SpriteHandler (
    private val game: Game
) {

    private var sprites = ArrayList<Sprite>()

    // Called at the first tick
    public fun start() {
        for (sprite in sprites) {
            sprite.start(game)
        }
    }

    // Called every tick after the first tick
    public fun update() {
        for (sprite in sprites) {
            sprite.update(game)
        }
    }

    // Called once every frame
    public fun draw() {
        for (sprite in sprites) {
            if (sprite.isVisible) {
                sprite.draw(game)
            }
        }
    }

    public fun addSprite(s: Sprite) {
        sprites.add(s)
    }

    public fun removeSprite(s: Sprite) {
        sprites.remove(s)
    }
}