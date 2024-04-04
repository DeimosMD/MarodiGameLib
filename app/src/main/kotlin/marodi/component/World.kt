package marodi.component

import marodi.control.Game

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
            if (o is Sprite) {
                if (o.isVisible)
                    o.draw(game)
            } else if (o is Drawable)
                o.draw(game)
    }

    public open fun getGravity(game: Game): Float {
        return game.physics.gravity
    }

    public open fun getBaseHorizontalResistance(game: Game): Float {
        return game.physics.baseHorizontalResistance
    }

    public open fun getBaseVerticalResistance(game: Game): Float {
        return game.physics.baseVerticalResistance
    }

    public open fun getBaseFrictionalResistance(game: Game): Float {
        return game.physics.baseFrictionalResistance
    }
}
