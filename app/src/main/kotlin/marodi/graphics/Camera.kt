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
                Math.round(inX + (graphicsPanel.width / 2 - x))
        else
                Math.round(inX - x)
    }

    private fun toScreenY(inY: Float): Int {
        return if (runtimeSettings.drawFromCenter)
                Math.round(-inY + (graphicsPanel.height / 2 + y))
        else
                Math.round(-inY + y + graphicsPanel.height)
    }

    public fun drawRect(w: Float, h: Float, color: Color, x: Float, y: Float) {
        val dx = toScreenX(x)
        val dy = toScreenY(y) - h.toInt()
        graphicsPanel.graphics2D!!.color = color
        graphicsPanel.graphics2D!!.fillRect(dx, dy, w.toInt(), h.toInt())
        graphicsPanel.graphics2D!!.color = null
    }

    public fun drawRect(w: Float, h: Float, color: Color, positional: Positional) {
        drawRect(w, h, color, positional.x, positional.y)
    }

    public fun drawImage(img: BufferedImage?, x: Float, y: Float, rotationDegrees: Double, cx: Float, cy: Float, scale: Double) {
        if (img != null) {
            val h = (img.height * scale).toInt()
            val w = (img.width * scale).toInt()
            val dx = toScreenX(x)
            val dy = toScreenY(y) - h
            val theta = Math.toRadians(rotationDegrees)
            val mx = cx*scale
            val my = cy*scale
            graphicsPanel.graphics2D!!.rotate(theta, dx+mx, dy+my)
            graphicsPanel.graphics2D!!.drawImage(img, dx, dy, w, h, null)
            graphicsPanel.graphics2D!!.transform = graphicsPanel.transformSave
        }
    }

    public fun drawImage(img: BufferedImage?, positional: Positional, rotationDegrees: Double, cx: Float, cy: Float, scale: Double) {
        drawImage(img, positional.x, positional.y, rotationDegrees, cx, cy, scale)
    }

    public fun drawImage(img: BufferedImage?, x: Float, y: Float) {
        drawImage(img, x, y, 0.0, 0f, 0f, 1.0)
    }

    public fun drawImage(img: BufferedImage?, positional: Positional) {
        drawImage(img, positional.x, positional.y)
    }
}