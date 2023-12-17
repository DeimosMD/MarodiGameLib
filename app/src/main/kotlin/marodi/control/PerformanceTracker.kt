package marodi.control

class PerformanceTracker (
    private val _label: String?,
    private val prints: Boolean
) {
    val label get() = _label
    var rate = 0

    fun increaseNum() {
        SecTimer(this).thread.start()
        if (prints && label != null) println("$label: $rate")
    }
}