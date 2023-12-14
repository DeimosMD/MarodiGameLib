package control

import component.sprite.Sprite
import component.world.World
import graphics.Camera
import graphics.GraphicsPanel
import graphics.Screen
import physics.PhysicalPositional
import physics.Physics
import physics.RoamerPhysics
import resource.Loader
import java.util.*

public open class Game {
    internal lateinit var graphicsPanel: GraphicsPanel
        private set
    internal lateinit var gameHandler: GameHandler
    public var runtimeSettings = RuntimeSettings()
    public lateinit var keyHandler: KeyHandler
    public lateinit var camera: Camera
    public var screen = Screen(runtimeSettings)
        private set
    public lateinit var loader: Loader
    public var currentWorld: World
        get() = camera.world
        set(value) {camera.world = value}
    public var frameTime: Float = 0.0F
        internal set
    public var physics: Physics = RoamerPhysics()
        private set
    public val statDraw get() = graphicsPanel.graphics2D
    public var running = true
        private set
    public val activePhysicalPositionals: Vector<PhysicalPositional> get() {
        val v = Vector<PhysicalPositional>()
        for (p in currentWorld) if (p is PhysicalPositional) v.add(p)
        return v
    }

    public constructor() {
        indirectConstruction()
    }

    public constructor(physics: Physics) : this() {
        this.physics = physics
    }

    public constructor(world: World) : this() {
        currentWorld = world
    }

    public constructor(physics: Physics, world: World) : this() {
        this.physics = physics
        currentWorld = world
    }

    private fun indirectConstruction() {
        graphicsPanel = GraphicsPanel(this)
        gameHandler = GameHandler(this)
        runtimeSettings = RuntimeSettings()
        keyHandler = KeyHandler(this)
        camera = Camera(this)
        loader = Loader(this)
        currentWorld = World()
    }

    public open fun launch() {
        graphicsPanel.init()
        keyHandler.init()
        screen.init()
        gameHandler.init()
        gameHandler.thread.start()
    }

    public open fun close() {
        running = false
    }

    public fun addSprite(s: Sprite) {
        currentWorld.add(s)
    }
}
