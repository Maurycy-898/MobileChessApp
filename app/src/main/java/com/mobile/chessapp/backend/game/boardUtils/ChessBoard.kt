package com.mobile.chessapp.backend.game.boardUtils

import com.mobile.chessapp.backend.game.moveUtils.*


const val BOARD_SIZE = 8

class ChessBoard {
    var whiteKingsideCastling = false
    var whiteQueensideCastling = false
    var blackKingsideCastling = false
    var blackQueensideCastling = false

    var enPassantPossible = false
    var enPassantTargetCol = 0
    var enPassantTargetRow = 0

    var whiteKingCol = 0
    var whiteKingRow = 0
    var blackKingCol = 0
    var blackKingRow = 0

    var whiteKingAttacked = false
    var blackKingAttacked = false

    var isGameOver: Boolean = false
    var activeColor = PieceColor.WHITE
    val fields = Array(BOARD_SIZE) {
        Array<ChessPiece?>(BOARD_SIZE) { null }
    }


    init { setupBoard() }


    fun doMove(move: ChessMove) {
        val movedPiece: ChessPiece? = fields[move.beginCol][move.beginRow]

        move.savedEnPassantPossible = enPassantPossible
        move.savedEnPassantTargetCol = enPassantTargetCol
        move.savedEnPassantTargetRow = enPassantTargetRow

        move.savedWhiteKingsideCastling = whiteKingsideCastling
        move.savedWhiteQueensideCastling = whiteQueensideCastling
        move.savedBlackKingsideCastling = blackKingsideCastling
        move.savedBlackQueensideCastling = blackQueensideCastling

        move.savedWhiteKingCol = whiteKingCol
        move.savedWhiteKingRow = whiteKingRow
        move.savedBlackKingCol = blackKingCol
        move.savedBlackKingRow = blackKingRow

        move.savedWhiteKingAttacked = whiteKingAttacked
        move.savedBlackKingAttacked = blackKingAttacked


        if (move is PromotionMove) {
            fields[move.beginCol][move.beginRow] = null
            move.takenPiece = fields[move.endCol][move.endRow]
            fields[move.endCol][move.endRow] = ChessPiece(move.newPiece, movedPiece!!.color)
        }
        else if (move is EnPassantMove) {
            fields[move.beginCol][move.beginRow] = null
            fields[move.endCol][move.endRow] = movedPiece
            fields[move.endCol][move.beginRow] = null
            move.takenPiece = fields[move.endCol][move.beginRow]
        }
        else if (move is CastlingMove) {
            println("Castling ... ")
            if (move.endCol == 2 && move.endRow == 0) {
                fields[0][0] = null; fields[4][0] = null
                fields[3][0] = ChessPiece(PieceType.ROOK, PieceColor.WHITE)
                fields[2][0] = ChessPiece(PieceType.KING, PieceColor.WHITE)
            } else if (move.endCol == 6 && move.endRow == 0) {
                fields[7][0] = null; fields[4][0] = null
                fields[5][0] = ChessPiece(PieceType.ROOK, PieceColor.WHITE)
                fields[6][0] = ChessPiece(PieceType.KING, PieceColor.WHITE)
            } else if (move.endCol == 2 && move.endRow == 7) {
                fields[0][7] = null; fields[4][7] = null
                fields[3][7] = ChessPiece(PieceType.ROOK, PieceColor.BLACK)
                fields[2][7] = ChessPiece(PieceType.KING, PieceColor.BLACK)
            } else if (move.endCol == 6 && move.endRow == 7) {
                fields[7][7] = null; fields[4][7] = null
                fields[5][7] = ChessPiece(PieceType.ROOK, PieceColor.BLACK)
                fields[6][7] = ChessPiece(PieceType.KING, PieceColor.BLACK)
            }
        }
        else {
            fields[move.beginCol][move.beginRow] = null
            move.takenPiece = fields[move.endCol][move.endRow]
            fields[move.endCol][move.endRow] = movedPiece
        }

        if (move.beginCol == 0 && move.beginRow == 0) {
            whiteQueensideCastling = false
        } else if (move.beginCol == 7 && move.beginRow == 0) {
            whiteKingsideCastling = false
        } else if (move.beginCol == 0 && move.beginRow == 7) {
            blackQueensideCastling = false
        } else if (move.beginCol == 7 && move.beginRow == 7) {
            blackKingsideCastling = false
        }

        if (move.endCol == 0 && move.endRow == 0) {
            whiteQueensideCastling = false
        } else if(move.endCol == 7 && move.endRow == 0) {
            whiteKingsideCastling = false
        } else if(move.endCol == 0 && move.endRow == 7) {
            blackQueensideCastling = false
        } else if(move.endCol == 7 && move.endRow == 7) {
            blackKingsideCastling = false
        }

        if (movedPiece?.type == PieceType.KING && movedPiece.color == PieceColor.WHITE) {
            whiteKingsideCastling = false
            whiteQueensideCastling = false
            whiteKingCol = move.endCol
            whiteKingRow = move.endRow
        }
        else if(movedPiece?.type == PieceType.KING && movedPiece.color == PieceColor.BLACK) {
            blackKingsideCastling = false
            blackQueensideCastling = false
            blackKingCol = move.endCol
            blackKingRow = move.endRow
        }

        if (movedPiece?.type == PieceType.PAWN && movedPiece.color == PieceColor.WHITE && move.endRow - move.beginRow == 2) {
            enPassantPossible = true
            enPassantTargetCol = move.beginCol
            enPassantTargetRow = move.beginRow+1
        }
        else if (movedPiece?.type == PieceType.PAWN && movedPiece.color == PieceColor.BLACK && move.endRow - move.beginRow == -2) {
            enPassantPossible = true
            enPassantTargetCol = move.beginCol
            enPassantTargetRow = move.beginRow-1
        }
        else {
            enPassantPossible = false
        }
        activeColor = if (activeColor == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE

        MoveGenerator.generatorSetup(this)
        whiteKingAttacked = MoveGenerator.attacked(whiteKingCol, whiteKingRow)
        blackKingAttacked = MoveGenerator.attacked(blackKingCol, blackKingRow)
    }


    fun undoMove(move: ChessMove) {
        whiteKingsideCastling = move.savedWhiteKingsideCastling
        whiteQueensideCastling = move.savedWhiteQueensideCastling
        blackKingsideCastling = move.savedBlackKingsideCastling
        blackQueensideCastling = move.savedBlackQueensideCastling

        enPassantPossible = move.savedEnPassantPossible
        enPassantTargetCol = move.savedEnPassantTargetCol
        enPassantTargetRow = move.savedEnPassantTargetRow

        whiteKingCol = move.savedWhiteKingCol
        whiteKingRow = move.savedWhiteKingRow
        blackKingCol = move.savedBlackKingCol
        blackKingRow = move.savedBlackKingRow

        whiteKingAttacked = move.savedWhiteKingAttacked
        blackKingAttacked = move.savedBlackKingAttacked

        if (move is PromotionMove) {
            val color = fields[move.endCol][move.endRow]!!.color
            fields[move.endCol][move.endRow] = move.takenPiece
            fields[move.beginCol][move.beginRow] = ChessPiece(PieceType.PAWN, color)
        }
        else if (move is EnPassantMove) {
            val movedPiece = fields[move.endCol][move.endRow]
            fields[move.endCol][move.endRow] = null
            fields[move.beginCol][move.beginRow] = movedPiece
            fields[move.endCol][move.beginRow] = move.takenPiece

        } else if (move is CastlingMove) {
            if (move.endCol == 2 && move.endRow == 0) {
                fields[0][0] = ChessPiece(PieceType.ROOK, PieceColor.WHITE)
                fields[4][0] = ChessPiece(PieceType.KING, PieceColor.WHITE)
                fields[3][0] = null; fields[2][0] = null

            } else if (move.endCol == 6 && move.endRow == 0) {
                fields[7][0] = ChessPiece(PieceType.ROOK, PieceColor.WHITE)
                fields[4][0] = ChessPiece(PieceType.KING, PieceColor.WHITE)
                fields[5][0] = null; fields[6][0] = null

            } else if (move.endCol == 2 && move.endRow == 7) {
                fields[0][7] = ChessPiece(PieceType.ROOK, PieceColor.BLACK)
                fields[4][7] = ChessPiece(PieceType.KING, PieceColor.BLACK)
                fields[3][7] = null; fields[2][7] = null
            } else if (move.endCol == 6 && move.endRow == 7) {
                fields[7][7] = ChessPiece(PieceType.ROOK, PieceColor.BLACK)
                fields[4][7] = ChessPiece(PieceType.KING, PieceColor.BLACK)
                fields[5][7] = null; fields[6][7] = null
            }
        } else {
            val movedPiece = fields[move.endCol][move.endRow]
            fields[move.endCol][move.endRow] = move.takenPiece
            fields[move.beginCol][move.beginRow] = movedPiece
        }

        activeColor = if (activeColor == PieceColor.WHITE) PieceColor.BLACK else PieceColor.WHITE
    }


    private fun setupBoard() {
        activeColor = PieceColor.WHITE
        for (i in 0 until BOARD_SIZE) {
            fields[i][1] = ChessPiece(PieceType.PAWN, PieceColor.WHITE)
            fields[i][6] = ChessPiece(PieceType.PAWN, PieceColor.BLACK)
        }

        fields[0][0] = ChessPiece(PieceType.ROOK, PieceColor.WHITE)
        fields[1][0] = ChessPiece(PieceType.KNIGHT, PieceColor.WHITE)
        fields[2][0] = ChessPiece(PieceType.BISHOP, PieceColor.WHITE)
        fields[3][0] = ChessPiece(PieceType.QUEEN, PieceColor.WHITE)
        fields[4][0] = ChessPiece(PieceType.KING, PieceColor.WHITE)
        fields[5][0] = ChessPiece(PieceType.BISHOP, PieceColor.WHITE)
        fields[6][0] = ChessPiece(PieceType.KNIGHT, PieceColor.WHITE)
        fields[7][0] = ChessPiece(PieceType.ROOK, PieceColor.WHITE)

        fields[0][7] = ChessPiece(PieceType.ROOK, PieceColor.BLACK)
        fields[1][7] = ChessPiece(PieceType.KNIGHT, PieceColor.BLACK)
        fields[2][7] = ChessPiece(PieceType.BISHOP, PieceColor.BLACK)
        fields[3][7] = ChessPiece(PieceType.QUEEN, PieceColor.BLACK)
        fields[4][7] = ChessPiece(PieceType.KING, PieceColor.BLACK)
        fields[5][7] = ChessPiece(PieceType.BISHOP, PieceColor.BLACK)
        fields[6][7] = ChessPiece(PieceType.KNIGHT, PieceColor.BLACK)
        fields[7][7] = ChessPiece(PieceType.ROOK, PieceColor.BLACK)

        whiteKingCol = 4
        whiteKingRow = 0
        blackKingCol = 4
        blackKingRow = 7

        blackKingsideCastling = true
        whiteKingsideCastling = true

        blackQueensideCastling = true
        whiteQueensideCastling = true

        blackKingAttacked = false
        whiteKingAttacked = false
    }


    fun getKingCol(color: PieceColor) : Int {
        return when(color) {
            PieceColor.WHITE -> whiteKingCol
            PieceColor.BLACK -> blackKingCol
        }
    }

    fun getKingRow(color: PieceColor) : Int {
        return when(color) {
            PieceColor.WHITE -> whiteKingRow
            PieceColor.BLACK -> blackKingRow
        }
    }
}
