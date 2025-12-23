package com.mobile.chessapp.backend.game.boardUtils

import android.util.Log
import com.mobile.chessapp.backend.game.moveUtils.*


const val BOARD_SIZE = 8

class ChessBoard : java.io.Serializable {
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
    var activeColor = PlayerColor.WHITE
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
            move.takenPiece = fields[move.endCol][move.beginRow]
            fields[move.endCol][move.beginRow] = null
        }
        else if (move is CastlingMove) {
            if (move.endCol == 2 && move.endRow == 0) {
                fields[0][0] = null; fields[4][0] = null
                fields[3][0] = ChessPiece(PieceType.ROOK, PlayerColor.WHITE)
                fields[2][0] = ChessPiece(PieceType.KING, PlayerColor.WHITE)
            } else if (move.endCol == 6 && move.endRow == 0) {
                fields[7][0] = null; fields[4][0] = null
                fields[5][0] = ChessPiece(PieceType.ROOK, PlayerColor.WHITE)
                fields[6][0] = ChessPiece(PieceType.KING, PlayerColor.WHITE)
            } else if (move.endCol == 2 && move.endRow == 7) {
                fields[0][7] = null; fields[4][7] = null
                fields[3][7] = ChessPiece(PieceType.ROOK, PlayerColor.BLACK)
                fields[2][7] = ChessPiece(PieceType.KING, PlayerColor.BLACK)
            } else if (move.endCol == 6 && move.endRow == 7) {
                fields[7][7] = null; fields[4][7] = null
                fields[5][7] = ChessPiece(PieceType.ROOK, PlayerColor.BLACK)
                fields[6][7] = ChessPiece(PieceType.KING, PlayerColor.BLACK)
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

        if (movedPiece?.type == PieceType.KING && movedPiece.color == PlayerColor.WHITE) {
            whiteKingsideCastling = false
            whiteQueensideCastling = false
            whiteKingCol = move.endCol
            whiteKingRow = move.endRow
        }
        else if(movedPiece?.type == PieceType.KING && movedPiece.color == PlayerColor.BLACK) {
            blackKingsideCastling = false
            blackQueensideCastling = false
            blackKingCol = move.endCol
            blackKingRow = move.endRow
        }

        if (movedPiece?.type == PieceType.PAWN && movedPiece.color == PlayerColor.WHITE && move.endRow - move.beginRow == 2) {
            enPassantPossible = true
            enPassantTargetCol = move.beginCol
            enPassantTargetRow = move.beginRow+1
        }
        else if (movedPiece?.type == PieceType.PAWN && movedPiece.color == PlayerColor.BLACK && move.endRow - move.beginRow == -2) {
            enPassantPossible = true
            enPassantTargetCol = move.beginCol
            enPassantTargetRow = move.beginRow-1
        }
        else {
            enPassantPossible = false
        }
        activeColor = if (activeColor == PlayerColor.WHITE) PlayerColor.BLACK else PlayerColor.WHITE
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
            Log.d("takenPiece", move.takenPiece.toString())
            fields[move.endCol][move.beginRow] = move.takenPiece

        } else if (move is CastlingMove) {
            if (move.endCol == 2 && move.endRow == 0) {
                fields[0][0] = ChessPiece(PieceType.ROOK, PlayerColor.WHITE)
                fields[4][0] = ChessPiece(PieceType.KING, PlayerColor.WHITE)
                fields[3][0] = null; fields[2][0] = null

            } else if (move.endCol == 6 && move.endRow == 0) {
                fields[7][0] = ChessPiece(PieceType.ROOK, PlayerColor.WHITE)
                fields[4][0] = ChessPiece(PieceType.KING, PlayerColor.WHITE)
                fields[5][0] = null; fields[6][0] = null

            } else if (move.endCol == 2 && move.endRow == 7) {
                fields[0][7] = ChessPiece(PieceType.ROOK, PlayerColor.BLACK)
                fields[4][7] = ChessPiece(PieceType.KING, PlayerColor.BLACK)
                fields[3][7] = null; fields[2][7] = null
            } else if (move.endCol == 6 && move.endRow == 7) {
                fields[7][7] = ChessPiece(PieceType.ROOK, PlayerColor.BLACK)
                fields[4][7] = ChessPiece(PieceType.KING, PlayerColor.BLACK)
                fields[5][7] = null; fields[6][7] = null
            }
        } else {
            val movedPiece = fields[move.endCol][move.endRow]
            fields[move.endCol][move.endRow] = move.takenPiece
            fields[move.beginCol][move.beginRow] = movedPiece
        }
        if (isGameOver) isGameOver = false // undo move = undo final result
        activeColor = if (activeColor == PlayerColor.WHITE) PlayerColor.BLACK else PlayerColor.WHITE
    }


    private fun setupBoard() {
        activeColor = PlayerColor.WHITE
        for (i in 0 until BOARD_SIZE) {
            fields[i][1] = ChessPiece(PieceType.PAWN, PlayerColor.WHITE)
            fields[i][6] = ChessPiece(PieceType.PAWN, PlayerColor.BLACK)
        }

        fields[0][0] = ChessPiece(PieceType.ROOK, PlayerColor.WHITE)
        fields[1][0] = ChessPiece(PieceType.KNIGHT, PlayerColor.WHITE)
        fields[2][0] = ChessPiece(PieceType.BISHOP, PlayerColor.WHITE)
        fields[3][0] = ChessPiece(PieceType.QUEEN, PlayerColor.WHITE)
        fields[4][0] = ChessPiece(PieceType.KING, PlayerColor.WHITE)
        fields[5][0] = ChessPiece(PieceType.BISHOP, PlayerColor.WHITE)
        fields[6][0] = ChessPiece(PieceType.KNIGHT, PlayerColor.WHITE)
        fields[7][0] = ChessPiece(PieceType.ROOK, PlayerColor.WHITE)

        fields[0][7] = ChessPiece(PieceType.ROOK, PlayerColor.BLACK)
        fields[1][7] = ChessPiece(PieceType.KNIGHT, PlayerColor.BLACK)
        fields[2][7] = ChessPiece(PieceType.BISHOP, PlayerColor.BLACK)
        fields[3][7] = ChessPiece(PieceType.QUEEN, PlayerColor.BLACK)
        fields[4][7] = ChessPiece(PieceType.KING, PlayerColor.BLACK)
        fields[5][7] = ChessPiece(PieceType.BISHOP, PlayerColor.BLACK)
        fields[6][7] = ChessPiece(PieceType.KNIGHT, PlayerColor.BLACK)
        fields[7][7] = ChessPiece(PieceType.ROOK, PlayerColor.BLACK)

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


    fun getKingCol(color: PlayerColor) : Int {
        return when(color) {
            PlayerColor.WHITE -> whiteKingCol
            PlayerColor.BLACK -> blackKingCol
        }
    }

    fun getKingRow(color: PlayerColor) : Int {
        return when(color) {
            PlayerColor.WHITE -> whiteKingRow
            PlayerColor.BLACK -> blackKingRow
        }
    }
}
