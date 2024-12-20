import java.io.File

fun main() {
    day08part1()
    day08part2()
}

fun day08part1() {
    val input = File("input/day08.txt").readLines()
    val stations = mutableMapOf<Char, MutableList<Cell>>()

    input.forEachIndexed { rowIndex, line ->
        line.forEachIndexed { columnIndex, value ->
            if (value != '.') {
                stations.computeIfAbsent(value) {
                    mutableListOf()
                }.add(Cell(rowIndex, columnIndex))
            }
        }
    }

    val antinodes = mutableSetOf<Cell>()
    for (keySpecificStations in stations.values) {
        for (i in keySpecificStations.indices) {
            for (j in (i + 1)..<keySpecificStations.size) {
                val station1 = keySpecificStations[i]
                val station2 = keySpecificStations[j]
                val rowDiff = station1.row - station2.row
                val colDiff = station1.column - station2.column
                val firstCell = Cell(station1.row + rowDiff, station1.column + colDiff)
                val secondCell = Cell(station2.row - rowDiff, station2.column - colDiff)
                antinodes.add(firstCell)
                antinodes.add(secondCell)
            }
        }
    }

    val insideField = antinodes.filter {
        it.row >= 0 && it.row <= input.lastIndex
                && it.column >= 0 && it.column <= input[0].lastIndex
    }

    println(insideField.size)
}

fun day08part2() {
    val input = File("input/day08.txt").readLines()
    val stations = mutableMapOf<Char, MutableList<Cell>>()

    input.forEachIndexed { rowIndex, line ->
        line.forEachIndexed { columnIndex, value ->
            if (value != '.') {
                stations.computeIfAbsent(value) {
                    mutableListOf()
                }.add(Cell(rowIndex, columnIndex))
            }
        }
    }

    val antinodes = mutableSetOf<Cell>()
    for (keySpecificStations in stations.values) {
        for (i in keySpecificStations.indices) {
            for (j in (i + 1)..<keySpecificStations.size) {
                val station1 = keySpecificStations[i]
                val station2 = keySpecificStations[j]
                val rowDiff = station1.row - station2.row
                val colDiff = station1.column - station2.column
                antinodes.add(station1)
                antinodes.add(station2)
                var prevCell = station1
                while (true) {
                    val cell = Cell(prevCell.row + rowDiff, prevCell.column + colDiff)
                    val insideField = cell.row >= 0 && cell.row <= input.lastIndex
                            && cell.column >= 0 && cell.column <= input[0].lastIndex
                    if (!insideField) {
                        break
                    } else {
                        antinodes.add(cell)
                        prevCell = cell
                    }
                }
                prevCell = station1
                while (true) {
                    val cell = Cell(prevCell.row - rowDiff, prevCell.column - colDiff)
                    val insideField = cell.row >= 0 && cell.row <= input.lastIndex
                            && cell.column >= 0 && cell.column <= input[0].lastIndex
                    if (!insideField) {
                        break
                    } else {
                        antinodes.add(cell)
                        prevCell = cell
                    }
                }
            }
        }
    }

    println(antinodes.size)
}