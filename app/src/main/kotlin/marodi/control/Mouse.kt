package marodi.control

import java.awt.event.MouseEvent
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

class Mouse(
    private val game: Game
) : MouseListener, MouseMotionListener {

    private val graphicsPanel get() = game.graphicsPanel

    public var x: Int = 0
        private set
    public var y: Int = 0
        private set
    public var onScreen = false
        private set

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

    override fun mouseMoved(e: MouseEvent?) {
        if (e != null) {
            x = e.x
            y = e.y
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