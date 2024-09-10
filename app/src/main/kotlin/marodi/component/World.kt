package marodi.component

import marodi.control.Game

public open class World : Vector2D() {

    fun update(game: Game) {
        for (o in this.clone() as World) {
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
        val spritesToDraw = ArrayList<Sprite>()
        for (o in this)
            if (o is Sprite) {
                if (o.isVisible)
                    spritesToDraw.add(o)
            }
        val orderedSpritesToDraw = ArrayList<Sprite>() // sprites in order from the highest depth to the lowest depth
        while (spritesToDraw.isNotEmpty()) {
            var highestDepthSprite = spritesToDraw[0]
            for (sprite in spritesToDraw)
                if (sprite.depth > highestDepthSprite.depth)
                    highestDepthSprite = sprite
            orderedSpritesToDraw.add(highestDepthSprite)
            spritesToDraw.remove(highestDepthSprite)
        }
        for (sprite in orderedSpritesToDraw) {
            sprite.draw(game)
            for (h in sprite.hitbox)
                h.draw(game, sprite)
        }
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
