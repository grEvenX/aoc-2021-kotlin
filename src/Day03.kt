import kotlin.math.roundToInt

fun main() {
    fun transpose(input: List<String>): List<List<Int>> {
        val matrix = input.map { it.toCharArray().map { c -> c.digitToInt() } }
        val colSize = matrix[0].size
        var columns: MutableList<MutableList<Int>> = mutableListOf()
        for (i in 0 until colSize) {
            columns.add(i, mutableListOf<Int>())
        }
        for ((rowIndex, row) in matrix.withIndex()) {
            for ((colIndex, value) in row.withIndex()) {
                columns[colIndex].add(rowIndex, value)
            }
        }
        return columns
    }

    fun recursiveFilter(input: List<String>, useMostSignificantBit: Boolean, bitIndex: Int = 0): List<String> {
        val colValues = input.map { it.toCharArray()[bitIndex].digitToInt() }
        var significantBitForColumn = colValues.average().roundToInt()
        var leastSignificantBitForColum = if (significantBitForColumn == 1) 0 else 1
        var bitToInclude = if (useMostSignificantBit) significantBitForColumn else leastSignificantBitForColum

        val filtered = input.filter { it[bitIndex].digitToInt() == bitToInclude }
        if (filtered.size <= 1) {
            return filtered
        }
        return recursiveFilter(filtered, useMostSignificantBit, bitIndex + 1)
    }

    fun part1(input: List<String>): Int {
        val columns = transpose(input)

        var gamma = ""
        var epsilon = ""
        for (element in columns) {
            val oneIsSignificantBit = (element.average().roundToInt() == 1)
            gamma += if (oneIsSignificantBit) "1" else "0"
            epsilon += if (!oneIsSignificantBit) "1" else "0"
        }

        return Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)
    }

    fun part2(input: List<String>): Int {
        val oxygenGeneratorRating = recursiveFilter(input, true).first()
        val co2ScrubberRating = recursiveFilter(input, false).first()

        return Integer.parseInt(oxygenGeneratorRating, 2) * Integer.parseInt(co2ScrubberRating, 2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
