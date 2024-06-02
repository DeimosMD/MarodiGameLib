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

    private fun toScreenX(inX: Float): Int {
        return if (runtimeSettings.drawFromCenter)
                (inX + (graphicsPanel.width / 2 - x)).toInt()
        else
                (inX - x).toInt()
    }

    private fun toScreenY(inY: Float): Int {
        return if (runtimeSettings.drawFromCenter)
                (-inY + (graphicsPanel.height / 2 + y)).toInt()
        else
                (-inY + y + graphicsPanel.height).toInt()
    }

    // gives screen pos and size to determine whether it'd be visible on the screen
    private fun isOnScreen(x: Int, y: Int, w: Int, h: Int): Boolean {
        return x+w >= 0 && x <= graphicsPanel.width
                && y+h >= 0 && y <= graphicsPanel.height
    }

    public fun drawImage(img: BufferedImage?, x: Float, y: Float) {
        if (img != null) {
                val dx = toScreenX(x)
                val dy = toScreenY(y) - img.height
            if (isOnScreen(dx, dy, img.width, img.height))
                graphicsPanel.graphics2D!!.drawImage(img, dx, dy, null)
        }
    }

    public fun drawImage(img: BufferedImage?, positional: Positional) {
        drawImage(img, positional.x, positional.y)
    }

    public fun drawRect(w: Float, h: Float, color: Color, x: Float, y: Float) {
            val ih = h.toInt()
            val dx = toScreenX(x)
            val dy = toScreenY(y) - ih
        if (isOnScreen(dx, dy, w.toInt(), ih)) {
            graphicsPanel.graphics2D!!.color = color
            graphicsPanel.graphics2D!!.fillRect(dx, dy, w.toInt(), ih)
            graphicsPanel.graphics2D!!.color = null
        }
    }

    public fun drawRect(w: Float, h: Float, color: Color, positional: Positional) {
        drawRect(w, h, color, positional.x, positional.y)
    }
}