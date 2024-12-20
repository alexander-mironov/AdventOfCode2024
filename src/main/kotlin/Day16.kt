import java.io.File
import java.util.*


fun main() {
    day16part1()
    day16part2()
}

enum class Direction(val offsetRow: Int, val offsetColumn: Int) {
    UP(offsetRow = -1, offsetColumn = 0),
    DOWN(offsetRow = 1, offsetColumn = 0),
    LEFT(offsetRow = 0, offsetColumn = -1),
    RIGHT(offsetRow = 0, offsetColumn = 1);

    val opposite: Direction
        get() = when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
}

data class Day16Record(val price: Int, val row: Int, val column: Int, val direction: Direction)

fun day16part1() {
    val field = mutableListOf<String>()

    var start = Cell(-1, -1)
    var end = Cell(-1, -1)
    File("input/day16.txt").readLines().forEachIndexed { row, line ->
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

    val front = PriorityQueue(1) { a: Day16Record, b: Day16Record -> a.price - b.price }
    front.add(Day16Record(0, start.row, start.column, Direction.RIGHT))
    val bestPriceByDirection = mapOf(
        Direction.RIGHT to mutableMapOf(start to 0),
        Direction.LEFT to mutableMapOf(),
        Direction.UP to mutableMapOf(),
        Direction.DOWN to mutableMapOf()
    )

    while (front.isNotEmpty()) {
        val record = front.remove()
        for (direction in Direction.entries) {
            val newRow = record.row + direction.offsetRow
            val newColumn = record.column + direction.offsetColumn
            if (field[newRow][newColumn] == '#' || record.direction.opposite == direction) {
                continue
            }
            val newPrice = if (direction == record.direction) {
                record.price + 1
            } else {
                record.price + 1001
            }
            val cell = Cell(newRow, newColumn)
            if (newPrice <= (bestPriceByDirection[direction]!![cell] ?: Int.MAX_VALUE)) {
                bestPriceByDirection[direction]!![cell] = newPrice
                front.add(Day16Record(newPrice, newRow, newColumn, direction))
            }
        }
    }

    val bestPrice = Direction.entries.minOf { bestPriceByDirection[it]!![end] ?: Int.MAX_VALUE }

    println(bestPrice)
}

fun day16part2() {
    val field = mutableListOf<String>()

    var start = Cell(-1, -1)
    var end = Cell(-1, -1)
    File("input/day16.txt").readLines().forEachIndexed { row, line ->
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

    val front = PriorityQueue(1) { a: Day16Record, b: Day16Record -> a.price - b.price }
    front.add(Day16Record(0, start.row, start.column, Direction.RIGHT))
    val bestPriceByDirection = mapOf(
        Direction.RIGHT to mutableMapOf(start to 0),
        Direction.LEFT to mutableMapOf(),
        Direction.UP to mutableMapOf(),
        Direction.DOWN to mutableMapOf()
    )

    while (front.isNotEmpty()) {
        val record = front.remove()
        for (direction in Direction.entries) {
            val newRow = record.row + direction.offsetRow
            val newColumn = record.column + direction.offsetColumn
            if (field[newRow][newColumn] == '#' || record.direction.opposite == direction) {
                continue
            }
            val newPrice = if (direction == record.direction) {
                record.price + 1
            } else {
                record.price + 1001
            }
            val cell = Cell(newRow, newColumn)
            if (newPrice < (bestPriceByDirection[direction]!![cell] ?: Int.MAX_VALUE)) {
                bestPriceByDirection[direction]!![cell] = newPrice
                front.add(Day16Record(newPrice, newRow, newColumn, direction))
            }
        }
    }

    val goodPlaces = mutableSetOf<Cell>()

    fun traceBack(price: Int, row: Int, column: Int) {
        val currentCell = Cell(row, column)
        val any = Direction.entries.any { innerDir ->
            val i =
                bestPriceByDirection[innerDir]!![Cell(row, column)] ?: Int.MAX_VALUE
            i == price
        }
        if (!any) {
            return
        }

        goodPlaces.add(currentCell)
        if (currentCell == start) {
            return
        }
        //val minPrice = Direction.entries.minOf { dir -> bestPriceByDirection[dir]!![currentCell] ?: Int.MAX_VALUE }
        val directionsToFollow = Direction.entries.filter { dir ->
            Direction.entries.any { innerDir ->
                val i =
                    bestPriceByDirection[innerDir]!![Cell(row - dir.offsetRow, column - dir.offsetColumn)] ?: Int.MAX_VALUE
                i == price - 1 || i == price - 1001
            }
        }
        directionsToFollow.forEach { dir ->
            val newRow = row - dir.offsetRow
            val newColumn = column - dir.offsetColumn
            traceBack(price - 1, newRow, newColumn)
            traceBack(price - 1001, newRow, newColumn)
        }
    }

    val bestPrice = Direction.entries.minOf { bestPriceByDirection[it]!![end] ?: Int.MAX_VALUE }

    traceBack(bestPrice, end.row, end.column)

    println(goodPlaces.size)
}
