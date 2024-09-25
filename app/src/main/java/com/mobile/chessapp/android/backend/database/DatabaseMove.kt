package com.mobile.chessapp.android.backend.database

import com.mobile.chessapp.android.backend.game.boardUtils.PieceType
import com.mobile.chessapp.android.backend.game.moveUtils.ChessMove

class DatabaseMove(
    var chessMove: ChessMove? = null,
    var newPiece: PieceType? = null,
    var isEnPassantMove: Boolean = false,
    var isCastlingMove: Boolean = false
)
