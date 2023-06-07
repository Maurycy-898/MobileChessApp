package com.mobile.chessapp.backend.game

import androidx.annotation.DrawableRes
import com.mobile.chessapp.R
import com.mobile.chessapp.backend.game.boardUtils.*
import com.mobile.chessapp.ui.theme.DARK_FIELD_COLOR
import com.mobile.chessapp.ui.theme.LIGHT_FIELD_COLOR


// Handles ui related to chess board
class BoardUI(private var playerPOV: PieceColor = PieceColor.WHITE, board: ChessBoard) : java.io.Serializable {
    var fields = Array(BOARD_SIZE) { col ->
        Array(BOARD_SIZE) { row ->
            val symbol: String = asSymbol(board.fields[col][row])
            val color = if ((col + row) % 2 == 0) DARK_FIELD_COLOR else LIGHT_FIELD_COLOR
            val imgRes = asImageRes(board.fields[col][row])
            FieldUI(col, row, symbol, imgRes, color)
        }
    }

    fun getField(col: Int, row: Int) : FieldUI {
        return fields[translateCol(col)][translateRow(row)]
    }

    fun updateFields(board: ChessBoard) {
        for (col in 0 until BOARD_SIZE) for (row in 0 until BOARD_SIZE) {
            val symbol: String = asSymbol(board.fields[col][row])
            val imgRes: Int = asImageRes(board.fields[col][row])
            fields[col][row].symbol = symbol
            fields[col][row].imgRes = imgRes
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

    private fun asImageRes(piece: ChessPiece?) : Int {
        if (piece == null) return FieldUI.EMPTY_FIELD_RES
        if (piece.color == PieceColor.WHITE) {
            return when(piece.type) {
                PieceType.PAWN   -> R.drawable.chess_plt60
                PieceType.KNIGHT -> R.drawable.chess_nlt60
                PieceType.BISHOP -> R.drawable.chess_blt60
                PieceType.ROOK   -> R.drawable.chess_rlt60
                PieceType.QUEEN  -> R.drawable.chess_qlt60
                PieceType.KING   -> R.drawable.chess_klt60
            }
        }
        if (piece.color == PieceColor.BLACK) {
            return when(piece.type) {
                PieceType.PAWN   -> R.drawable.chess_pdt60
                PieceType.KNIGHT -> R.drawable.chess_ndt60
                PieceType.BISHOP -> R.drawable.chess_bdt60
                PieceType.ROOK   -> R.drawable.chess_rdt60
                PieceType.QUEEN  -> R.drawable.chess_qdt60
                PieceType.KING   -> R.drawable.chess_kdt60
            }
        }
        return FieldUI.EMPTY_FIELD_RES
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
    var symbol: String,
    var imgRes: Int,
    var color: Int,
    var prompted: Boolean = false
) : java.io.Serializable {
    companion object {
        const val EMPTY_FIELD_RES = -1
    }
}
