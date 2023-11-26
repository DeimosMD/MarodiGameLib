package component.world

import component.sprite.Drawable
import component.sprite.Sprite
import component.sprite.Updatable
import control.Game

class World {

    public val vector2D: Vector2D = Vector2D(this)

    fun update(game: Game) {
        for (o in vector2D) {
            if (o is Sprite) {
                o.increaseLifespan()
                if (o.lifespan == 1)
                    o.start(game)
                else
                    o.update(game)
            } else {
                if (o is Updatable)
                    o.update(game)
            }
        }
    }

    fun draw(game: Game) {
        for (o in vector2D)
            if (o is Drawable)
                o.draw(game)
    }
}
