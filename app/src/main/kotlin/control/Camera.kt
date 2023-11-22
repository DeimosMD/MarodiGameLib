package control

import component.world.Positional
import java.awt.Graphics2D
import java.awt.image.BufferedImage

public class Camera (
    private val game: Game
) : Positional() {

    private val runtimeSettings get() = game.runtimeSettings

    public var graphics2D = null as Graphics2D?

    public fun drawImage(img: BufferedImage?, drawPos: Positional) {
        val dx = drawPos.x + (runtimeSettings.screenWidth / 2 - x)
        val dy = -drawPos.y + (runtimeSettings.screenHeight / 2 + y) - img?.getHeight(null)!!
        graphics2D?.drawImage(img, dx.toInt(), dy.toInt(), null)
    }
}