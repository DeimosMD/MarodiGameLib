package marodi.control

import marodi.component.Drawable
import marodi.component.Sprite
import marodi.component.Updatable
import marodi.component.World
import marodi.graphics.Camera
import marodi.graphics.GraphicsPanel
import marodi.graphics.Screen
import marodi.physics.PhysicalPositional
import marodi.physics.Physics
import marodi.resource.Loader
import java.util.*
import javax.swing.JFrame

public abstract class Game {

    public lateinit var graphicsPanel: GraphicsPanel
        private set
    internal lateinit var gameHandler: GameHandler
    public var runtimeSettings = RuntimeSettings()
    public lateinit var keyHandler: KeyHandler
    public lateinit var mouse: Mouse
    public lateinit var camera: Camera
    internal var screen: Screen?
        private set
    public lateinit var loader: Loader
    public var currentWorld: World
        get() = camera.world
        set(value) {camera.world = value}
    public var frameProportion: Float = 0.0F
        internal set
    public var physics: Physics = Physics()
        private set
    public val statDraw get() = graphicsPanel.graphics2D!!
    public var running = true
        internal set
    public val activePhysicalPositionals: Vector<PhysicalPositional>
        get() {
            val v = Vector<PhysicalPositional>()
            for (p in currentWorld) if (p is PhysicalPositional) v.add(p)
            return v
        }
    public val updateList = Vector<Updatable>()
    public val drawList = Vector<Drawable>()
    public var frameNum = 0
        internal set
    public val FPS get() = gameHandler.trackFPS.rate

    public constructor() {
        screen = Screen(runtimeSettings)
        indirectConstruction()
    }

    public constructor(world: World) : this() {
        currentWorld = world
    }

    private fun indirectConstruction() {
        graphicsPanel = GraphicsPanel(this)
        gameHandler = GameHandler(this)
        keyHandler = KeyHandler(this)
        mouse = Mouse(this)
        camera = Camera(this)
        loader = Loader(this)
        currentWorld = World()
    }

    public open fun launch() {
        graphicsPanel.init()
        keyHandler.init()
        mouse.init()
        screen?.init()
        gameHandler.init()
        gameHandler.thread.start()
    }

    public open fun close() {
        running = false
    }

    public fun addSprite(s: Sprite) {
        currentWorld.add(s)
    }

    // used by engine
    public fun runInPanel(): GraphicsPanel {
        screen = null
        return graphicsPanel
    }

    // used by engine
    public fun runInNewWindow() {
        screen!!.defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
    }

    // used by engine
    public fun waitUntilClosed() {
        while (running) {
            Thread.sleep(gameHandler.latestSleepTimeMS)
            if (gameHandler.trackFPS.frozen)
                close()
        }
    }

    public fun queueRunnable(r: MarodiRunnable) {
        gameHandler.runnableQueue.add(r)
    }

    public fun delayRunnableNano(r: MarodiRunnable, nano: Long) {
        gameHandler.scheduledRunnableMap[r] = System.nanoTime()+nano
    }

    public fun delayRunnableMS(r: MarodiRunnable, MS: Double) {
        gameHandler.scheduledRunnableMap[r] = System.nanoTime()+(MS*1e6).toLong()
    }

    public fun delayRunnableSec(r: MarodiRunnable, Sec: Double) {
        gameHandler.scheduledRunnableMap[r] = System.nanoTime()+(Sec*1e9).toLong()
    }
}
