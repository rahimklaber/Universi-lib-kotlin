package universi.game

//TODO add direction enum
/**
 * Class representing a reversi game with a black and white player.
 *
 * @property board
 */
class Game(val board: Board) {
    //the id might be useful if you make the game multilayer Todo is there a better way?
    var id: Int = Classid++
    var started = false
    lateinit var blackPlayer: Player
    lateinit var whitePlayer: Player


    /**
     * Executes the move.
     *
     * @param cell cell that will be placed on.
     * @param player player placing the piece.
     * @return number of cells captured.
     */

    fun move(cell: Cell, player: Player): Int {
        if (!started) {
            throw IllegalStateException("Game has not started.")
        }
        return board.move(cell, player.color)

    }

    fun blackMove(cell: Cell): Int {
        return move(cell, blackPlayer)
    }

    fun whiteMove(cell: Cell): Int {
        return move(cell, whitePlayer)

    }

    fun anyMovePossible(player: Player): Boolean {
        return board.anyMovePossible(player.color)
    }

    fun isMovePossible(cell: Cell, player: Player): Boolean {
        return board.isMovePossible(cell, player.color)
    }


    fun switchTurns() {
        blackPlayer.turn = !blackPlayer.turn
        whitePlayer.turn = !whitePlayer.turn
    }

    fun start() {
        started = !started
    }

    fun stop() {
        started = !started
    }

    companion object {
        var Classid: Int = 0
    }

}

class Spectator

class Cell(val x: Int, val y: Int, var color: Color = Color.GREEN) {

    fun isSameColor(other: Cell): Boolean {
        return color == other.color
    }


    fun isOppositeColorTo(other: Cell): Boolean {
        return color.opposite() == other.color
    }

    fun isWhite(): Boolean {
        return color == Color.WHITE
    }

    fun isBlack(): Boolean {
        return color == Color.BLACK
    }

    fun flip(mover: Color): Color {
        color = if (color == Color.GREEN) {
            mover

        } else {
            color.opposite()
        }
        return color
    }
}


class Board : Iterable<Cell> {
    private val internalBoard: Array<Array<Cell>> = arrayOf(
        arrayOf(Cell(1, 1), Cell(2, 1), Cell(3, 1), Cell(4, 1), Cell(5, 1), Cell(6, 1), Cell(7, 1), Cell(8, 1)),
        arrayOf(Cell(1, 2), Cell(2, 2), Cell(3, 2), Cell(4, 2), Cell(5, 2), Cell(6, 2), Cell(7, 2), Cell(8, 2)),
        arrayOf(Cell(1, 3), Cell(2, 3), Cell(3, 3), Cell(4, 3), Cell(5, 3), Cell(6, 3), Cell(7, 3), Cell(8, 3)),
        arrayOf(Cell(1, 4), Cell(2, 4), Cell(3, 4), Cell(4, 4), Cell(5, 4), Cell(6, 4), Cell(7, 4), Cell(8, 4)),
        arrayOf(Cell(1, 5), Cell(2, 5), Cell(3, 5), Cell(4, 5), Cell(5, 5), Cell(6, 5), Cell(7, 5), Cell(8, 5)),
        arrayOf(Cell(1, 6), Cell(2, 6), Cell(3, 6), Cell(4, 6), Cell(5, 6), Cell(6, 6), Cell(7, 6), Cell(8, 6)),
        arrayOf(Cell(1, 7), Cell(2, 7), Cell(3, 7), Cell(4, 7), Cell(5, 7), Cell(6, 7), Cell(7, 7), Cell(8, 7)),
        arrayOf(Cell(1, 8), Cell(2, 8), Cell(3, 8), Cell(4, 8), Cell(5, 8), Cell(6, 8), Cell(7, 8), Cell(8, 8))
    )

    init {
        this[4, 4].color = Color.WHITE
        this[4, 5].color = Color.BLACK
        this[5, 4].color = Color.BLACK
        this[5, 5].color = Color.WHITE
    }

    val directions: Array<Array<Int>> = arrayOf(
        arrayOf(1, 1),
        arrayOf(-1, 1),
        arrayOf(1, -1),
        arrayOf(-1, -1),
        arrayOf(1, 0),
        arrayOf(0, 1),
        arrayOf(-1, 0),
        arrayOf(0, -1)
    )

    fun count(colorToMatch: Color): Int {
        var result: Int = 0
        for (cell in this) {
            if (cell.color == colorToMatch) result++
        }
        return result
    }

    fun blackCount(): Int {
        return count(Color.BLACK)
    }

    fun whiteCount(): Int {
        return count(Color.WHITE)
    }


    /**
     * Travels in the given direction until it finds a match (Same color cell) for the starting cell or
     * until it reaches the end of the board.
     * @param start the cell from which the traversal will start.
     * @param dx the x direction to go in.
     * @param dy the y direction to go in.
     * @return a list of cells that are between the start and its match or an empty list if a match was not found.
     * TODO (add a option to or not to return a list?) wait wtf was i thinking SHould i combine this and travelInDirection? with a boolean option?
     */

