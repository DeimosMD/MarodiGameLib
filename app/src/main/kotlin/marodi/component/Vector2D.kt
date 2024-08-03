package marodi.component

import java.util.*

abstract class Vector2D : Vector<Positional>() {

    public fun getAtPos(x: Float, y: Float): Vector<Positional> {
        val list = Vector<Positional>()
        for (o in this)
            if (x == o.x && y == o.y) list.add(o)
        return list
    }

    public fun getInArea(x1_: Float, x2_: Float, y1_: Float, y2_: Float): Vector<Positional> {
        var x1 = x1_
        var x2 = x2_
        var y1 = y1_
        var y2 = y2_

        // Swaps values if in wrong order
        if (x1 > x2) {
            x1 = x2_
            x2 = x1_
        }
        if (y1 > y2) {
            y1 = y2_
            y2 = y1_
        }

        val list = Vector<Positional>()
        for (o in this)
            if (o.x in x1..x2 && o.y in y1..y2) list.add(o)
        return list
    }

    public override fun toString(): String {
        var s = this::class.simpleName + ":"
        for (o in this) {
            s += " " + o::class.simpleName + o.toString()
        }
        return s
    }

    override fun add(element: Positional): Boolean {
        return if (element.world == null) {
            element.world = this as World
            super.add(element)
        } else false
    }

    override fun remove(element: Positional?): Boolean {
        return if (this.contains(element)) {
            if (element != null) {
                element.world = null
            }
            super.remove(element)
        } else false
    }
}