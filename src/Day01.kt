fun main() {
    fun countLarger(list: List<Int>): Int {
        return list.windowed(2, 1) {
            val prevValue = it.getOrElse(0) { 0 }
            val currentValue = it.getOrElse(1) { 0 }
            if (currentValue > prevValue) {
                1
            } else {
                0
            }
        }.sum()
    }

    fun part1(input: List<String>): Int {
        return return countLarger(input.map { it.toInt() })
    }

    fun part2(input: List<String>): Int {
        val summedWindow = input.windowed(3, 1) {
            it.sumOf { sub -> sub.toInt() }
        }

        return countLarger(summedWindow)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
