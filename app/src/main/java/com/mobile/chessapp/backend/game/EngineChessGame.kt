package com.mobile.chessapp.backend.game

import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.backend.game.engineUtils.ChessEngine

class EngineChessGame(
    board: ChessBoard,
    playerColor: PieceColor = PieceColor.WHITE,
    oppColor: PieceColor = PieceColor.BLACK,
) : ChessGame(board, playerColor, oppColor) {
    override fun onFieldClick(col: Int, row: Int) {
        clickCount += 1
        click(col, row)
    }

    override fun prepareOpponentsTurn() {
        onEngineMove()
    }

    private fun onEngineMove() {
        if (oppColor == board.activeColor) {
            val eval = ChessEngine.findBestMove(board)
            val move = eval.bestMove

            if (move != null) {
                board.doMove(move)
                moveArchive.add(move)
                boardUI.updateFields(board)
            }
        }
    }
}