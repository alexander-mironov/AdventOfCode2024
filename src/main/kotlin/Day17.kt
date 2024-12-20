import java.io.File
import kotlin.math.pow

fun main() {
    //day17part1()
    day17part2()
}

object Consts {
    const val ADV = 0L
    const val BXL = 1L
    const val BST = 2L
    const val JNZ = 3L
    const val BXC = 4L
    const val OUT = 5L
    const val BDV = 6L
    const val CDV = 7L
}

fun day17part1() {
    val input = File("input/day17.txt").readLines()
    val inputA = input[0].substring(12).toLong()
    val inputB = input[1].substring(12).toLong()
    val inputC = input[2].substring(12).toLong()
    val instructions = input[4].substring(9).split(',').map { it.toLong() }

    val out = invoke(inputA, inputB, inputC, instructions)
    println(out.joinToString(","))
}

fun day17part2() {
    val input = File("input/day17.txt").readLines()
    val inputA = input[0].substring(12).toInt()
    val inputB = input[1].substring(12).toInt()
    val inputC = input[2].substring(12).toInt()
    val instructions = input[4].substring(9).split(',').map { it.toLong() }

    val actual = listOf(2L, 4, 1, 2, 7, 5, 4, 7, 1, 3, 5, 5, 0, 3, 3, 0)

    fun findSmallest(currentList: List<Int>, position: Int) {
        if (position == actual.size) {
            println(currentList.joinToString("").toLong(radix = 8))
            return
        }

        for (i in 0..7) {
            val copy = currentList.toMutableList()
            copy[position] = i
            val a = copy.joinToString("").toLong(radix = 8)
            val result = invoke(inputA = a, inputB = 0, inputC = 0, instructions)
            if (result.size == actual.size) {
                val good = (actual.size - position - 1 until actual.size).all {
                    result[it] == actual[it]
                }
                if (good) {
                    findSmallest(copy, position + 1)
                }
            }
        }
    }

    val initial = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    findSmallest(initial, 0)

//    var a = 0L
//    while (true) {
//        if (a % 1000 == 0) {
//            println("Checking a = $a")
//        }
//        val (match, _) = invoke(a, inputB, inputC, instructions, requiredOutput = instructions)
//        if (match) {
//            println("Result: $a")
//            break
//        }
//        a += 1
//    }
}

enum class ComparisonResult {
    FOLLOWS, NO_MATCH, FULL_MATCH
}

private fun invoke(
    inputA: Long,
    inputB: Long,
    inputC: Long,
    instructions: List<Long>
): List<Long> {
    var a = inputA
    var b = inputB
    var c = inputC

    val out = mutableListOf<Long>()

    fun comboOperand(value: Long): Long {
        return when (value) {
            in 0..3 -> value
            4L -> a
            5L -> b
            6L -> c
            else -> throw IllegalStateException()
        }
    }

    var pointer = 0
    while (pointer < instructions.size) {
        val operation = instructions[pointer]
        val operand = instructions[pointer + 1]
        when (operation) {
            Consts.ADV -> {
                a /= 2.0.pow(comboOperand(operand).toInt()).toLong()
                pointer += 2
            }

            Consts.BXL -> {
                b = b xor operand
                pointer += 2
            }

            Consts.BST -> {
                b = comboOperand(operand) % 8
                pointer += 2
            }

            Consts.JNZ -> {
                if (a == 0L) {
                    pointer += 2
                } else {
                    pointer = operand.toInt()
                }
            }

            Consts.BXC -> {
                b = b xor c
                pointer += 2
            }

            Consts.OUT -> {
                out.add(comboOperand(operand) % 8)
                pointer += 2
            }

            Consts.BDV -> {
                b = a / 2.0.pow(comboOperand(operand).toInt()).toLong()
                pointer += 2
            }

            Consts.CDV -> {
                c = a / 2.0.pow(comboOperand(operand).toInt()).toLong()
                pointer += 2
            }
        }
    }
    return out
}