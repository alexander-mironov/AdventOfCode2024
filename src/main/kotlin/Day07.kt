import java.io.File
import java.math.BigInteger


fun main() {
    day07part1()
    day07part2()
}

private fun day07part1() {
    day07("input/day07.txt", ::checkVariantsTwoOperators)
}

private fun day07part2() {
    day07("input/day07.txt", ::checkVariantsThreeOperators)
}

private fun day07(inputFile: String, function: (required: Long, current: Long, right: List<Long>) -> Boolean) {
    val input = File(inputFile).readLines()
    val sum = input.sumOf { line ->
        val parts = line.split(": ", " ").map { it.toLong() }
        val required = parts[0]
        val right = parts.drop(1)
        if (function(required, 0L, right)) {
            required
        } else 0L
    }
    println(sum)
}

fun checkVariantsTwoOperators(required: Long, current: Long, right: List<Long>): Boolean {
    if (current > required) {
        return false
    }
    if (right.isEmpty()) {
        return required == current
    }
    val firstFromRight = right.first()
    val newRight = right.drop(1)
    return checkVariantsTwoOperators(required, current + firstFromRight, newRight)
            || checkVariantsTwoOperators(required, current * firstFromRight, newRight)
}

fun checkVariantsThreeOperators(required: Long, current: Long, right: List<Long>): Boolean {
    if (current > required) {
        return false
    }
    if (right.isEmpty()) {
        return required == current
    }
    val firstFromRight = right.first()
    val newRight = right.drop(1)
    val concatenationResult = try {
        BigInteger(current.toString() + firstFromRight.toString()).longValueExact()
    } catch (e: ArithmeticException) {
        Long.MAX_VALUE
    }

    return checkVariantsThreeOperators(required, current + firstFromRight, newRight)
            || checkVariantsThreeOperators(required, current * firstFromRight, newRight)
            || checkVariantsThreeOperators(required, concatenationResult, newRight)
}