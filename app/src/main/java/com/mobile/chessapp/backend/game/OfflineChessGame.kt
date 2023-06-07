package com.mobile.chessapp.backend.game

import android.widget.Toast
import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor

class OfflineChessGame(
    board: ChessBoard,
    playerColor: PieceColor = PieceColor.WHITE,
    oppColor: PieceColor = PieceColor.BLACK,
) : ChessGame(board, playerColor, oppColor) {
    override fun prepareOpponentsTurn() {
        boardUI.flip()
    }

    override fun surrender() {
        winner = if (board.activeColor == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
        board.isGameOver = true
    }
}