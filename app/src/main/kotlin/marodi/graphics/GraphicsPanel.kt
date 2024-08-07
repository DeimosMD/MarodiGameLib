package marodi.graphics

import marodi.control.Game
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Toolkit
import java.awt.geom.AffineTransform
import javax.swing.JPanel

class GraphicsPanel (
    private val game: Game
) : JPanel() {

    private val runtimeSettings get() = game.runtimeSettings
    private val screen get() = game.screen
    private val camera get() = game.camera
    private val world get() = game.currentWorld

    internal var graphics2D = null as Graphics2D?
    internal var transformSave = null as AffineTransform?

    fun init() {
        isDoubleBuffered = true
        background = runtimeSettings.backgroundColor
        isFocusable = true
        requestFocusInWindow()
        screen?.add(this)
    }


    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        graphics2D = g as Graphics2D
        transformSave = g.transform
        if (!game.backgroundDrawList.isEmpty())
            for (drawable in game.backgroundDrawList)
                drawable.draw(game)
        world.draw(game)
        if (!game.foregroundDrawList.isEmpty())
            for (drawable in game.foregroundDrawList)
                drawable.draw(game)
        Toolkit.getDefaultToolkit().sync()
        graphics2D!!.dispose()
    }
}