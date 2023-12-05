package component.world

import java.util.*

abstract class Vector2D : Vector<Positional>() {

    public fun getAtPos(pos: Positional): Vector<Positional> {
        val list = Vector<Positional>()
        for (o in this)
            if (pos.x == o.x && pos.y == o.y) list.add(o)
        return list
    }

    public fun getInArea(pos1: Positional, pos2: Positional): Vector<Positional> {
        var x1 = pos1.x
        var y1 = pos1.y
        var x2 = pos2.x
        var y2 = pos2.y

        // Swaps values if in wrong order
        if (x1 > x2) {
            x1 += x2
            x2 = x1 - x2
            x1 -= x2
        }
        if (y1 > y2) {
            y1 += y2
            y2 = y1 - y2
            y1 -= y2
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