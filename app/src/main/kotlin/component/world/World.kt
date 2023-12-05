package component.world

import component.sprite.Drawable
import component.sprite.Sprite
import component.sprite.Updatable
import control.Game
import physics.PlatformerPhysics
import java.lang.IllegalArgumentException

public open class World : Vector2D() {

    fun update(game: Game) {
        for (o in this) {
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
        for (o in this)
            if (o is Drawable)
                o.draw(game)
    }

    public open fun getGravity(game: Game): Float {
        if (game.physics is PlatformerPhysics)
            return (game.physics as PlatformerPhysics).gravity
        throw IllegalArgumentException("Requires PlatformerPhysics")
    }
}
