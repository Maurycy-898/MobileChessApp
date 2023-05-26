package com.mobile.chessapp.backend.game

import com.mobile.chessapp.backend.game.boardUtils.*
import com.mobile.chessapp.ui.theme.DARK_FIELD_COLOR
import com.mobile.chessapp.ui.theme.LIGHT_FIELD_COLOR


// Handles ui related to chess board
class BoardUI(private var playerPOV: PieceColor = PieceColor.WHITE, board: ChessBoard) {
    var fields = Array(BOARD_SIZE) { col ->
        Array(BOARD_SIZE) { row ->
            val symbol: String = asSymbol(board.fields[col][row])
            val color = if ((col + row) % 2 == 0) DARK_FIELD_COLOR else LIGHT_FIELD_COLOR
            FieldUI(col, row, symbol, color)
        }
    }

    fun getField(col: Int, row: Int) : FieldUI {
        return fields[translateCol(col)][translateRow(row)]
    }

    fun updateFields(board: ChessBoard) {
        for (col in 0 until BOARD_SIZE) for (row in 0 until BOARD_SIZE) {
            val symbol: String = asSymbol(board.fields[col][row])
            fields[col][row].symbol = symbol
            fields[col][row].prompted = false
        }
    }

    fun flip() {
        playerPOV = if (playerPOV == PieceColor.WHITE) PieceColor.BLACK
        else PieceColor.WHITE
    }

    private fun asSymbol(piece: ChessPiece?) : String {
        if (piece == null) return " "
        if (piece.color == PieceColor.WHITE) {
            return when(piece.type) {
                PieceType.PAWN   -> "♙"
                PieceType.KNIGHT -> "♘"
                PieceType.BISHOP -> "♗"
                PieceType.ROOK   -> "♖"
                PieceType.QUEEN  -> "♕"
                PieceType.KING   -> "♔"
            }
        }
        if (piece.color == PieceColor.BLACK) {
            return when(piece.type) {
                PieceType.PAWN   -> "♟"
                PieceType.KNIGHT -> "♞"
                PieceType.BISHOP -> "♝"
                PieceType.ROOK   -> "♜"
                PieceType.QUEEN  -> "♛"
                PieceType.KING   -> "♚"
            }
        }
        return " "
    }

    private fun translateCol(col: Int) : Int {
        return if (playerPOV == PieceColor.WHITE) col
        else (BOARD_SIZE-1 - col)
    }

    private fun translateRow(row: Int) : Int {
        return if (playerPOV == PieceColor.BLACK) row
        else (BOARD_SIZE-1 - row)
    }
}

data class FieldUI(
    val col: Int, val row: Int,
    var symbol: String, var color: Int,
    var prompted: Boolean = false
)
