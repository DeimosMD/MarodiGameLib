package control

import component.world.World
import graphics.Camera
import graphics.GraphicsPanel
import graphics.Screen
import physics.Physics
import physics.RoamerPhysics
import resource.Loader

public open class Game {
    internal lateinit var graphicsPanel: GraphicsPanel
        private set
    internal lateinit var gameHandler: GameHandler
    public var runtimeSettings: RuntimeSettings = RuntimeSettings()
    public lateinit var keyHandler: KeyHandler
    public lateinit var camera: Camera
    public var screen: Screen? = null
        private set
    public lateinit var loader: Loader
    public var currentWorld = World()
    public var frameTime = 0.0
        internal set
    public var physics: Physics = RoamerPhysics()
        private set

    public constructor() {
        indirectConstruction()
    }

    public constructor(physics: Physics) : this() {
        this.physics = physics
    }

    private fun indirectConstruction() {
        graphicsPanel = GraphicsPanel(this)
        gameHandler = GameHandler(this)
        runtimeSettings = RuntimeSettings()
        keyHandler = KeyHandler(this)
        camera = Camera(this)
        screen = Screen(runtimeSettings)
        loader = Loader(this)
    }

    public fun launch() {
        graphicsPanel.init()
        gameHandler.init()
        keyHandler.init()
        gameHandler.thread.start()
    }

    public fun close() {
        screen = null
    }
}
