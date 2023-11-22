package control

class SecTimer(
    private val tracker: PerformanceTracker
) : Runnable {

    var thread: Thread = Thread(this)

    override fun run() {
        tracker.rate++
        try {
            Thread.sleep(1000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        tracker.rate--
        if (tracker.rate == 0) {
            println("Warning: ${tracker.label} Frozen")
        }
    }
}