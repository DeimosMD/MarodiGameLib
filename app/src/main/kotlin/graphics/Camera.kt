package graphics

import component.world.Positional
import control.Game
import java.awt.Color
import java.awt.image.BufferedImage

public class Camera (
    private val game: Game
) : Positional() {

    private val runtimeSettings get() = game.runtimeSettings
    private val graphicsPanel get() = game.graphicsPanel

    private fun toScreenX(inX: Double): Int {
        return (inX + (runtimeSettings.screenWidth / 2 - x)).toInt()
    }

    private fun toScreenY(inY: Double): Int {
        return (-inY + (runtimeSettings.screenHeight / 2 + y)).toInt()
    }

    public fun drawImage(img: BufferedImage?, positional: Positional) {
        val dx = toScreenX(positional.x)
        val dy = toScreenY(positional.y) - img?.getHeight(null)!!
        graphicsPanel.graphics2D?.drawImage(img, dx, dy, null)
    }

    public fun drawRect(w: Double, h: Double, color: Color, x: Double, y: Double) {
        graphicsPanel.graphics2D?.color = color
        val ih = h.toInt()
        val dx = toScreenX(x)
        val dy = toScreenY(y) - ih
        graphicsPanel.graphics2D?.fillRect(dx, dy, w.toInt(), ih)
        graphicsPanel.graphics2D?.color = null
    }

    public fun drawRect(w: Double, h: Double, color: Color, positional: Positional) {
        drawRect(w, h, color, positional.x, positional.y)
    }
}