package com.mobile.chessapp.backend.game

import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PlayerColor

class OfflineChessGame(
  board: ChessBoard,
  playerColor: PlayerColor = PlayerColor.WHITE,
  oppColor: PlayerColor = PlayerColor.BLACK,
) : ChessGame(board, playerColor, oppColor) {
    override fun prepareOpponentsTurn() {
        boardUI.flip()
    }

    override fun surrender() {
        winner = if (board.activeColor == PlayerColor.WHITE) PlayerColor.BLACK else PlayerColor.WHITE
        board.isGameOver = true
    }
}