package marodi.control

import marodi.physics.PhysicalPositional
import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

class Mouse(
    private val game: Game
) : MouseListener, MouseMotionListener {

    private val graphicsPanel get() = game.graphicsPanel
    private val runtimeSettings get() = game.runtimeSettings

    public var screenX: Int = 0
        private set
    public var screenY: Int = 0
        private set
    public var onScreen = false
        private set
    public val gameX: Int get() {
        return if (runtimeSettings.drawFromCenter)
            screenX-graphicsPanel.width/2
        else
            screenX
    }
    public val gameY: Int get() {
        return if (runtimeSettings.drawFromCenter)
            graphicsPanel.height/2-screenY
        else
            graphicsPanel.height-screenY
    }

    // same logic as in keyHandler
    // assumes a mouse can have up to eight buttons, just in case
    private var lastButtons = Array(9) {false}
    private var currentButtons = Array(9) {false}
    private var effectiveButtons = Array(9) {false} // realButtons with the addition of any buttons pressed during the frame (even if later unpressed)
    private var realButtons = Array(9) {false} // always set to the buttons being pressed at any moment

    fun init() {
        graphicsPanel.addMouseListener(this)
        graphicsPanel.addMouseMotionListener(this)
    }

    fun update() {
        lastButtons = currentButtons.clone()
        currentButtons = effectiveButtons.clone()
        effectiveButtons = realButtons.clone()
    }

    public fun isPressed(button: Int): Boolean {
        return currentButtons[button]
    }

    public fun isBeginPressed(button: Int): Boolean {
        return !lastButtons[button] && currentButtons[button]
    }

    public fun wasReleased(button: Int): Boolean {
        return lastButtons[button] && !currentButtons[button]
    }

    public fun isTouching(ph: PhysicalPositional): Boolean {
        for (h in ph.hitbox) {
            if (onScreen && h.getLeftSide(ph.x) <= gameX && gameX <= h.getRightSide(ph.x) && h.getBottomSide(ph.y) <= gameY && gameY <= h.getTopSide(ph.y))
                return true
        }
        return false
    }

    override fun mouseMoved(e: MouseEvent?) {
        if (e != null) {
            screenX = e.x
            screenY = e.y
        }
    }

    override fun mousePressed(e: MouseEvent?) {
        if (e != null) {
            realButtons[e.button] = true
            effectiveButtons[e.button] = true
        }
    }

    override fun mouseReleased(e: MouseEvent?) {
        if (e != null)
            realButtons[e.button] = false
    }

    override fun mouseEntered(e: MouseEvent?) {
        onScreen = true
    }

    override fun mouseExited(e: MouseEvent?) {
        onScreen = false
    }

    override fun mouseClicked(e: MouseEvent?) {}

    override fun mouseDragged(e: MouseEvent?) {}

    companion object {
        public const val LEFT_BUTTON = 1
        public const val MIDDLE_BUTTON = 2
        public const val RIGHT_BUTTON = 3
    }
}