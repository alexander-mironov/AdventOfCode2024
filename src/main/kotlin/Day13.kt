import java.io.File

fun main() {
    day13part1()
    day13part2()
}

fun day13part1() {
    solution(::parsePart1)
}

fun day13part2() {
    solution(::parsePart2)
}

private fun solution(parsePrize: (String) -> List<Long>) {
    val lines = File("input/day13.txt").readLines()
    var sum = 0L
    for (case in 0..lines.size / 4) {
        val (aX, aY) = lines[case * 4].substring(12).split(", Y+").map { it.toLong() }
        val (bX, bY) = lines[case * 4 + 1].substring(12).split(", Y+").map { it.toLong() }
        val (prizeX, prizeY) = parsePrize(lines[case * 4 + 2])

        println("case: $case")

        val bNumerator = prizeY * aX - prizeX * aY
        val bDenominator = bY * aX - bX * aY
        if (bNumerator % bDenominator == 0L) {
            val b = bNumerator / bDenominator

            val aNumerator = prizeX - (b * bX)
            val aDenominator = aX
            if (aNumerator % aDenominator == 0L) {
                val a = aNumerator / aDenominator
                sum += (a * 3) + b
            }
        }
    }
    println(sum)
}

private fun parsePart1(
    input: String
) = input.substring(9).split(", Y=").map { it.toLong() }

private fun parsePart2(
    input: String
) = input.substring(9).split(", Y=").map { it.toLong() + 10000000000000L }