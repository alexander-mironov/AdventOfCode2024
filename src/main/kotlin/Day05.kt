import java.io.File

fun main() {
    //day05part1()
    day05part2()
}

fun day05part1() {
    var rules = true
    val before = mutableMapOf<Int, MutableSet<Int>>()
    var sum = 0
    File("input/day05.txt").forEachLine { line ->
        when {
            line.isEmpty() -> {
                rules = false
            }

            rules -> {
                val (x, y) = line.split('|').map { it.toInt() }
                before.computeIfAbsent(x, { mutableSetOf() }).add(y)
            }

            else -> {
                val pages = line.split(',').map { it.toInt() }
                var validOverall = true
                for (i in pages.indices) {
                    val page = pages[i]
                    val valid = (i + 1..pages.lastIndex).all {
                        before[page]?.contains(pages[it]) ?: false
                    }
                    validOverall = validOverall && valid
                }
                if (validOverall) {
                    sum += pages[pages.size / 2]
                }
            }
        }
    }
    println(sum)
}

fun day05part2() {

    var rules = true
    val before = mutableMapOf<Int, MutableSet<Int>>()

    fun findLast(list: List<Int>): Int {
        var last = list.first()
        i@for (i in list) {
            for (j in list) {
                if (i == j) {
                    continue
                }
                if (before[i]?.contains(j) == true) {
                    continue@i
                }
            }
            last = i
        }
        return last
    }

    var sum = 0
    File("input/day05.txt").forEachLine { line ->
        when {
            line.isEmpty() -> {
                rules = false
            }

            rules -> {
                val (x, y) = line.split('|').map { it.toInt() }
                before.computeIfAbsent(x) { mutableSetOf() }.add(y)
            }

            else -> {
                val pages = line.split(',').map { it.toInt() }
                var validOverall = true
                for (i in pages.indices) {
                    val page = pages[i]
                    val valid = (i + 1..pages.lastIndex).all {
                        before[page]?.contains(pages[it]) ?: false
                    }
                    validOverall = validOverall && valid
                }
                if (!validOverall) {
                    val mutablePages = pages.toMutableList()
                    val iterations = pages.size / 2
                    var last = 0
                    for (i in 0 .. iterations) {
                        last = findLast(mutablePages)
                        mutablePages.remove(last)
                    }
                    sum += last
                }
            }
        }
    }
    println(sum)
}