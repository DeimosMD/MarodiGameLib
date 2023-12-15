package marodi.resource

import marodi.control.Game
import java.awt.event.KeyEvent
import java.awt.image.BufferedImage
import java.io.IOException
import javax.imageio.ImageIO

class Loader (
    private val game: Game
) {

    private val keyHandler get() = game.keyHandler

    private val imageList = HashMap<String, BufferedImage?>()
    private val debugImagePath = "debug-textures/default-sprite.png"

    init {
        loadImage(debugImagePath)
    }

    public fun update() {
        if (keyHandler.downPressed(KeyEvent.VK_F1))
            reloadRes()
    }


    public fun loadImage(path: String) {
        try {
            imageList[path] = ImageIO.read(Loader::class.java.classLoader.getResource(path))
        } catch (_: IOException) {}
        if (imageList[path] == null) {
            println("WARNING: Image '$path' failed to load")
        } else {
            println("Image '$path' loaded")
        }
    }


    public fun getImage(path: String): BufferedImage? {
        return if (imageList.containsKey(path) && imageList[path] != null) {
            imageList[path]
        } else {
            imageList[debugImagePath]
        }
    }


    public fun unlistImage(path: String) {
        imageList.remove(path)
    }


    public fun reloadRes() {
        println("Reloading Resources...")
        for (path in imageList.keys) {
            loadImage(path)
        }
        println("Resources Reloaded")
    }
}