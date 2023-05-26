package com.mobile.chessapp.backend.game.boardUtils

import com.mobile.chessapp.backend.game.moveUtils.MoveGenerator
import com.mobile.chessapp.backend.game.moveUtils.MoveGenerator.attacked


object BoardBuilder {
    fun createFromFEN(FEN: String): ChessBoard {
        val board = ChessBoard()
        val elements = FEN.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val rows = elements[0].split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        var colIndex: Int
        for (i in 0..7) {
            colIndex = 0
            for (j in 0 until rows[i].length) {
                if (rows[i][j] == 'p') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.PAWN, PieceColor.BLACK)
                    colIndex++
                } else if (rows[i][j] == 'r') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.ROOK, PieceColor.BLACK)
                    colIndex++
                } else if (rows[i][j] == 'n') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.KNIGHT, PieceColor.BLACK)
                    colIndex++
                } else if (rows[i][j] == 'b') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.BISHOP, PieceColor.BLACK)
                    colIndex++
                } else if (rows[i][j] == 'q') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.QUEEN, PieceColor.BLACK)
                    colIndex++
                } else if (rows[i][j] == 'k') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.KING, PieceColor.BLACK)
                    board.blackKingCol = colIndex
                    board.blackKingRow = 7 - i
                    colIndex++
                } else if (rows[i][j] == 'P') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.PAWN, PieceColor.WHITE)
                    colIndex++
                } else if (rows[i][j] == 'R') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.ROOK, PieceColor.WHITE)
                    colIndex++
                } else if (rows[i][j] == 'N') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.KNIGHT, PieceColor.WHITE)
                    colIndex++
                } else if (rows[i][j] == 'B') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.BISHOP, PieceColor.WHITE)
                    colIndex++
                } else if (rows[i][j] == 'Q') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.QUEEN, PieceColor.WHITE)
                    colIndex++
                } else if (rows[i][j] == 'K') {
                    board.fields[colIndex][7 - i] = ChessPiece(PieceType.KING, PieceColor.WHITE)
                    board.whiteKingCol = colIndex
                    board.whiteKingRow = 7 - i
                    colIndex++
                } else { colIndex += ("" + rows[i][j]).toInt() }
            }
        }
        if (elements[1].compareTo("b") == 0) {
            board.activeColor = PieceColor.BLACK
        } else {
            board.activeColor = PieceColor.WHITE
        }
        for (i in 0 until elements[2].length) {
            if (elements[2][i] == 'K') {
                board.whiteKingsideCastling = true
            } else if (elements[2][i] == 'Q') {
                board.whiteQueensideCastling = true
            } else if (elements[2][i] == 'k') {
                board.blackKingsideCastling = true
            } else if (elements[2][i] == 'q') {
                board.blackQueensideCastling = true
            }
        }
        if (elements[3].compareTo("-") != 0) {
            board.enPassantPossible = true
            board.enPassantTargetCol = elements[3][0].code - 97
            board.enPassantTargetRow = ("" + elements[3][1]).toInt() - 1
        }
        MoveGenerator.generatorSetup(board)
        board.whiteKingAttacked = attacked(board.whiteKingCol, board.whiteKingRow)
        board.blackKingAttacked = attacked(board.blackKingCol, board.blackKingRow)
        return board
    }
}
