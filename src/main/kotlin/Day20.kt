import java.io.File
import java.util.*
import kotlin.math.abs

fun main() {
    //day20part1()
    day20part2()
}

data class Day20Record(val distance: Int, val row: Int, val column: Int)

fun day20part1() {
    val field = mutableListOf<String>()

    var start = Cell(-1, -1)
    var end = Cell(-1, -1)
    File("input/day20.txt").readLines().forEachIndexed { row, line ->
        val startColumn = line.indexOf('S')
        if (startColumn != -1) {
            start = Cell(row, startColumn)
        }
        val endColumn = line.indexOf('E')
        if (endColumn != -1) {
            end = Cell(row, endColumn)
        }
        field.add(line)
    }

    val front = PriorityQueue(1) { a: Day20Record, b: Day20Record -> a.distance - b.distance }
    front.add(Day20Record(0, end.row, end.column))
    val distanceFromEnd = Array(field.size) { IntArray(field[0].length) { Int.MAX_VALUE } }
    distanceFromEnd[end.row][end.column] = 0

    while (front.isNotEmpty()) {
        val record = front.remove()
        for (direction in Direction.entries) {
            val newRow = record.row + direction.offsetRow
            val newColumn = record.column + direction.offsetColumn
            if (newRow < 0 || newRow > field.lastIndex || newColumn < 0 || newColumn > field[0].lastIndex) {
                continue
            }
            if (field[newRow][newColumn] == '#') {
                continue
            }
            val distance = record.distance + 1
            if (distance < distanceFromEnd[newRow][newColumn]) {
                distanceFromEnd[newRow][newColumn] = distance
                front.add(Day20Record(distance, newRow, newColumn))
            }
        }
    }

    val currentRouteLength = distanceFromEnd[start.row][start.column]


    fun traceAndCheat(): MutableMap<Int, Int> {
        val saves = mutableMapOf<Int, Int>()
        var row = start.row
        var column = start.column

        while (row != end.row || column != end.column) {

            val currentDistanceFromEnd = distanceFromEnd[row][column]
            for (direction in Direction.entries) {
                if (field[row + direction.offsetRow][column + direction.offsetColumn] == '#') {
                    val cheatRow = row + 2 * direction.offsetRow
                    val cheatColumn = column + 2 * direction.offsetColumn
                    if (cheatRow < 0 || cheatRow > field.lastIndex || cheatColumn < 0 || cheatColumn > field[0].lastIndex) {
                        continue
                    }
                    val currentDistanceFromStart = currentRouteLength - currentDistanceFromEnd
                    val restDistanceFromCheat = distanceFromEnd[cheatRow][cheatColumn]
                    if (restDistanceFromCheat == Int.MAX_VALUE) {
                        continue
                    }
                    val newRoute = currentDistanceFromStart + restDistanceFromCheat + 2
                    if (newRoute < currentRouteLength) {
                        val save = currentRouteLength - newRoute
                        saves[save] = (saves[save] ?: 0) + 1
                    }
                }
            }
            for (direction in Direction.entries) {
                val distance = distanceFromEnd[row + direction.offsetRow][column + direction.offsetColumn]
                if (distance == currentDistanceFromEnd - 1) {
                    //traceAndCheat(row + direction.offsetRow, column + direction.offsetColumn)
                    row += direction.offsetRow
                    column += direction.offsetColumn
                    break
                }
            }
        }
        return saves
    }

    val saves = traceAndCheat()


    println(saves.toSortedMap())
    println(saves.filterKeys { it >= 100 }.values.sum())

    //println(distanceFromEnd[start])
}

