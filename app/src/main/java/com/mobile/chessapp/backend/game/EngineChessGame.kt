package com.mobile.chessapp.backend.game

import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PlayerColor
import com.mobile.chessapp.backend.game.engineUtils.ChessEngine

class EngineChessGame(
  board: ChessBoard,
  playerColor: PlayerColor = PlayerColor.WHITE,
  oppColor: PlayerColor = PlayerColor.BLACK,
) : ChessGame(board, playerColor, oppColor) {

  override fun prepareOpponentsTurn() {
    onEngineMove()
  }

  override fun surrender() {
    winner = oppColor
    board.isGameOver = true
  }

  private fun onEngineMove() {
    if (oppColor != board.activeColor) return

    val eval = ChessEngine.findBestMove(board)
    val move = eval.bestMove ?: return

    board.doMove(move)
    moveArchive.add(move)
    boardUI.updateFields(board)
  }
}