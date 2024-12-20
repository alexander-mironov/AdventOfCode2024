import java.io.File
import java.util.*

fun main() {
    //day18part1()
    day18part2()
}


data class Day18Record(val price: Int, val row: Int, val column: Int)

fun day18part1() {

    val start = Cell(0, 0)
    val end = Cell(70, 70)
    val field = Array(71) { BooleanArray(71) }
    val readLines = File("input/day18.txt").readLines()
    readLines.subList(0, 1024).forEach { line ->
        val (x, y) = line.split(',').map { it.toInt() }
        field[x][y] = true
    }

    val front = PriorityQueue(1) { a: Day18Record, b: Day18Record -> a.price - b.price }
    front.add(Day18Record(0, start.row, start.column))
    val bestPrice = mutableMapOf(start to 0)

    while (front.isNotEmpty()) {
        val record = front.remove()
        for (direction in Direction.entries) {
            val newRow = record.row + direction.offsetRow
            val newColumn = record.column + direction.offsetColumn
            if (newRow < 0 || newRow > field.lastIndex || newColumn < 0 || newColumn > field[0].lastIndex) {
                continue
            }
            if (field[newRow][newColumn]) {
                continue
            }
            val newPrice = record.price + 1
            val cell = Cell(newRow, newColumn)
            if (newPrice < (bestPrice[cell] ?: Int.MAX_VALUE)) {
                bestPrice[cell] = newPrice
                front.add(Day18Record(newPrice, newRow, newColumn))
            }
        }
    }

    val steps = bestPrice[end]

    println(steps)
}

fun day18part2() {

    val start = Cell(0, 0)
    val end = Cell(70, 70)
    val input = File("input/day18.txt").readLines()

    var left = 0
    var right = input.lastIndex

    while (left < right) {
        val mid = (left + right) / 2
        val endPrice = traverse(input.subList(0, mid), start, end)
        if (endPrice != null) {
            left = mid + 1
        } else {
            right = mid - 1
        }
    }

    println(traverse(input.subList(0, left), start, end))
    println(traverse(input.subList(0, right), start, end))
    println(traverse(input.subList(0, right + 1), start, end))

    println(input[right])
    println(input[right + 1])
    println(input[right + 2])
}

private fun traverse(
    input: List<String>,
    start: Cell,
    end: Cell
): Int? {
    val field = Array(71) { BooleanArray(71) }
    input.forEach { line ->
        val (x, y) = line.split(',').map { it.toInt() }
        field[x][y] = true
    }

    val front = PriorityQueue(1) { a: Day18Record, b: Day18Record -> a.price - b.price }
    front.add(Day18Record(0, start.row, start.column))
    val bestPrice = mutableMapOf(start to 0)

    while (front.isNotEmpty()) {
        val record = front.remove()
        for (direction in Direction.entries) {
            val newRow = record.row + direction.offsetRow
            val newColumn = record.column + direction.offsetColumn
            if (newRow < 0 || newRow > field.lastIndex || newColumn < 0 || newColumn > field[0].lastIndex) {
                continue
            }
            if (field[newRow][newColumn]) {
                continue
            }
            val newPrice = record.price + 1
            val cell = Cell(newRow, newColumn)
            if (newPrice < (bestPrice[cell] ?: Int.MAX_VALUE)) {
                bestPrice[cell] = newPrice
                front.add(Day18Record(newPrice, newRow, newColumn))
            }
        }
    }

    val endPrice = bestPrice[end]
    return endPrice
}
