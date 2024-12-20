import Result
import java.io.File

fun main() {
    day06part1()
    day06part2()

}

data class BumpedIntoHash(val hash: Cell, val directionIndex: Int)

fun day06part2() {
    // use day06part1() results and try to put an obstacle in every visitedCell
    // find a way to check when stuck in a loop (e.g. seen the same # from the same direction
    val (field, initialPos, visitedCells) = day06part1()

    fun outside(cell: Cell): Boolean {
        return cell.row < 0 || cell.column < 0 || cell.row > field.lastIndex || cell.column > field[0].lastIndex
    }

    val allowedVisitedCells = visitedCells - initialPos


    val directions = listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)
    var loops = 0

    i@for (potentialHashCell in allowedVisitedCells) {
        val bumpedIntoHash = mutableSetOf<BumpedIntoHash>()
        var directionIndex = 0

        var currentPos = initialPos
        while (true) {
            val nextPos = currentPos.copy(
                row = currentPos.row + directions[directionIndex].first,
                column = currentPos.column + directions[directionIndex].second
            )
            if (outside(nextPos)) {
                break
            }
            if (field[nextPos.row][nextPos.column] == '#' || nextPos == potentialHashCell) {
                if (!bumpedIntoHash.add(BumpedIntoHash(nextPos, directionIndex))) {
                    loops += 1
                    continue@i
                }
                directionIndex = (directionIndex + 1) % 4
            } else {
                currentPos = nextPos
            }
        }
    }
    println(loops)
}

data class Cell(val row: Int, val column: Int)

fun day06part1(): Result {
    val directions = listOf(-1 to 0, 0 to 1, 1 to 0, 0 to -1)
    var directionIndex = 0

    val field = File("input/day06.txt").readLines()
    var initialPos = Cell(0, 0)

    fun outside(cell: Cell): Boolean {
        return cell.row < 0 || cell.column < 0 || cell.row > field.lastIndex || cell.column > field[0].lastIndex
    }

    i@ for (row in field.indices) {
        for (column in field[0].indices) {
            if (field[row][column] == '^') {
                initialPos = Cell(row, column)
                break@i
            }
        }
    }

    val visitedCells = mutableSetOf(initialPos)
    var currentPos = initialPos
    while (true) {
        val nextPos = currentPos.copy(
            row = currentPos.row + directions[directionIndex].first,
            column = currentPos.column + directions[directionIndex].second
        )
        if (outside(nextPos)) {
            break
        }
        if (field[nextPos.row][nextPos.column] == '#') {
            directionIndex = (directionIndex + 1) % 4
        } else {
            visitedCells.add(nextPos)
            currentPos = nextPos
        }
    }
    println(visitedCells.size)
    return Result(field, initialPos, visitedCells)
}

data class Result(val field: List<String>, val initialPos: Cell, val visitedCells: Set<Cell>)