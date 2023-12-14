package control

import java.awt.event.KeyEvent
import java.awt.event.KeyEvent.*
import java.awt.event.KeyListener
import java.util.*

class KeyHandler (
    private val game: Game
) : KeyListener {

    private val screen get() = game.screen

    private var lastTickPressed = Vector<Int>() // Keys pressed at the tick before the most recent tick
    private var currentTickPressed = Vector<Int>() // Keys pressed at the most recent tick
    private var realPressed = Vector<Int>() // Keys being held down at the current time
    private var effectivePressed = Vector<Int>() // Keys being held down at current time and any pressed during the current frame

    fun init() {
        screen.addKeyListener(this)
        screen.isFocusable = true
        screen.requestFocusInWindow()
    }


    fun update() {
        lastTickPressed = Vector<Int>(currentTickPressed)
        currentTickPressed = Vector<Int>(effectivePressed)
        effectivePressed = Vector<Int>(realPressed)
    }


    public fun isPressed(keyCode: Int): Boolean {
        return containsKeyOrKeyOfSet(currentTickPressed, keyCode)
    }


    public fun downPressed(keyCode: Int): Boolean {
        return containsKeyOrKeyOfSet(currentTickPressed, keyCode) && !containsKeyOrKeyOfSet(lastTickPressed, keyCode)
    }


    public fun wasReleased(keyCode: Int): Boolean {
        return !containsKeyOrKeyOfSet(currentTickPressed, keyCode) && containsKeyOrKeyOfSet(lastTickPressed, keyCode)
    }

    override fun keyPressed(e: KeyEvent) {
        if (!effectivePressed.contains(e.keyCode))  {
            effectivePressed.add(e.keyCode)
            realPressed.add(e.keyCode)
        }
    }

    override fun keyReleased(e: KeyEvent) {
        realPressed.remove(e.keyCode)
    }

    override fun keyTyped(e: KeyEvent) {}

    private fun containsKeyOrKeyOfSet(v: Vector<Int>, k: Int): Boolean {
        when (k) {
            OF_DOWN -> return v.contains(VK_S) || v.contains(VK_DOWN)
            OF_UP -> return v.contains(VK_W) || v.contains(VK_UP)
            OF_LEFT -> return v.contains(VK_A) || v.contains(VK_LEFT)
            OF_RIGHT -> return v.contains(VK_D) || v.contains(VK_RIGHT)
        }
        return v.contains(k)
    }

    companion object {
        // key sets
        public const val OF_UP: Int = 1201
        public const val OF_DOWN: Int = 1202
        public const val OF_RIGHT: Int = 1203
        public const val OF_LEFT: Int = 1204
    }
}