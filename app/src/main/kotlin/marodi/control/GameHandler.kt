package marodi.control

class GameHandler (
    private val game: Game
) : Runnable  {

    private val graphicsPanel get() =  game.graphicsPanel
    private val keyHandler get() = game.keyHandler
    private val mouse get() = game.mouse
    private val loader get() = game.loader
    private val runtimeSettings get() = game.runtimeSettings
    private val world get() = game.currentWorld
    private val physics get() = game.physics

    val thread = Thread(this)
    lateinit var trackFPS: PerformanceTracker
    var latestSleepTimeMS = 1L

    fun init() {
        trackFPS = PerformanceTracker("Tracked FPS", runtimeSettings.isPrintFramesPerSecond)
    }

    override fun run() {

        var startTimeNS = System.nanoTime() // System time in nanoseconds at frame start
        var sleepTimeMS: Long // Time to sleep
        var lastUpdateTimeNS = System.nanoTime() // System time in nanoseconds at most recent update call
        trackFPS.increaseNum()

        fun update() {
            keyHandler.update()
            mouse.update()
            loader.update()
            world.update(game)
            if (!game.updateList.isEmpty())
                for (updatable in game.updateList)
                    updatable.update(game)
            physics.update(game)
        }

        fun sleep() {
            sleepTimeMS =
                (((1.0 / (runtimeSettings.FPS * 2 - trackFPS.rate)
                        * 1_000_000_000).toLong())
                        - (System.nanoTime() - startTimeNS)) / 1_000_000
            if (sleepTimeMS > 0) {
                try {
                    latestSleepTimeMS = sleepTimeMS
                    Thread.sleep(sleepTimeMS) // Sleeps the remaining time in milliseconds
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            startTimeNS = System.nanoTime()
        }

        while (game.running) {
            sleep()
            if (trackFPS.rate <= runtimeSettings.FPS) {
                trackFPS.increaseNum()
                game.frameNum++
                game.frameProportion = (System.nanoTime() - lastUpdateTimeNS).toFloat() / 1_000_000_000F
                if (game.frameProportion > 1) {
                    game.frameProportion = 1f
                }
                lastUpdateTimeNS = System.nanoTime()
                update()
                graphicsPanel.repaint()
                if (game.frameNum % runtimeSettings.garbageCollectorFrequency == 0) {
                    System.gc()
                    System.runFinalization()
                }
            }
        }
    }
}
