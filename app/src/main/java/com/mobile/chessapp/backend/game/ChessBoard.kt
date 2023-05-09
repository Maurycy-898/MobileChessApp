package com.mobile.chessapp.backend.game

class ChessBoard {
    var board = Array(8) { Array(8) { ChessPiece(PieceColor.WHITE, PieceType.NONE) } }

    init {
        setupBoard()
    }
    fun setupBoard() {
        for (i in 0 until 8) {
            for (j in 1 until 7) {
                board[i][j].type = PieceType.NONE
            }
        }
        for (i in 0 until 8) {
            board[i][6].color = PieceColor.BLACK
            board[i][7].color = PieceColor.BLACK
        }
        for (i in 0 until 8) {
            board[i][1].type = PieceType.PAWN
            board[i][6].type = PieceType.PAWN
        }
        for (j in listOf(0, 7)) {
            board[0][j].type = PieceType.ROOK
            board[7][j].type = PieceType.ROOK
            board[1][j].type = PieceType.KNIGHT
            board[6][j].type = PieceType.KNIGHT
            board[2][j].type = PieceType.BISHOP
            board[5][j].type = PieceType.BISHOP
            board[3][j].type = PieceType.QUEEN
            board[4][j].type = PieceType.KING
        }
    }
}