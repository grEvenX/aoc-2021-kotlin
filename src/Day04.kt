
data class BingoNumber(val number: Int, val colIndex: Int, val rowIndex: Int) {
    var checked: Boolean = false
}

class Bingo(
    availableNumbers: List<Int>
) {
    private var availableNumbers: MutableList<Int>
    private var boards: MutableList<BingoBoard> = mutableListOf()

    init {
        this.availableNumbers = availableNumbers.toMutableList()
    }

    fun popNextNumber(): Int {
        val number = availableNumbers.removeFirst()
        boards.forEach { it.markNumber(number) }
        return number
    }

    fun addBoard(board: BingoBoard) {
        boards.add(board)
    }

    fun checkForWins(): BingoBoard? {
        return boards.firstOrNull { it.hasBingo() }
    }

    fun boardsWithoutWins(): List<BingoBoard> {
        return boards.filter { !it.hasBingo() }
    }
}

class BingoBoard(
    private val numbers: List<BingoNumber>
) {
    private var colMarked: MutableMap<Int, Int> = mutableMapOf()
    private var rowMarked: MutableMap<Int, Int> = mutableMapOf()

    fun unmarkedNumbers(): List<Int> {
        return numbers.filter { !it.checked }.map { it.number }
    }

    fun markNumber(number: Int) {
        numbers.filter { it.number == number }.forEach {
            colMarked[it.colIndex] = colMarked[it.colIndex]?.plus(1) ?: 1
            rowMarked[it.rowIndex] = rowMarked[it.rowIndex]?.plus(1) ?: 1
            it.checked = true
        }
    }

    fun hasBingo(): Boolean {
        // check all rows and columns
        for (i in 0..4) {
            if (numbers.filter { it.rowIndex == i }.all { it.checked }) {
                return true
            }
            if (numbers.filter { it.colIndex == i }.all { it.checked }) {
                return true
            }
        }
        return false
    }
}

fun main() {

    fun parseGameFromInput(input: List<String>): Bingo {
        val mutableInput = input.toMutableList()
        // Read first line for Bingo
        val firstLine = mutableInput.removeFirst()
        val randomNumbers = firstLine.split(",").map { it.toInt() }
        val bingo = Bingo(randomNumbers)
        mutableInput.removeFirst()

        var allBoards = mutableListOf<List<String>>()
        var boardLines = mutableListOf<String>()
        for ((lineIndex, line) in mutableInput.withIndex()) {
            if (line != "") {
                boardLines.add(line)
            }
            if (line == "" || lineIndex == (mutableInput.size - 1)) {
                allBoards.add(boardLines)
                boardLines = mutableListOf()
            }
        }

        // Map to board rows
        // Create boards
        for (board in allBoards) {
            // Map board lines to BoardNumbers with rowIndex and colIndex ?
            var boardNumbers = mutableListOf<BingoNumber>()
            for ((rowIndex, row) in board.withIndex()) {
                val rowNumbers = row.trim().split("""[^\d]+""".toRegex()).map { it.toInt() }
                for ((cellIndex, cellValue) in rowNumbers.withIndex()) {
                    // Need to create a boardNumber (not same reference)
                    val number = BingoNumber(cellValue, rowIndex = rowIndex, colIndex = cellIndex)
                    boardNumbers.add(number)
                }
            }
            bingo.addBoard(BingoBoard(boardNumbers))
        }
        return bingo
    }

    fun part1(input: List<String>): Int {
        val bingo = parseGameFromInput(input)
        var winner: BingoBoard? = null
        var lastNumberPicked: Int? = null
        while (winner == null) {
            lastNumberPicked = bingo.popNextNumber()
            winner = bingo.checkForWins()
        }

        // take the winning board
        return winner.unmarkedNumbers().sum() * lastNumberPicked!!
    }

    fun part2(input: List<String>): Int {
        val bingo = parseGameFromInput(input)
        var looser: BingoBoard? = null
        var lastNumberPicked: Int? = null
        while (bingo.boardsWithoutWins().isNotEmpty()) {
            lastNumberPicked = bingo.popNextNumber()
            val loosingBoards = bingo.boardsWithoutWins()
            if (loosingBoards.size == 1) {
                looser = bingo.boardsWithoutWins().first()
            }
        }

        // take the loosing board
        return looser!!.unmarkedNumbers().sum() * lastNumberPicked!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 4512)
    check(part2(testInput) == 1924)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
