import java.io.File

fun main() {
    day19part1()
    day19part2()
}

fun day19part1() {
    val input = File("input/day19.txt").readLines()
    val patterns = input[0].split(", ")

    fun possible(input: String): Boolean {
        if (input == "") {
            return true
        }
        return patterns.any { pattern ->
            if (input.startsWith(pattern)) {
                possible(input.substring(pattern.length))
            } else {
                false
            }
        }
    }

    var possibleVariants = 0
    for (i in 2..input.lastIndex) {
        if (possible(input[i])) {
            possibleVariants += 1
        }
    }

    println(possibleVariants)
}

fun day19part2() {
    val input = File("input/day19.txt").readLines()
    val patterns = input[0].split(", ")

    val cache = mutableMapOf("" to 1L)

    fun possible(input: String): Long {
        if (cache.contains(input)) {
            return cache[input]!!
        }
        val sumOf = patterns.sumOf { pattern ->
            if (input.startsWith(pattern)) {
                possible(input.substring(pattern.length))
            } else {
                0
            }
        }
        cache[input] = sumOf
        return sumOf
    }

    var possibleVariants = 0L
    for (i in 2..input.lastIndex) {
        possibleVariants += possible(input[i])
    }

    println(possibleVariants)
}