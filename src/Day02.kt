interface Shipable {
    fun moveForward(count: Int)
    fun moveDown(count: Int)
    fun moveUp(count: Int)
}
open class Ship(): Shipable {
    protected var horizontalPos = 0
    protected var depth = 0

    override fun moveForward(count: Int) {
        horizontalPos += count
    }
    override fun moveDown(count: Int) {
        depth += count
    }
    override fun moveUp(count: Int) {
        depth -= count
    }
    fun getTotalPosition() = horizontalPos * depth
}

class AdvancedShip: Ship() {
    private var aim = 0

    override fun moveForward(count: Int) {
        horizontalPos += count
        depth += aim * count
    }

    override fun moveDown(count: Int) {
        aim += count
    }
    override fun moveUp(count: Int) {
        aim -= count
    }
}

fun main() {
    fun moveShip(input: List<String>, ship: Shipable) {
        // transform to command + count
        input.map {
            val (command, count) = it.split(" ")
            when (command) {
                "forward" -> ship.moveForward(count.toInt())
                "down" -> ship.moveDown(count.toInt())
                "up" -> ship.moveUp(count.toInt())
            }
        }
    }

    fun part1(input: List<String>): Int {
        val ship = Ship()
        moveShip(input, ship)

        return ship.getTotalPosition()
    }

    fun part2(input: List<String>): Int {
        val ship = AdvancedShip()
        moveShip(input, ship)

        return ship.getTotalPosition()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
