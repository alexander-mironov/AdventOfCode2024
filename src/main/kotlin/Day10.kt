import java.io.File

fun main() {
    day10part2()
}

fun day10part2() {
    val input = File("input/day10.txt").readLines().map { it.asIterable().map { it.digitToInt() } }
    val offsets = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)
    fun calculateAllRoutes(requiredHeight: Int, row: Int, column: Int): Int {
        return if (row < 0 || row > input.lastIndex || column < 0 || column > input[0].lastIndex) {
            0
        } else if (input[row][column] != requiredHeight) {
            0
        } else if (requiredHeight == 9) {
            1
        } else {
            offsets.sumOf { calculateAllRoutes(requiredHeight + 1, row + it.first, column + it.second) }
        }
    }

    val starts = mutableListOf<Cell>()
    for (row in input.indices) {
        for (column in input[0].indices) {
            if (input[row][column] == 0)
                starts.add(Cell(row, column))
        }
    }

    val routes = starts.sumOf { calculateAllRoutes(0, it.row, it.column) }

    println(routes)
}