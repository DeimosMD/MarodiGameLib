package control

import component.world.Positional
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

public class Camera (
    private val game: Game
) : Positional() {

    private val runtimeSettings get() = game.runtimeSettings

    public var graphics2D = null as Graphics2D?

    public fun drawImage(img: BufferedImage?, positional: Positional) {
        val dx = toScreenX(positional.x)
        val dy = toScreenY(positional.y) - img?.getHeight(null)!!
        graphics2D?.drawImage(img, dx, dy, null)
    }

    public fun drawRect(w: Double, h: Double, color: Color, positional: Positional) {
        graphics2D?.color = color
        val ih = h.toInt()
        val dx = toScreenX(positional.x)
        val dy = toScreenY(positional.y) - ih
        graphics2D?.fillRect(dx, dy, w.toInt(), ih)
        graphics2D?.color = null
    }

    private fun toScreenX(inX: Double): Int {
        return (inX + (runtimeSettings.screenWidth / 2 - x)).toInt()
    }

    private fun toScreenY(inY: Double): Int {
        return (-inY + (runtimeSettings.screenHeight / 2 + y)).toInt()
    }
}