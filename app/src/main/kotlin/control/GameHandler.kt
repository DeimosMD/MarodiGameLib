package control

class GameHandler (
    private val game: Game
) : Runnable  {

    private val graphicsPanel get() =  game.graphicsPanel
    private val keyHandler get() = game.keyHandler
    private val loader get() = game.loader
    private val runtimeSettings get() = game.runtimeSettings
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
        while (game.running) {
            sleepTimeMS =
                        (((1.0 / (runtimeSettings.FPS * 2 - trackFPS.rate)
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
            game.frameTime = (System.nanoTime() - startTimeNS).toFloat() / 1_000_000_000F
            startTimeNS = System.nanoTime()
            keyHandler.update()
            loader.update()
            world.update(game)
            if (!game.updateList.isEmpty())
                for (updatable in game.updateList)
                    updatable.update(game)
            physics.update(game)
            graphicsPanel.repaint()
            System.gc()
            System.runFinalization()
        }
    }
}