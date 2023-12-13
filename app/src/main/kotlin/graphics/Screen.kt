package graphics

import control.RuntimeSettings
import javax.swing.JFrame

class Screen internal constructor(
    private val runtimeSettings: RuntimeSettings
) : JFrame(runtimeSettings.windowTitle) {

    fun init() {
        this.defaultCloseOperation = EXIT_ON_CLOSE
        this.setSize(runtimeSettings.screenWidth, runtimeSettings.screenHeight)
        this.isResizable = runtimeSettings.isResizable
        this.isVisible = true
    }
}
