class CombinationsGenerator(private val size: Int, private val toGenerate: List<Int>) {

    private var lastCombination: MutableList<Int>? = null
    private val n = toGenerate.size

    fun next(): List<Int>? {
        if (lastCombination == null) {
            lastCombination = (0 until  size).toMutableList()
            return lastCombination!!.map { toGenerate[it] }
        }
        var t = size - 1
        while (t != 0 && lastCombination!![t] == n - size + t) {
            t--
        }
        lastCombination!![t]++
        for (i in t + 1 until size) {
            lastCombination!![i] = lastCombination!![i - 1] + 1
        }
        if (lastCombination!![size - 1] == n) {
            return null
        }
        return lastCombination!!.map { toGenerate[it] }
    }

}