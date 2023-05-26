package com.mobile.chessapp.backend.game.engineUtils

import com.mobile.chessapp.backend.game.boardUtils.*

val WINNING_SCORE = 1000000
val LOSING_SCORE  = -1000000
val DRAWING_SCORE = 0

object Evaluator {
    fun evaluate(board: ChessBoard): Int {
        var evaluation = 0
        var currentPiece: ChessPiece?
        for (row in 0 until BOARD_SIZE) {
            for (column in 0 until BOARD_SIZE) {
                currentPiece = board.fields[column][row]
                currentPiece?.let{ evaluation += calcTotalPieceValue(it, row, column) }
            }
        }
        return evaluation
    }

    private fun calcTotalPieceValue(piece: ChessPiece, row: Int, column: Int): Int {
        return piece.getValue() + positionImpact(piece, row, column)
    }

    private fun positionImpact(piece: ChessPiece, row: Int, column: Int): Int {
        var absRow = row // based on color reverse row to get correct  data from tables
        if (piece.color == PieceColor.WHITE) {
            absRow = BOARD_SIZE-1 - absRow
        }
        val positionAspectVal: Int = when (piece.type) {
            PieceType.PAWN   -> PAWN_POSITION_VALUE_TABLE[absRow][column]
            PieceType.KNIGHT -> KNIGHT_POSITION_VALUE_TABLE[absRow][column]
            PieceType.BISHOP -> BISHOP_POSITION_VALUE_TABLE[absRow][column]
            PieceType.ROOK   -> ROOK_POSITION_VALUE_TABLE[absRow][column]
            PieceType.QUEEN  -> QUEEN_POSITION_VALUE_TABLE[absRow][column]
            PieceType.KING   -> KING_POSITION_VALUE_TABLE[absRow][column]
        }
        return positionAspectVal * piece.color.colorFactor
    }
}


//--------------------------------------- EVALUATION TABLES ----------------------------------------
val PAWN_POSITION_VALUE_TABLE = arrayOf(
    intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
    intArrayOf(50, 50, 50, 50, 50, 50, 50, 50),
    intArrayOf(10, 10, 20, 30, 30, 20, 10, 10),
    intArrayOf(5, 5, 10, 25, 25, 10, 5, 5),
    intArrayOf(0, 0, 5, 25, 25, 0, 0, 0),
    intArrayOf(5, 0, 5, 5, 5, -10, -5, 5),
    intArrayOf(5, 10, 10, -20, -20, 10, 10, 5),
    intArrayOf(0, 0, 0, 0, 0, 0, 0, 0)
)


val KNIGHT_POSITION_VALUE_TABLE = arrayOf(
    intArrayOf(-50, -40, -30, -30, -30, -30, -40, -50),
    intArrayOf(-40, -20, 0, 0, 0, 0, -20, -40),
    intArrayOf(-30, 0, 10, 15, 15, 10, 0, -30),
    intArrayOf(-30, 5, 15, 20, 20, 15, 5, -30),
    intArrayOf(-30, 0, 15, 20, 20, 15, 0, -30),
    intArrayOf(-30, 5, 10, 15, 15, 10, 5, -30),
    intArrayOf(-40, -20, 0, 5, 5, 0, -20, -40),
    intArrayOf(-50, -40, -30, -30, -30, -30, -40, -50)
)


val BISHOP_POSITION_VALUE_TABLE = arrayOf(
    intArrayOf(-20, -10, -10, -10, -10, -10, -10, -20),
    intArrayOf(-10, 0, 0, 0, 0, 0, 0, -10),
    intArrayOf(-10, 0, 5, 10, 10, 5, 0, -10),
    intArrayOf(-10, 5, 5, 10, 10, 5, 5, -10),
    intArrayOf(-10, 0, 10, 10, 10, 10, 0, -10),
    intArrayOf(-10, 10, 10, 10, 10, 10, 10, -10),
    intArrayOf(-10, 5, 0, 0, 0, 0, 5, -10),
    intArrayOf(-20, -10, -10, -10, -10, -10, -10, -20)
)


val ROOK_POSITION_VALUE_TABLE = arrayOf(
    intArrayOf(0, 0, 0, 0, 0, 0, 0, 0),
    intArrayOf(5, 10, 10, 10, 10, 10, 10, 5),
    intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
    intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
    intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
    intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
    intArrayOf(-5, 0, 0, 0, 0, 0, 0, -5),
    intArrayOf(0, 0, 0, 5, 5, 0, 0, 0)
)


val QUEEN_POSITION_VALUE_TABLE = arrayOf(
    intArrayOf(-20, -10, -10, -5, -5, -10, -10, -20),
    intArrayOf(-10, 0, 0, 0, 0, 0, 0, -10),
    intArrayOf(-10, 0, 5, 5, 5, 5, 0, -10),
    intArrayOf(-5, 0, 5, 5, 5, 5, 0, -5),
    intArrayOf(0, 0, 5, 5, 5, 5, 0, -5),
    intArrayOf(-10, 5, 5, 5, 5, 5, 0, -10),
    intArrayOf(-10, 0, 5, 0, 0, 0, 0, -10),
    intArrayOf(-20, -10, -10, -5, -5, -10, -10, -20)
)


val KING_POSITION_VALUE_TABLE = arrayOf(
    intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
    intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
    intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
    intArrayOf(-30, -40, -40, -50, -50, -40, -40, -30),
    intArrayOf(-20, -30, -30, -40, -40, -30, -30, -20),
    intArrayOf(-10, -20, -20, -20, -20, -20, -20, -10),
    intArrayOf(20, 20, 0, 0, 0, 0, 20, 20),
    intArrayOf(20, 30, 10, 0, 0, 10, 30, 20)
)