    fun travelAndGetInDirection(start: Cell, colorToMatch: Color, dx: Int, dy: Int): ArrayList<Cell> {
        if (start.color != Color.GREEN) {
            return ArrayList()
        }
        var currentX = start.x
        var currentY = start.y
        var matched = false
        val result: ArrayList<Cell> = ArrayList()
        while (true) {
            currentX += dx
            currentY += dy
            if (currentX > 8 || currentY > 8 || currentX < 1 || currentY < 1) break
            if (this[currentX, currentY].color == Color.GREEN) break
            if (this[currentX, currentY].color == colorToMatch) {
                matched = true
                break
            }
            if (this[currentX, currentY].color.opposite() == colorToMatch) result.add(this[currentX, currentY])
        }
        return if (matched) result else ArrayList(0)
    }

    /**
     * Travels in the given direction until it finds a match (colorToMatch) or
     * until it reaches the end of the board.
     * @param start the cell from which the traversal will start.
     * @param dx the x direction to go in.
     * @param dy the y direction to go in.
     * @return true if the there was a match else false.
     */
    private fun travelInDirection(start: Cell, colorToMatch: Color, dx: Int, dy: Int): Boolean {
        if (colorToMatch == Color.GREEN) return false
        if (start.color != Color.GREEN) return false

        var currentX = start.x
        var currentY = start.y
        var matched = false
        while (true) {
            currentX += dx
            currentY += dy
            if (currentX > 8 || currentY > 8 || currentX < 1 || currentY < 1) break
            if (this[currentX, currentY].color == Color.GREEN) break
            if (this[currentX, currentY].color == colorToMatch) {
                matched = true
                break
            }

        }
        return matched
    }

    /**
     * get the cell with the supplied coordinates.
     *  x: 1, y : 1 is the top-left corner.
     * @param x the x coordinate, starts at 1.
     * @param y the y coordinate, starts at 1.
     */
    operator fun get(x: Int, y: Int): Cell {
        return internalBoard[y - 1][x - 1]
    }

    /**
     * checks if moves are possible for both colors.
     *
     * @return true if there are still moves possible else false
     */

    fun anyMovePossible(): Boolean {
        for (cell in this) {
            for (direction in directions) {
                if (travelInDirection(cell, Color.WHITE, direction[0], direction[1])) return true
                if (travelInDirection(cell, Color.BLACK, direction[0], direction[1])) return true
            }
        }
        return false
    }

    fun size(): Int {
        return internalBoard.size
    }

    /**
     * Flips the pieces that need flipping when making a move.
     *
     * @param cell the position of the placed piece ( it's not flipped yet)
     * @param colorToMatch the color of the player that made the move.
     * @return the number of cells flipped.
     */

    fun move(cell: Cell, colorToMatch: Color): Int {
        val cellsToFlip = arrayListOf<Cell>()
        for (direction in directions) {
            cellsToFlip.addAll(travelAndGetInDirection(cell, colorToMatch, direction[0], direction[1]))
        }
        if (cellsToFlip.isEmpty()) {
            return 0
        }
        cellsToFlip.add(cell)
        flipCells(cellsToFlip, colorToMatch)
        return cellsToFlip.count()
    }

    /**
     * checks if moves are possible for the color given.
     *
     * @param color
     * @return true if there are moves possible else false.
     */
    fun anyMovePossible(color: Color): Boolean {
        for (cell in this) {
            for (direction in directions) {
                if (travelInDirection(cell, color, direction[0], direction[1])) return true
            }
        }
        return false
    }

    fun anyWhiteMovePossible(): Boolean {
        return anyMovePossible(Color.WHITE)
    }

    fun anyBlackMovePossible(): Boolean {
        return anyMovePossible(Color.BLACK)
    }

    fun isMovePossible(cell: Cell, color: Color): Boolean {
        for (direction in directions) {
            if (travelInDirection(cell, color, direction[0], direction[1])) return true
        }
        return false
    }

    fun isWhiteMovePossible(cell: Cell): Boolean {
        return isMovePossible(cell, Color.WHITE)
    }

    fun isBlackMovePossible(cell: Cell): Boolean {
        return isMovePossible(cell, Color.BLACK)
    }

    private fun flipCells(cells: ArrayList<Cell>, mover: Color) {
        for (cell in cells) {
            cell.flip(mover)
        }
    }

    override operator fun iterator(): Iterator<Cell> {
        return BoardIterator(this)
    }


}

class BoardIterator(private val board: Board) : Iterator<Cell> {
    private var currentX: Int = 1
    private var currentY: Int = 1
    override fun next(): Cell {
        val x = currentX++
        val y = currentY
        //TODO: why do i need this if?

        if (x == 8 && y == 8)
        else if (currentX == board.size() + 1) {
            currentX = 1
            currentY++
        }

        return board[x, y]
    }

    override fun hasNext(): Boolean {
        return currentX * currentY <= 64
    }

}

enum class Color(val colornr: Int) {
    BLACK(2),
    WHITE(1),
    GREEN(0);

    fun opposite(): Color {
        return when (this) {
            BLACK -> WHITE
            WHITE -> BLACK
            else -> GREEN
        }
    }
}

class Player(val name: String, val color: Color, var turn: Boolean)



