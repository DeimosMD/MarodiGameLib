package component.sprite

import control.Game

public class ElementHandler (
    private val game: Game
){

    private var updatableScripts: ArrayList<Updatable>? = ArrayList()
    private var drawableScripts: ArrayList<Drawable>? = ArrayList()

    // Called every tick after the first tick
    public fun update() {
        if (updatableScripts != null) for (script in updatableScripts!!) {
            script.update(game)
        }
    }

    // Called once every frame
    public fun draw() {
        if (drawableScripts != null) for (script in drawableScripts!!) {
            script.draw(game)
        }
    }

    public fun addUpdateScript(u: Updatable) {
        updatableScripts!!.add(u)
    }

    public fun addDrawScript(d: Drawable) {
        drawableScripts!!.add(d)
    }

    public fun removeUpdateScript(u: Updatable) {
        updatableScripts!!.remove(u)
    }

    public fun removeDrawScript(d: Drawable) {
        drawableScripts!!.remove(d)
    }
}