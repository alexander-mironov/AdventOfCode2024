import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    //day12part1()
    day12part2()
}

val offsets = listOf(0 to 1, 0 to -1, 1 to 0, -1 to 0)

fun visit(
    map: StringField,
    row: Int,
    column: Int,
    visited: Array<Array<Boolean>>
): Int {
    val type = map.get(row, column)
    var regionArea = 0
    var regionPerimeter = 0
    fun internalVisit(row: Int, column: Int) {
        if (visited[row][column]) {
            return
        }
        visited[row][column] = true
        regionArea += 1
        for (offset in offsets) {
            val newRow = row + offset.first
            val newColumn = column + offset.second
            if (map.get(newRow, newColumn) == type) {
                internalVisit(newRow, newColumn)
            } else {
                regionPerimeter += 1
            }
        }
    }
    internalVisit(row, column)
    return regionArea * regionPerimeter
}

fun day12part1() {
    // calculate area and perimeter in one go
    val input = File("input/day12_sample.txt").readLines()

    val visited = Array(input.size) { Array(input[0].length) { false } }
    val field = StringField(input)

    var price = 0
    for (row in input.indices) {
        for (column in input[0].indices) {
            if (!visited[row][column]) {
                price += visit(field, row, column, visited)
            }
        }
    }
    println(price)
}

fun day12part2() {
    // calculate area and perimeter in one go
    val input = File("input/day12.txt").readLines()

    val visited = Array(input.size) { Array(input[0].length) { false } }
    val field = StringField(input)

    var price = 0
    for (row in input.indices) {
        for (column in input[0].indices) {
            if (!visited[row][column]) {
                price += visit2(field, row, column, visited)
            }
        }
    }
    println(price)
}

class StringField(private val field: List<String>) {
    fun get(row: Int, column: Int): Char? {
        return if (row >= 0 && row <= field.lastIndex && column >= 0 && column <= field[0].lastIndex) {
            field[row][column]
        } else {
            null
        }
    }
}

fun visit2(
    map: StringField,
    row: Int,
    column: Int,
    visited: Array<Array<Boolean>>
): Int {
    val type = map.get(row, column)
    var regionArea = 0
    var minRow = Int.MAX_VALUE
    var maxRow = Int.MIN_VALUE
    var minCol = Int.MAX_VALUE
    var maxCol = Int.MIN_VALUE

    val area = mutableSetOf<Cell>()

    fun internalVisit(row: Int, column: Int) {
        if (visited[row][column]) {
            return
        }
        area.add(Cell(row, column))
        minRow = min(minRow, row)
        maxRow = max(maxRow, row)
        minCol = min(minCol, column)
        maxCol = max(maxCol, column)
        visited[row][column] = true
        regionArea += 1
        for (offset in offsets) {
            val newRow = row + offset.first
            val newColumn = column + offset.second
            if (map.get(newRow, newColumn) == type) {
                internalVisit(row + offset.first, column + offset.second)
            } else {
                continue
            }
        }
    }
    internalVisit(row, column)

    var side = 0
    for (row in minRow..maxRow) {
        var topSide = false
        var bottomSide = false
        for (column in minCol..maxCol) {
            if (area.contains(Cell(row, column)) && map.get(row - 1, column) != type) {
                if (!topSide) {
                    topSide = true
                    side += 1
                }
            } else {
                topSide = false
            }

            if (area.contains(Cell(row, column)) && map.get(row + 1, column) != type) {
                if (!bottomSide) {
                    bottomSide = true
                    side += 1
                }
            } else {
                bottomSide = false
            }
        }
    }

    for (column in minCol..maxCol) {
        var leftSide = false
        var rightSide = false
        for (row in minRow..maxRow) {
            if (area.contains(Cell(row, column)) && map.get(row, column - 1) != type) {
                if (!leftSide) {
                    leftSide = true
                    side += 1
                }
            } else {
                leftSide = false
            }

            if (area.contains(Cell(row, column)) && map.get(row, column + 1) != type) {
                if (!rightSide) {
                    rightSide = true
                    side += 1
                }
            } else {
                rightSide = false
            }
        }
    }
    println("$regionArea * $side")
    return regionArea * side
}