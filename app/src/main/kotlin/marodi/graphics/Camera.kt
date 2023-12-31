package marodi.graphics

import marodi.component.Positional
import marodi.control.Game
import java.awt.Color
import java.awt.image.BufferedImage

public class Camera internal constructor (
    private val game: Game
) : Positional() {

    private val runtimeSettings get() = game.runtimeSettings
    private val graphicsPanel get() = game.graphicsPanel
    private val screen get() = game.screen

    private fun toScreenX(inX: Float): Int {
        return if (runtimeSettings.drawFromCenter)
                (inX + (screen.width / 2 - x)).toInt()
        else
                (inX - x).toInt()
    }

    private fun toScreenY(inY: Float): Int {
        return if (runtimeSettings.drawFromCenter)
                (-inY + (screen.height / 2 + y)).toInt()
        else
                (-inY + y + screen.height).toInt()
    }

    public fun drawImage(img: BufferedImage?, x: Float, y: Float) {
        val dx = toScreenX(x)
        val dy = toScreenY(y) - img?.getHeight(null)!!
        graphicsPanel.graphics2D?.drawImage(img, dx, dy, null)
    }

    public fun drawImage(img: BufferedImage?, positional: Positional) {
        drawImage(img, positional.x, positional.y)
    }

    public fun drawRect(w: Float, h: Float, color: Color, x: Float, y: Float) {
        graphicsPanel.graphics2D?.color = color
        val ih = h.toInt()
        val dx = toScreenX(x)
        val dy = toScreenY(y) - ih
        graphicsPanel.graphics2D?.fillRect(dx, dy, w.toInt(), ih)
        graphicsPanel.graphics2D?.color = null
    }

    public fun drawRect(w: Float, h: Float, color: Color, positional: Positional) {
        drawRect(w, h, color, positional.x, positional.y)
    }
}