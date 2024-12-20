import java.io.File

fun main() {
    //day15part1()
    day15part2()
}

data class CellAndType(val row: Int, val column: Int, var type: Char)

fun day15part2() {
    // left and right are not affected (just have to move all elements, and not just a couple)
    // top and bottom - add row where for [left, right] everything is empty

    val field = mutableListOf<MutableList<Char>>()
    var isField = true
    var start: Cell? = null

    var instructions = ""

    File("input/day15.txt").readLines().forEachIndexed { row, line ->
        if (line.isEmpty()) {
            isField = false
        } else if (isField) {
            val column = line.indexOf('@')
            if (column != -1) {
                start = Cell(row, column * 2)
            }
            val map = line.map { char ->
                when (char) {
                    'O' -> "[]"
                    '@' -> "@."
                    else -> "$char$char"
                }
            }.flatMap { it.asIterable() }.toMutableList()
            field.add(map)
        } else {
            instructions += line
        }
    }

    fun moveVertically(row: Int, column: Int, rowOffset: Int): Boolean {
        val cratesToMove = mutableSetOf<CellAndType>()

        fun canMoveInner(row: Int, column: Int, rowOffset: Int): Boolean {
            if (field[row + rowOffset][column] == '.') {
                return true
            } else if (field[row + rowOffset][column] == '[') {
                cratesToMove.add(CellAndType(row + rowOffset, column, '['))
                cratesToMove.add(CellAndType(row + rowOffset, column + 1, ']'))
                return canMoveInner(row + rowOffset, column, rowOffset)
                        && canMoveInner(row + rowOffset, column + 1, rowOffset)
            } else if (field[row + rowOffset][column] == ']') {
                cratesToMove.add(CellAndType(row + rowOffset, column - 1, '['))
                cratesToMove.add(CellAndType(row + rowOffset, column, ']'))
                return canMoveInner(row + rowOffset, column, rowOffset)
                        && canMoveInner(row + rowOffset, column - 1, rowOffset)
            }
            return false
        }

        val canMove = canMoveInner(row, column, rowOffset)
        if (canMove) {
            for (crate in cratesToMove) {
                field[crate.row][crate.column] = '.'
            }
            for (crate in cratesToMove) {
                field[crate.row + rowOffset][crate.column] = crate.type
            }
            field[row][column] = '.'
            field[row + rowOffset][column] = '@'
            val size = cratesToMove.size
            if (size > 1) {
                println("--- CHECK ---")
            }
            println("--- Moved: $size crates!")
        } else {
            println("--- Can't move! ---")
        }

        return canMove
    }

    val offsets = mapOf(
        '<' to -1,
        '>' to 1
    )

    fun printField() {
        for (row in field.indices) {
            for (column in field[0].indices) {
                print(field[row][column])
            }
            println()
        }
    }

    var curRow = start!!.row
    var curCol = start!!.column
    printField()
    for (instruction in instructions) {
        println("Instruction: $instruction")

        if (instruction == '^') {
            val moved = moveVertically(curRow, curCol, rowOffset = -1)
            if (moved) {
                curRow -= 1
            }
        } else if (instruction == 'v') {
            val moved = moveVertically(curRow, curCol, rowOffset = 1)
            if (moved) {
                curRow += 1
            }
        } else {
            val offset = offsets[instruction]!!
            var firstEmptyOffset = -1
            var i = 0
            while (true) {
                i += 1
                if (field[curRow][curCol + i * offset] == '.') {
                    firstEmptyOffset = i
                    break
                } else if (field[curRow][curCol + i * offset] == '#') {
                    break
                }
            }
            if (firstEmptyOffset != -1) {
                field[curRow].removeAt(curCol + firstEmptyOffset * offset)
                field[curRow].add(curCol, '.')
                curCol += offset
            }
        }

        //printField()
    }

    var sum = 0
    for (row in field.indices) {
        for (column in field[0].indices) {
            if (field[row][column] == '[') {
                sum += row * 100 + column
            }
        }
    }
    println(sum)
}

data class Offset(val row: Int, val column: Int)

fun day15part1() {
    val field = mutableListOf<MutableList<Char>>()
    var isField = true
    var start: Cell? = null

    var instructions = ""

    File("input/day15.txt").readLines().forEachIndexed { row, line ->
        if (line.isEmpty()) {
            isField = false
        } else if (isField) {
            val column = line.indexOf('@')
            if (column != -1) {
                start = Cell(row, column)
            }
            field.add(line.toMutableList())
        } else {
            instructions += line
        }
    }

    val offsets = mapOf(
        '<' to Offset(0, -1),
        '>' to Offset(0, 1),
        '^' to Offset(-1, 0),
        'v' to Offset(1, 0)
    )

    var curRow = start!!.row
    var curCol = start!!.column
    for (instruction in instructions) {
        val offset = offsets[instruction]!!
        var firstEmptyOffset = -1
        for (i in 1..50) {
            if (field[curRow + i * offset.row][curCol + i * offset.column] == '.') {
                firstEmptyOffset = i
                break
            } else if (field[curRow + i * offset.row][curCol + i * offset.column] == '#') {
                break
            }
        }
        if (firstEmptyOffset != -1) {
            field[curRow + firstEmptyOffset * offset.row][curCol + firstEmptyOffset * offset.column] = 'O'
            field[curRow + offset.row][curCol + offset.column] = '@'
            field[curRow][curCol] = '.'
            curRow += offset.row
            curCol += offset.column
        }
    }

    var sum = 0
    for (row in field.indices) {
        for (column in field[0].indices) {
            if (field[row][column] == 'O') {
                sum += row * 100 + column
            }
        }
    }

    println(sum)
}