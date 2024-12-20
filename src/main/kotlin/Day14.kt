import java.awt.Color
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.BufferedImage.TYPE_INT_RGB
import java.io.File
import javax.imageio.ImageIO


fun main() {
    day14part1()
    day14part2()
}

fun day14part1() {
    val iterations = 100
    val width = 101
    val height = 103

    var topLeftQuadrant = 0L
    var topRightQuadrant = 0L
    var bottomLeftQuadrant = 0L
    var bottomRightQuadrant = 0L
    val midWidth = width / 2
    val midHeight = height / 2

    val lines = File("input/day14.txt").readLines()
    lines.forEach { line ->
        val (posX, posY, velX, velY) = line.substring(2).split(" v=", ",").map { it.toInt() }
        val finalPosX = ((posX + iterations * velX).rem(width) + width).rem(width)
        val finalPosY = ((posY + iterations * velY).rem(height) + height).rem(height)
        println("$finalPosX $finalPosY")
        if (finalPosX < midWidth && finalPosY < midHeight) {
            topLeftQuadrant += 1
        } else if (finalPosX > midWidth && finalPosY < midHeight) {
            topRightQuadrant += 1
        } else if (finalPosX < midWidth && finalPosY > midHeight) {
            bottomLeftQuadrant += 1
        } else if (finalPosX > midWidth && finalPosY > midHeight) {
            bottomRightQuadrant += 1
        }
    }
    println(topLeftQuadrant * topRightQuadrant * bottomLeftQuadrant * bottomRightQuadrant)
}

data class Robot(val posX: Int, val posY: Int, val velX: Int, val velY: Int)

fun day14part2() {
    val iterations = 10_000
    val width = 101
    val height = 103
    val lines = File("input/day14.txt").readLines()


    val robots = lines.map { line ->
        val (posX, posY, velX, velY) = line.substring(2).split(" v=", ",").map { it.toInt() }
        Robot(posX, posY, velX, velY)
    }


    repeat(iterations) { iter ->
        val image = BufferedImage(width, height, TYPE_INT_RGB)
        robots.forEach { (posX, posY, velX, velY) ->
            val finalPosX = ((posX + iter * velX).rem(width) + width).rem(width)
            val finalPosY = ((posY + iter * velY).rem(height) + height).rem(height)
            image.setRGB(finalPosX, finalPosY, Color.WHITE.rgb)
        }
        ImageIO.write(image, "png", File("output/day14/$iter.png").also { it.mkdirs() })
    }
}