fun day20part2() {
    val field = mutableListOf<String>()

    var start = Cell(-1, -1)
    var end = Cell(-1, -1)
    File("input/day20.txt").readLines().forEachIndexed { row, line ->
        val startColumn = line.indexOf('S')
        if (startColumn != -1) {
            start = Cell(row, startColumn)
        }
        val endColumn = line.indexOf('E')
        if (endColumn != -1) {
            end = Cell(row, endColumn)
        }
        field.add(line)
    }

    val front = PriorityQueue(1) { a: Day20Record, b: Day20Record -> a.distance - b.distance }
    front.add(Day20Record(0, end.row, end.column))
    val distanceFromEnd = Array(field.size) { IntArray(field[0].length) { Int.MAX_VALUE } }
    distanceFromEnd[end.row][end.column] = 0

    while (front.isNotEmpty()) {
        val record = front.remove()
        for (direction in Direction.entries) {
            val newRow = record.row + direction.offsetRow
            val newColumn = record.column + direction.offsetColumn
            if (newRow < 0 || newRow > field.lastIndex || newColumn < 0 || newColumn > field[0].lastIndex) {
                continue
            }
            if (field[newRow][newColumn] == '#') {
                continue
            }
            val distance = record.distance + 1
            if (distance < distanceFromEnd[newRow][newColumn]) {
                distanceFromEnd[newRow][newColumn] = distance
                front.add(Day20Record(distance, newRow, newColumn))
            }
        }
    }

    val currentRouteLength = distanceFromEnd[start.row][start.column]


    fun traceAndCheat(): MutableMap<Int, Int> {
        val saves = mutableMapOf<Int, Int>()
        var row = start.row
        var column = start.column

        while (row != end.row || column != end.column) {
            val currentDistanceFromEnd = distanceFromEnd[row][column]

            for (rowOffset in -20..20) {
                for (columnOffset in -20..20) {
                    val cheatLength = abs(rowOffset) + abs(columnOffset)
                    if (cheatLength > 20) {
                        continue
                    }
                    val cheatRow = row + rowOffset
                    val cheatColumn = column + columnOffset
                    if (cheatRow < 0 || cheatRow > field.lastIndex || cheatColumn < 0 || cheatColumn > field[0].lastIndex) {
                        continue
                    }
                    val restDistanceFromCheat = distanceFromEnd[cheatRow][cheatColumn]
                    if (restDistanceFromCheat == Int.MAX_VALUE) {
                        continue
                    }
                    val currentDistanceFromStart = currentRouteLength - currentDistanceFromEnd
                    val newRoute = currentDistanceFromStart + restDistanceFromCheat + cheatLength
                    if (newRoute < currentRouteLength) {
                        val save = currentRouteLength - newRoute
                        saves[save] = (saves[save] ?: 0) + 1
                    }
                }
            }

//            for (direction in Direction.entries) {
//                if (field[row + direction.offsetRow][column + direction.offsetColumn] == '#') {
//                    val cheatRow = row + 2 * direction.offsetRow
//                    val cheatColumn = column + 2 * direction.offsetColumn
//                    if (cheatRow < 0 || cheatRow > field.lastIndex || cheatColumn < 0 || cheatColumn > field[0].lastIndex) {
//                        continue
//                    }
//
//                    val restDistanceFromCheat = distanceFromEnd[cheatRow][cheatColumn]
//                    if (restDistanceFromCheat == Int.MAX_VALUE) {
//                        continue
//                    }
//                    val currentDistanceFromStart = currentRouteLength - currentDistanceFromEnd
//                    val newRoute = currentDistanceFromStart + restDistanceFromCheat + 2
//                    if (newRoute < currentRouteLength) {
//                        val save = currentRouteLength - newRoute
//                        saves[save] = (saves[save] ?: 0) + 1
//                    }
//                }
//            }

            for (direction in Direction.entries) {
                val distance = distanceFromEnd[row + direction.offsetRow][column + direction.offsetColumn]
                if (distance == currentDistanceFromEnd - 1) {
                    //traceAndCheat(row + direction.offsetRow, column + direction.offsetColumn)
                    row += direction.offsetRow
                    column += direction.offsetColumn
                    break
                }
            }
        }
        return saves
    }

    val saves = traceAndCheat()


    println(saves.toSortedMap())
    println(saves.filterKeys { it >= 100 }.values.sum())

    //println(distanceFromEnd[start])
}
