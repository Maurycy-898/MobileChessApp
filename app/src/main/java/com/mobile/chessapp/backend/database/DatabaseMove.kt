package com.mobile.chessapp.backend.database

import com.mobile.chessapp.backend.game.boardUtils.PieceType
import com.mobile.chessapp.backend.game.moveUtils.ChessMove

data class DatabaseMove(
    var chessMove: ChessMove? = null,
    var newPiece: PieceType? = null,
    var isEnPassantMove: Boolean = false,
    var isCastlingMove: Boolean = false
)
