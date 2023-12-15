package marodi.control

import java.awt.Color

class RuntimeSettings {

    // SCREEN
    var windowTitle = "Game Window"
    var screenWidth = 900
    var screenHeight = 900
    var backgroundColor: Color? = Color.BLACK
    var isResizable = false

    // DRAWING
    var drawFromCenter = false
    var FPS = 60

    // DEBUG
    var isPrintFramesPerSecond = false
}