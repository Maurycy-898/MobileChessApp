package com.mobile.chessapp.backend.game.boardUtils

data class ChessPiece(var type: PieceType, var color: PieceColor) {
    fun getValue() : Int {
        return color.colorFactor * type.pieceValue
    }
}

enum class PieceColor(val colorFactor: Int) {
    WHITE(colorFactor = 1),
    BLACK(colorFactor = -1),
}

enum class PieceType(val pieceValue: Int) {
    KING(pieceValue = 1000000), PAWN(pieceValue = 100),
    KNIGHT(pieceValue = 320),   BISHOP(pieceValue = 330),
    ROOK(pieceValue = 500),     QUEEN(pieceValue = 900)

}
