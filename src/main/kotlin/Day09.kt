import java.io.File
import java.util.LinkedList
import kotlin.math.min

fun main() {
    day09part1()
    day09part2()
}

fun day09part1() {
    val input = File("input/day09.txt").readText().map { it.digitToInt() }

    var left = 0
    var right = input.lastIndex
    var sum = 0L
    var blocksToMove = 0
    var emptySpaces = 0

    var index = 0

    while (left < right) {
        if (emptySpaces == 0) {
            val fileLen = input[left]
            repeat(fileLen) {
                sum += index * (left / 2)
                index += 1
            }
            emptySpaces = input[left + 1]
        }
        if (blocksToMove == 0) {
            blocksToMove = input[right]
        }

        val min = min(blocksToMove, emptySpaces)
        repeat(min) {
            sum += index * (right / 2)
            index += 1
        }
        blocksToMove -= min
        emptySpaces -= min

        if (blocksToMove == 0) {
            right -= 2
        }
        if (emptySpaces == 0) {
            left += 2
        }
    }
    repeat(blocksToMove) {
        sum += index * (right / 2)
        index += 1
    }
    println(sum)
}

data class Space(val fileNumberId: Int, val size: Int)



fun day09part2() {
    val input = File("input/day09.txt").readText().map { it.digitToInt() }
    val filesystem = MutableList(input.sum()) { -1 }
    var isFile = true
    var innerIndex = 0
    for ((outerIndex, value) in input.withIndex()) {
        if (isFile) {
            repeat(value) {
                filesystem[innerIndex++] = outerIndex / 2
            }
        } else {
            innerIndex += value
        }
        isFile = !isFile
    }

    var rightFileNumberId = filesystem.last()
    var rightEndIndex = filesystem.lastIndex
    while (rightFileNumberId > 0) {
        var rightStartIndex = rightEndIndex
        while (filesystem[rightStartIndex - 1] == rightFileNumberId) {
            rightStartIndex -= 1
        }
        val rightFileSize = rightEndIndex - rightStartIndex + 1

        var startOfEmptySpace = -1
        var endOfEmptySpace = -1
        for (i in filesystem.indices) {
            if (filesystem[i] == -1) {
                if (startOfEmptySpace == -1) {
                    startOfEmptySpace = i
                    endOfEmptySpace = i
                } else {
                    endOfEmptySpace = i
                }
            } else {
                startOfEmptySpace = -1
            }

            if (startOfEmptySpace != -1) {
                val emptySpaceSize = endOfEmptySpace - startOfEmptySpace + 1
                if (emptySpaceSize >= rightFileSize && startOfEmptySpace < rightStartIndex) {
                    for (j in startOfEmptySpace..endOfEmptySpace) {
                        filesystem[j] = rightFileNumberId
                    }
                    for (j in rightStartIndex..rightEndIndex) {
                        filesystem[j] = -1
                    }
                    break
                }
            }
        }
        rightFileNumberId -= 1
        rightEndIndex = filesystem.lastIndexOf(rightFileNumberId)
    }

    var sum = 0L
    filesystem.forEachIndexed { index, value ->
        if (value != -1) {
            sum += index * value
        }
    }
    println(sum)
}