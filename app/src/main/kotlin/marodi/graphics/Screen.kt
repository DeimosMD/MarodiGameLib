package marodi.graphics

import marodi.control.RuntimeSettings
import javax.swing.JFrame
import javax.swing.WindowConstants.EXIT_ON_CLOSE

class Screen internal constructor(
    private val runtimeSettings: RuntimeSettings
) : JFrame() {

    fun init() {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(runtimeSettings.screenWidth, runtimeSettings.screenHeight)
        isResizable = runtimeSettings.isResizable
        title = runtimeSettings.windowTitle
        isVisible = true
    }
}
