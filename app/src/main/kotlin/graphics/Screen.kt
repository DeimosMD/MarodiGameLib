package graphics

import control.RuntimeSettings
import javax.swing.JFrame

class Screen internal constructor(runtimeSettings: RuntimeSettings) : JFrame(runtimeSettings.windowTitle) {
    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        this.setSize(runtimeSettings.screenWidth, runtimeSettings.screenHeight)
        this.isResizable = isResizable
    }
}
