package marodi.control

import java.util.*
import kotlin.collections.HashMap

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
    internal var runnableQueue: LinkedList<MarodiRunnable> = LinkedList()
    internal var scheduledRunnableMap: HashMap<MarodiRunnable, Long> = HashMap() // the pair values are a scheduled time in nanoseconds, as in system nano-time

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
            for (updatable in game.updateList)
                updatable.update(game)
            physics.update(game)
            updateFromRunnableLists()
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

    private fun updateFromRunnableLists() {
        // runs through runnableQueue
        val queue = LinkedList(runnableQueue)
        runnableQueue = LinkedList()
        while (!queue.isEmpty()) {
            queue.remove().run()
        }
        // runs through scheduledRunnableMap
        val time = System.nanoTime()
        val ready = ArrayList<MarodiRunnable>()
        for (key in scheduledRunnableMap.keys) {
            if (scheduledRunnableMap[key]!! <= time) {
                ready.add(key)
            }
        }
        // sorts ready into order based on corresponding time value in scheduledRunnableMap
        var anyChanged = true
        while (anyChanged) {
            anyChanged = false
            for (i in 0 until ready.size - 1) {
                if (scheduledRunnableMap[ready[i]]!! > scheduledRunnableMap[ready[i + 1]]!!) {
                    val temp = ready[i]
                    ready[i] = ready[i+1]
                    ready[i+1] = temp
                    anyChanged = true
                }
            }
        }
        for (r in ready) {
            scheduledRunnableMap.remove(r)
            r.run()
        }
    }
}
