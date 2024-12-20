import java.io.File

class PackedList(val internalMap: MutableMap<Long, Long> = mutableMapOf()) {

    fun add(item: Long, count: Long) {
        internalMap[item] = (internalMap[item] ?: 0) + count
    }

}

fun main() {
    val input = File("input/day11.txt").readText().split(" ").map { it.toLong() }

    var items = PackedList()
    input.forEach { items.add(it, 1) }
    repeat(75) {
        println("blink: ${it + 1}")
        val newItems = PackedList()
        for ((item, number) in items.internalMap) {
            if (item == 0L) {
                newItems.add(1L, number)
            } else if (item.toString().length % 2 == 0) {
                val toString = item.toString()
                val left = toString.substring(0, toString.length / 2).toLong(10)
                val right = toString.substring(toString.length / 2).toLong(10)
                newItems.add(left, number)
                newItems.add(right, number)
            } else {
                newItems.add(2024L * item, number)
            }
        }
        items = newItems
    }
    println(items.internalMap.values.sum())
}