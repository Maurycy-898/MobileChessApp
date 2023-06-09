package com.mobile.chessapp.backend.game.moveUtils

import com.mobile.chessapp.backend.game.boardUtils.ChessPiece
import com.mobile.chessapp.backend.game.boardUtils.PieceType

open class ChessMove(var beginCol: Int = 0, var beginRow: Int = 0, var endCol: Int = 0, var endRow: Int = 0) : java.io.Serializable {
    var movedPiece: ChessPiece? = null
    var takenPiece: ChessPiece? = null

    var savedWhiteKingsideCastling = false
    var savedWhiteQueensideCastling = false
    var savedBlackKingsideCastling = false
    var savedBlackQueensideCastling = false

    var savedEnPassantPossible = false
    var savedEnPassantTargetCol = 0
    var savedEnPassantTargetRow = 0

    var savedWhiteKingCol = 0
    var savedWhiteKingRow = 0
    var savedBlackKingCol = 0
    var savedBlackKingRow = 0

    var savedWhiteKingAttacked = false
    var savedBlackKingAttacked = false

    override fun toString() : String {
        var result = ""
        when (beginCol) {
            0 -> result += "a"
            1 -> result += "b"
            2 -> result += "c"
            3 -> result += "d"
            4 -> result += "e"
            5 -> result += "f"
            6 -> result += "g"
            7 -> result += "h"
        }
        result += (beginRow + 1).toString()

        result += " "
        when (endCol) {
            0 -> result += "a"
            1 -> result += "b"
            2 -> result += "c"
            3 -> result += "d"
            4 -> result += "e"
            5 -> result += "f"
            6 -> result += "g"
            7 -> result += "h"
        }
        result += (endRow + 1).toString()
        return result
    }

    fun toPromotionMove(newPiece: PieceType) : PromotionMove {
        val move = PromotionMove(beginCol, beginRow, endCol, endRow, newPiece)
        move.movedPiece = movedPiece
        move.takenPiece = takenPiece

        move.savedWhiteKingsideCastling = savedWhiteKingsideCastling
        move.savedWhiteQueensideCastling = savedWhiteQueensideCastling
        move.savedBlackKingsideCastling = savedBlackKingsideCastling
        move.savedBlackQueensideCastling = savedBlackQueensideCastling

        move.savedEnPassantPossible = savedEnPassantPossible
        move.savedEnPassantTargetCol = savedEnPassantTargetCol
        move.savedEnPassantTargetRow = savedEnPassantTargetRow

        move.savedWhiteKingCol = savedWhiteKingCol
        move.savedWhiteKingRow = savedWhiteKingRow
        move.savedBlackKingCol = savedBlackKingCol
        move.savedBlackKingRow = savedBlackKingRow

        move.savedWhiteKingAttacked = savedWhiteKingAttacked
        move.savedBlackKingAttacked = savedBlackKingAttacked
        return move
    }

    fun toEnPassantMove() : EnPassantMove {
        val move = EnPassantMove(beginCol, beginRow, endCol, endRow)
        move.movedPiece = movedPiece
        move.takenPiece = takenPiece

        move.savedWhiteKingsideCastling = savedWhiteKingsideCastling
        move.savedWhiteQueensideCastling = savedWhiteQueensideCastling
        move.savedBlackKingsideCastling = savedBlackKingsideCastling
        move.savedBlackQueensideCastling = savedBlackQueensideCastling

        move.savedEnPassantPossible = savedEnPassantPossible
        move.savedEnPassantTargetCol = savedEnPassantTargetCol
        move.savedEnPassantTargetRow = savedEnPassantTargetRow

        move.savedWhiteKingCol = savedWhiteKingCol
        move.savedWhiteKingRow = savedWhiteKingRow
        move.savedBlackKingCol = savedBlackKingCol
        move.savedBlackKingRow = savedBlackKingRow

        move.savedWhiteKingAttacked = savedWhiteKingAttacked
        move.savedBlackKingAttacked = savedBlackKingAttacked
        return move
    }

    fun toCastlingMove() : CastlingMove {
        val move = CastlingMove(beginCol, beginRow, endCol, endRow)
        move.movedPiece = movedPiece
        move.takenPiece = takenPiece

        move.savedWhiteKingsideCastling = savedWhiteKingsideCastling
        move.savedWhiteQueensideCastling = savedWhiteQueensideCastling
        move.savedBlackKingsideCastling = savedBlackKingsideCastling
        move.savedBlackQueensideCastling = savedBlackQueensideCastling

        move.savedEnPassantPossible = savedEnPassantPossible
        move.savedEnPassantTargetCol = savedEnPassantTargetCol
        move.savedEnPassantTargetRow = savedEnPassantTargetRow

        move.savedWhiteKingCol = savedWhiteKingCol
        move.savedWhiteKingRow = savedWhiteKingRow
        move.savedBlackKingCol = savedBlackKingCol
        move.savedBlackKingRow = savedBlackKingRow

        move.savedWhiteKingAttacked = savedWhiteKingAttacked
        move.savedBlackKingAttacked = savedBlackKingAttacked
        return move
    }
}


class PromotionMove(beginCol: Int, beginRow: Int, endCol: Int, endRow: Int,
                    var newPiece: PieceType) : ChessMove(beginCol, beginRow, endCol, endRow) {
    override fun toString() : String {
        var result = super.toString() + " "
        when (newPiece) {
            PieceType.KNIGHT -> result += "n"
            PieceType.BISHOP -> result += "b"
            PieceType.ROOK   -> result += "r"
            PieceType.QUEEN  -> result += "q"
            else -> { /* do nothing */ }
        }
        return result
    }
}


class CastlingMove(beginCol: Int, beginRow: Int, endCol: Int, endRow: Int) :
    ChessMove(beginCol, beginRow, endCol, endRow)


class EnPassantMove(beginCol: Int, beginRow: Int, endCol: Int, endRow: Int) :
    ChessMove(beginCol, beginRow, endCol, endRow)
