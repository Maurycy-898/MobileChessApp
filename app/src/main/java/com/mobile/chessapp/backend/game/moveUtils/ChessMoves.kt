package com.mobile.chessapp.backend.game.moveUtils

import com.mobile.chessapp.backend.game.boardUtils.ChessPiece
import com.mobile.chessapp.backend.game.boardUtils.PieceType

open class Move(var beginCol: Int, var beginRow: Int, var endCol: Int, var endRow: Int) {
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
}


class PromotionMove(beginCol: Int, beginRow: Int, endCol: Int, endRow: Int,
                    var newPiece: PieceType) : Move(beginCol, beginRow, endCol, endRow) {
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
    Move(beginCol, beginRow, endCol, endRow)


class EnPassantMove(beginCol: Int, beginRow: Int, endCol: Int, endRow: Int) :
    Move(beginCol, beginRow, endCol, endRow)
