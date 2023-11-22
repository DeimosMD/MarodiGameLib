package control

class GameHandler (
    private val game: Game
) : Runnable  {

    private val graphicsPanel get() =  game.graphicsPanel
    private val keyHandler get() = game.keyHandler
    private val loader get() = game.loader
    private val runtimeSettings get() = game.runtimeSettings
    private val elementHandler get() = game.elementHandler
    private val spriteHandler get() = game.spriteHandler
    private val screen get() = game.screen
    private val world get() = game.currentWorld
    private val physics get() = game.physics

    val thread = Thread(this)
    private lateinit var trackFPS: PerformanceTracker
    private var frameNum = 0

    fun init() {
        trackFPS = PerformanceTracker("Tracked FPS", runtimeSettings.isPrintFramesPerSecond)
    }

    override fun run() {
        trackFPS.increaseNum()
        var startTimeNS = System.nanoTime() // System time in nanoseconds
        var sleepTimeMS: Long // Time to sleep
        while (screen != null) {
            sleepTimeMS =
                        (((1.0 / (runtimeSettings.fps * 2 - trackFPS.rate)
                        * 1_000_000_000).toLong())
                        - (System.nanoTime() - startTimeNS)) / 1_000_000
            if (sleepTimeMS > 0) {
                try {
                    Thread.sleep(sleepTimeMS) // Sleeps the remaining time in milliseconds
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            trackFPS.increaseNum()
            frameNum++
            game.frameTime = (System.nanoTime() - startTimeNS).toDouble() / 1_000_000_000
            startTimeNS = System.nanoTime()
            keyHandler.update()
            loader.update()
            physics.update(game)
            world.update(game)
            graphicsPanel.repaint()
            System.gc()
            System.runFinalization()
        }
    }
}