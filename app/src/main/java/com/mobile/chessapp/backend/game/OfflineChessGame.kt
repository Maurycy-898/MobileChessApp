package com.mobile.chessapp.backend.game

import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor

class OfflineChessGame(
    board: ChessBoard,
    playerColor: PieceColor = PieceColor.WHITE,
    oppColor: PieceColor = PieceColor.BLACK,
) : ChessGame(board, playerColor, oppColor) {
    override fun onFieldClick(col: Int, row: Int) {
        clickCount += 1
        click(col, row)
    }

    override fun prepareOpponentsTurn() {
        boardUI.flip()
    }
}