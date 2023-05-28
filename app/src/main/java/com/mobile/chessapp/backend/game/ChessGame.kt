package com.mobile.chessapp.backend.game

import com.mobile.chessapp.backend.game.boardUtils.BOARD_SIZE
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.backend.game.moveUtils.ChessMove
import com.mobile.chessapp.backend.game.moveUtils.MoveGenerator
import java.util.*

abstract class ChessGame(
    var board: ChessBoard,
    var playerColor: PieceColor = PieceColor.WHITE,
    var oppColor: PieceColor = PieceColor.BLACK,
) : java.io.Serializable {

    protected var clickCount: Int =  0
    private val NOT_CLICKED: Int = -1

    private var prevCol = NOT_CLICKED
    private var prevRow = NOT_CLICKED

    protected var moveArchive = LinkedList<ChessMove>()
    private var gameEnded: Boolean = false

    var boardUI: BoardUI = BoardUI(playerColor, board)
    private var possibleMovesMap = Array(BOARD_SIZE) {
        Array<ChessMove?>(BOARD_SIZE) { null }
    }

    abstract fun onFieldClick(col: Int, row: Int)

    protected fun click(col: Int, row: Int) {
        if (clickCount == 1) { // map moves
            if (board.fields[col][row]?.color != board.activeColor) {
                onCancel(); return
            }
            prevCol = col
            prevRow = row

            val moves = MoveGenerator.generateMoves(board, col, row)
            for (move in moves) {
                possibleMovesMap[move.endCol][move.endRow] = move
                boardUI.fields[move.endCol][move.endRow].prompted = true
            }
        }
        else if (clickCount == 2) { // make move
            if (possibleMovesMap[col][row] != null) {
                val moveMade = possibleMovesMap[col][row]!!
                board.doMove(moveMade)
                moveArchive.add(moveMade)
                boardUI.updateFields(board)
                prepareOpponentsTurn()
            }
            onCancel(); return
        }
    }

    abstract fun prepareOpponentsTurn()

    protected fun onCancel() {
        clickCount = 0
        prevCol = NOT_CLICKED
        prevRow = NOT_CLICKED
        for (i in 0 until BOARD_SIZE) for (j in 0 until BOARD_SIZE) {
            possibleMovesMap[i][j] = null
        }
        boardUI.updateFields(board)
    }
}
