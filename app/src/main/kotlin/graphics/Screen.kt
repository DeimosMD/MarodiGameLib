package graphics

import control.RuntimeSettings
import javax.swing.JFrame

class Screen internal constructor(
    private val runtimeSettings: RuntimeSettings
) : JFrame(runtimeSettings.windowTitle) {

    fun init() {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(runtimeSettings.screenWidth, runtimeSettings.screenHeight)
        isResizable = runtimeSettings.isResizable
        isVisible = true
    }
}
