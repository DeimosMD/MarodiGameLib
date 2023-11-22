package control

import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class GraphicsPanel (
    private val game: Game
) : JPanel() {

    private val runtimeSettings get() = game.runtimeSettings
    private val elementHandler get() = game.elementHandler
    private val spriteHandler get() = game.spriteHandler
    private val screen get() = game.screen
    private val camera get() = game.camera
    private val world get() = game.currentWorld

    private var graphics2D = null as Graphics2D?

    init {
        this.isDoubleBuffered = true
        this.isFocusable = true
        this.background = runtimeSettings.backgroundColor
    }


    fun init() {
        screen.add(this)
        screen.isVisible = true
    }


    override fun paint(g: Graphics) {
        super.paint(g)
        graphics2D = g as Graphics2D
        camera.graphics2D = graphics2D
        world.draw(game)
        graphics2D!!.dispose()
    }
}