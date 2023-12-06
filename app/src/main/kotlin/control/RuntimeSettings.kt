package control

import java.awt.Color

class RuntimeSettings {

    // SCREEN
    var windowTitle = "Game Window"
    var screenWidth = 900
    var screenHeight = 900
    var FPS = 60
    var backgroundColor: Color? = Color.BLACK
    var isResizable = false
    var drawFromCenter = false

    // DEBUG
    var isPrintFramesPerSecond = false
}