package com.mobile.chessapp.backend.game

import android.widget.Toast
import com.mobile.chessapp.backend.game.boardUtils.BOARD_SIZE
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.backend.game.engineUtils.ChessEngine
import com.mobile.chessapp.backend.game.moveUtils.ChessMove
import com.mobile.chessapp.backend.game.moveUtils.MoveGenerator
import java.util.LinkedList

enum class GameMode { ONLINE, OFFLINE, ENGINE }

class ChessGame (
    var board: ChessBoard,
    var gameMode: GameMode = GameMode.OFFLINE,
    var playerColor: PieceColor = PieceColor.WHITE,
    var oppColor: PieceColor = PieceColor.BLACK,
) {

    private var clickCount: Int =  0
    private val NOT_CLICKED: Int = -1

    private var prevCol = NOT_CLICKED
    private var prevRow = NOT_CLICKED

    private var moveArchive = LinkedList<ChessMove>()
    private var gameEnded: Boolean = false

    var boardUI: BoardUI = BoardUI(playerColor, board)
    private var possibleMovesMap = Array(BOARD_SIZE) {
        Array<ChessMove?>(BOARD_SIZE) { null }
    }

    init {
        if (gameMode == GameMode.ENGINE) onEngineMove()
    }

    fun onFieldClick(col: Int, row: Int) {
        clickCount += 1
        when (gameMode) {
            GameMode.OFFLINE -> offlineClick(col, row)
            GameMode.ENGINE  -> engineClick(col, row)
            GameMode.ONLINE  -> onlineClick(col, row)
        }
    }

    private fun offlineClick(col: Int, row: Int) {
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
                boardUI.flip()
            }
            onCancel(); return
        }
    }

    private fun engineClick(col: Int, row: Int) {
        if (clickCount == 1) { // map moves
            if (board.fields[col][row]?.color != playerColor) {
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
                onEngineMove() // engine makes its move
            }
            onCancel(); return
        }
    }

    private fun onlineClick(col: Int, row: Int) {
        // TODO: implement
    }

    private fun onEngineMove() {
        if (oppColor == board.activeColor) {
            val eval = ChessEngine.findBestMove(board)
            val move = eval.bestMove

            if (move != null) {
                board.doMove(move)
                moveArchive.add(move)
                boardUI.updateFields(board)
            }
        }
    }

    private fun onCancel() {
        clickCount = 0
        prevCol = NOT_CLICKED
        prevRow = NOT_CLICKED
        for (i in 0 until BOARD_SIZE) for (j in 0 until BOARD_SIZE) {
            possibleMovesMap[i][j] = null
        }
        boardUI.updateFields(board)
    }
}
