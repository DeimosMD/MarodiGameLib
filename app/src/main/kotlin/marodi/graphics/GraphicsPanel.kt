package marodi.graphics

import marodi.control.Game
import java.awt.Graphics
import java.awt.Graphics2D
import javax.swing.JPanel

class GraphicsPanel (
    private val game: Game
) : JPanel() {

    private val runtimeSettings get() = game.runtimeSettings
    private val screen get() = game.screen
    private val camera get() = game.camera
    private val world get() = game.currentWorld

    internal var graphics2D = null as Graphics2D?

    fun init() {
        isDoubleBuffered = true
        isFocusable = true
        background = runtimeSettings.backgroundColor
        screen.add(this)
    }


    override fun paint(g: Graphics) {
        super.paint(g)
        graphics2D = g as Graphics2D
        world.draw(game)
        if (!game.drawList.isEmpty())
            for (drawable in game.drawList)
                drawable.draw(game)
        graphics2D!!.dispose()
    }
}