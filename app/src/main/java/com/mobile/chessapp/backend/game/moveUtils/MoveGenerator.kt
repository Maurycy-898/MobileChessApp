package com.mobile.chessapp.backend.game.moveUtils

import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.boardUtils.PieceColor
import com.mobile.chessapp.backend.game.boardUtils.PieceType
import java.util.*


object MoveGenerator {
    private var board: ChessBoard = ChessBoard()
    private var oppAttacks: MutableList<Move> = LinkedList()
    private var playerColor = PieceColor.WHITE
    private var oppColor = PieceColor.BLACK


    /**
     * generates all possible moves in given position for active player
     */
    fun generateMoves(board: ChessBoard) : LinkedList<Move> {
        generatorSetup(board)
        val possibleMoves: LinkedList<Move> = LinkedList()

        for (i in 0..7) for (j in 0..7) {
            if (hasColor(i, j, playerColor)) {
                continue
            }
            when (board.fields[i][j]?.type) {
                PieceType.PAWN   -> addPawnMoves(i, j, playerColor, possibleMoves)
                PieceType.KNIGHT -> addKnightMoves(i, j, playerColor, possibleMoves)
                PieceType.BISHOP -> addBishopMoves(i, j, playerColor, possibleMoves)
                PieceType.ROOK   -> addRookMoves(i, j, playerColor, possibleMoves)
                PieceType.QUEEN  -> addQueenMoves(i, j, playerColor, possibleMoves)
                PieceType.KING   -> addKingMoves(i, j, playerColor, possibleMoves)
                else -> { /* do nothing- empty field */ }
            }
        }

        var kingCol = 0
        var kingRow = 0

        val illegalMoves: LinkedList<Move> = LinkedList()
        for (move in possibleMoves) {
            board.doMove(move)
            for (i in 0..7) for (j in 0..7) {
                if (!freeSquare(i, j) // find king
                    && board.fields[i][j]!!.color == playerColor
                    && board.fields[i][j]!!.type  == PieceType.KING
                ) {
                    kingCol = i
                    kingRow = j
                }
            }
            if (isSquareAttacked(kingCol, kingRow)) {
                illegalMoves.add(move)
            }
            board.undoMove(move)
        }

        possibleMoves.removeAll(illegalMoves)
        return possibleMoves
    }


    /**
     * generates all legal moves for given (row, col) position for active player
     */
    fun generateMoves(board: ChessBoard, col: Int, row: Int) : LinkedList<Move> {
        generatorSetup(board)
        val possibleMoves: LinkedList<Move> = LinkedList()
        if (!hasColor(col, row, playerColor)) return possibleMoves

        when (board.fields[col][row]?.type) {
            PieceType.PAWN   -> addPawnMoves(col, row, playerColor, possibleMoves)
            PieceType.KNIGHT -> addKnightMoves(col, row, playerColor, possibleMoves)
            PieceType.BISHOP -> addBishopMoves(col, row, playerColor, possibleMoves)
            PieceType.ROOK   -> addRookMoves(col, row, playerColor, possibleMoves)
            PieceType.QUEEN  -> addQueenMoves(col, row, playerColor, possibleMoves)
            PieceType.KING   -> addKingMoves(col, row, playerColor, possibleMoves)
            else -> { /* do nothing- empty field */ }
        }

        var kingCol = 0
        var kingRow = 0

        val illegalMoves: LinkedList<Move> = LinkedList()
        for (move in possibleMoves) {
            board.doMove(move)
            for (i in 0..7) for (j in 0..7) {
                if (!freeSquare(i, j) // find king
                    && board.fields[i][j]!!.color == playerColor
                    && board.fields[i][j]!!.type  == PieceType.KING
                ) {
                    kingCol = i
                    kingRow = j
                }
            }
            if (isSquareAttacked(kingCol, kingRow)) {
                illegalMoves.add(move)
            }
            board.undoMove(move)
        }

        possibleMoves.removeAll(illegalMoves)
        return possibleMoves
    }


    //-------------------------------------- UTILITY METHODS ---------------------------------------
    fun generatorSetup(board: ChessBoard) {
        this.board = board
        this.playerColor = board.activeColor
        this.oppColor = opponentColor(board.activeColor)
        this.oppAttacks = getAttackingMoves(oppColor)
    }


    /**
     * all moves that attack some field (used to keep track if opponent attacks it)
     */
    private fun getAttackingMoves(color: PieceColor) : MutableList<Move> {
        val attackingMoves = LinkedList<Move>()
        for (i in 0..7) for (j in 0..7) {
            if (!hasColor(i, j, color)) {
                continue
            }
            when (board.fields[i][j]?.type) {
                PieceType.PAWN   -> addPawnAttackingMoves(i, j, color, attackingMoves)
                PieceType.KNIGHT -> addKnightMoves(i, j, color, attackingMoves)
                PieceType.BISHOP -> addBishopMoves(i, j, color, attackingMoves)
                PieceType.ROOK   -> addRookMoves(i, j, color, attackingMoves)
                PieceType.QUEEN  -> addQueenMoves(i, j, color, attackingMoves)
                PieceType.KING   -> addKingAttackingMoves(i, j, color, attackingMoves)
                else -> { /* do nothing- empty field */ }
            }
        }
        return attackingMoves
    }


    /**
     * Checks if given square is attacked by pieces of given color
     * */
    fun isSquareAttacked(col: Int, row: Int) : Boolean {
        for (move in oppAttacks) {
            if (move.endCol == col && move.endRow == row) return true
        }
        return false
    }


    /**
     * returns an opposite color to the one we provided
     */
    private fun opponentColor(color: PieceColor) : PieceColor {
        return if (color === PieceColor.BLACK) { PieceColor.WHITE } else { PieceColor.BLACK }
    }


    /**
     * checks if position is still on the chessboard
     */
    private fun onChessboard(col: Int, row: Int) : Boolean {
        return ((col >= 0) && (col <= 7) && (row >= 0) && (row <= 7))
    }


    /**
     * checks if given board field is free (there is no piece here)
     */
    private fun freeSquare(col: Int, row: Int): Boolean {
        return board.fields[col][row] == null
    }


    /**
     * checks if piece on given square has expected color
     */
    private fun hasColor(col: Int, row: Int, color: PieceColor): Boolean {
        return if (board.fields[col][row] == null) { false }
            else { board.fields[col][row]?.color == color }
    }


    /**
     * checks if piece on given square has expected color or is free
     */
    private fun freeOrColor(col: Int, row: Int, color: PieceColor): Boolean {
        if (board.fields[col][row] == null) return true
        else if (board.fields[col][row]!!.color == color) return true
        return false
    }


    //----------------------------------- PIECE MOVES GENERATION -----------------------------------
    private fun addPawnMoves(col: Int, row: Int, color: PieceColor, moves: MutableList<Move>) {
        val oppColor = opponentColor(color)
        if (color == PieceColor.WHITE) {
            if (onChessboard(col, row + 1) && freeSquare(col, row + 1)) {
                if (row + 1 == 7) {
                    moves.add(PromotionMove(col, row, col, row + 1, PieceType.ROOK))
                    moves.add(PromotionMove(col, row, col, row + 1, PieceType.KNIGHT))
                    moves.add(PromotionMove(col, row, col, row + 1, PieceType.BISHOP))
                    moves.add(PromotionMove(col, row, col, row + 1, PieceType.QUEEN))
                } else { moves.add(Move(col, row, col, row + 1)) }
            }

            if (row == 1 && onChessboard(col, row + 2)
                && freeSquare(col, row + 2)
                && freeSquare(col, row + 1)
            ) {
                moves.add(Move(col, row, col, row + 2))
            }

            if (onChessboard(col + 1, row + 1)
                && !freeSquare(col + 1, row + 1)
                && board.fields[col + 1][row + 1]!!.color == oppColor
            ) {
                if (row + 1 == 7) {
                    moves.add(PromotionMove(col, row, col + 1, row + 1, PieceType.QUEEN))
                    moves.add(PromotionMove(col, row, col + 1, row + 1, PieceType.ROOK))
                    moves.add(PromotionMove(col, row, col + 1, row + 1, PieceType.KNIGHT))
                    moves.add(PromotionMove(col, row, col + 1, row + 1, PieceType.BISHOP))
                } else { moves.add(Move(col, row, col + 1, row + 1)) }
            }

            if (onChessboard(col - 1, row + 1)
                && !freeSquare(col - 1, row + 1)
                && board.fields[col - 1][row + 1]!!.color == oppColor
            ) {
                if (row + 1 == 7) {
                    moves.add(PromotionMove(col, row, col - 1, row + 1, PieceType.QUEEN))
                    moves.add(PromotionMove(col, row, col - 1, row + 1, PieceType.ROOK))
                    moves.add(PromotionMove(col, row, col - 1, row + 1, PieceType.KNIGHT))
                    moves.add(PromotionMove(col, row, col - 1, row + 1, PieceType.BISHOP))
                } else { moves.add(Move(col, row, col - 1, row + 1)) }
            }

            if (board.enPassantPossible && row == 4 && board.enPassantTargetCol == col + 1) {
                moves.add(EnPassantMove(col, row, col + 1, row + 1))
            }
            if (board.enPassantPossible && row == 4 && board.enPassantTargetCol == col - 1) {
                moves.add(EnPassantMove(col, row, col - 1, row + 1))
            }

        } else { // PieceColor == BLACK
            if (onChessboard(col, row - 1) && freeSquare(col, row - 1)) {
                if (row - 1 == 0) {
                    moves.add(PromotionMove(col, row, col, 0, PieceType.ROOK))
                    moves.add(PromotionMove(col, row, col, 0, PieceType.KNIGHT))
                    moves.add(PromotionMove(col, row, col, 0, PieceType.BISHOP))
                    moves.add(PromotionMove(col, row, col, 0, PieceType.QUEEN))
                } else { moves.add(Move(col, row, col, row - 1)) }
            }

            if (row == 6 && onChessboard(col, row - 2)
                && freeSquare(col, row - 2)
                && freeSquare(col, row - 1)
            ) {
                moves.add(Move(col, row, col, row - 2))
            }

            if (onChessboard(col + 1, row - 1)
                && !freeSquare(col + 1, row - 1)
                && board.fields[col + 1][row - 1]!!.color == oppColor) {

                if (row - 1 == 0) {
                    moves.add(PromotionMove(col, row, col + 1, 0, PieceType.ROOK))
                    moves.add(PromotionMove(col, row, col + 1, 0, PieceType.KNIGHT))
                    moves.add(PromotionMove(col, row, col + 1, 0, PieceType.BISHOP))
                    moves.add(PromotionMove(col, row, col + 1, 0, PieceType.QUEEN))
                } else { moves.add(Move(col, row, col + 1, row - 1)) }
            }

            if (onChessboard(col - 1, row - 1)
                && !freeSquare(col - 1, row - 1)
                && board.fields[col - 1][row - 1]!!.color === oppColor
            ) {
                if (row - 1 == 0) {
                    moves.add(PromotionMove(col, row, col - 1, 0, PieceType.ROOK))
                    moves.add(PromotionMove(col, row, col - 1, 0, PieceType.KNIGHT))
                    moves.add(PromotionMove(col, row, col - 1, 0, PieceType.BISHOP))
                    moves.add(PromotionMove(col, row, col - 1, 0, PieceType.QUEEN))
                } else { moves.add(Move(col, row, col - 1, row - 1)) }
            }


            if (board.enPassantPossible && row == 3 && board.enPassantTargetCol == col + 1) {
                moves.add(EnPassantMove(col, row, col + 1, row - 1))
            }
            if (board.enPassantPossible && row == 3 && board.enPassantTargetCol == col - 1) {
                moves.add(EnPassantMove(col, row, col - 1, row - 1))
            }
        }
    }


    private fun addKnightMoves(col: Int, row: Int, color: PieceColor, moves: MutableList<Move>) {
        val oppColor = opponentColor(color)
        if (onChessboard(col + 2, row + 1) && freeOrColor(col + 2, row + 1, oppColor)) {
            moves.add(Move(col, row, col + 2, row + 1))
        }
        if (onChessboard(col + 2, row - 1) && freeOrColor(col + 2, row - 1, oppColor)) {
            moves.add(Move(col, row, col + 2, row - 1))
        }
        if (onChessboard(col - 2, row + 1) && freeOrColor(col - 2, row + 1, oppColor)) {
            moves.add(Move(col, row, col - 2, row + 1))
        }
        if (onChessboard(col - 2, row - 1) && freeOrColor(col - 2, row - 1, oppColor)) {
            moves.add(Move(col, row, col - 2, row - 1))
        }
        if (onChessboard(col + 1, row + 2) && freeOrColor(col + 1, row + 2, oppColor)) {
            moves.add(Move(col, row, col + 1, row + 2))
        }
        if (onChessboard(col + 1, row - 2) && freeOrColor(col + 1, row - 2, oppColor)) {
            moves.add(Move(col, row, col + 1, row - 2))
        }
        if (onChessboard(col - 1, row + 2) && freeOrColor(col - 1, row + 2, oppColor)) {
            moves.add(Move(col, row, col - 1, row + 2))
        }
        if (onChessboard(col - 1, row - 2) && freeOrColor(col - 1, row - 2, oppColor)) {
            moves.add(Move(col, row, col - 1, row - 2))
        }
    }


    private fun addBishopMoves(col: Int, row: Int, color: PieceColor, moves: MutableList<Move>) {
        val oppColor = opponentColor(color)
        var i = 1
        while (onChessboard(col + i, row + i) && freeSquare(col + i, row + i)) {
            moves.add(Move(col, row, col + i, row + i)); i++
        }
        if (onChessboard(col + i, row + i)) {
            if (board.fields[col + i][row + i]!!.color == oppColor)
                moves.add(Move(col, row, col + i, row + i))
        }

        i = 1
        while (onChessboard(col - i, row + i) && freeSquare(col - i, row + i)) {
            moves.add(Move(col, row, col - i, row + i)); i++
        }
        if (onChessboard(col - i, row + i)) {
            if (board.fields[col - i][row + i]!!.color == oppColor)
                moves.add(Move(col, row, col - i, row + i))
        }

        i = 1
        while (onChessboard(col + i, row - i) && freeSquare(col + i, row - i)) {
            moves.add(Move(col, row, col + i, row - i)); i++
        }
        if (onChessboard(col + i, row - i)) {
            if (board.fields[col + i][row - i]!!.color == oppColor)
                moves.add(Move(col, row, col + i, row - i))
        }

        i = 1
        while (onChessboard(col - i, row - i) && freeSquare(col - i, row - i)) {
            moves.add(Move(col, row, col - i, row - i)); i++
        }
        if (onChessboard(col - i, row - i)) {
            if (board.fields[col - i][row - i]!!.color == oppColor)
                moves.add(Move(col, row, col - i, row - i))
        }
    }


    private fun addRookMoves(col: Int, row: Int, color: PieceColor, moves: MutableList<Move>) {
        val oppColor = opponentColor(color)
        var i = 1
        while (onChessboard(col + i, row) && freeSquare(col + i, row)) {
            moves.add(Move(col, row, col + i, row)); i++
        }
        if (onChessboard(col + i, row)) {
            if (board.fields[col + i][row]!!.color == oppColor) {
                moves.add(Move(col, row, col + i, row))
            }
        }

        i = 1
        while (onChessboard(col - i, row) && freeSquare(col - i, row)) {
            moves.add(Move(col, row, col - i, row)); i++
        }
        if (onChessboard(col - i, row)) {
            if (board.fields[col - i][row]!!.color == oppColor) {
                moves.add(Move(col, row, col - i, row))
            }
        }

        i = 1
        while (onChessboard(col, row + i) && freeSquare(col, row + i)) {
            moves.add(Move(col, row, col, row + i)); i++
        }
        if (onChessboard(col, row + i)) {
            if (board.fields[col][row + i]!!.color === oppColor) {
                moves.add(Move(col, row, col, row + i))
            }
        }

        i = 1
        while (onChessboard(col, row - i) && freeSquare(col, row - i)) {
            moves.add(Move(col, row, col, row - i)); i++
        }
        if (onChessboard(col, row - i)) {
            if (board.fields[col][row - i]!!.color === oppColor) {
                moves.add(Move(col, row, col, row - i))
            }
        }
    }


    private fun addQueenMoves(col: Int, row: Int, color: PieceColor, moves: MutableList<Move>) {
        addRookMoves(col, row, color, moves)
        addBishopMoves(col, row, color, moves)
    }


    private fun addKingMoves(col: Int, row: Int, color: PieceColor, moves: MutableList<Move>) {
        addKingAttackingMoves(col, row, color, moves) // add 'normal' moves
        // add / handle castling moves
        if (color === PieceColor.WHITE) {
            if (board.whiteKingsideCastling
                && board.fields[7][0]?.color == PieceColor.WHITE
                && board.fields[7][0]?.type == PieceType.ROOK
                && freeSquare(5, 0)
                && freeSquare(6, 0)
            ) {
                if (!isSquareAttacked(4, 0)
                    && !isSquareAttacked(5, 0)
                    && !isSquareAttacked(6, 0)
                ) {
                    moves.add(CastlingMove(4, 0, 6, 0))
                }
            }
            if (board.whiteQueensideCastling
                && board.fields[0][0]?.color == PieceColor.WHITE
                && board.fields[0][0]?.type  == PieceType.ROOK
                && freeSquare(1, 0)
                && freeSquare(2, 0)
                && freeSquare(3, 0)
            ) {
                if (!isSquareAttacked(4, 0)
                    && !isSquareAttacked(2, 0)
                    && !isSquareAttacked(3, 0)
                ) {
                    moves.add(CastlingMove(4, 0, 2, 0))
                }
            }
        }
        else if (color === PieceColor.BLACK) {
            if (board.blackKingsideCastling
                && board.fields[7][7]?.color == PieceColor.BLACK
                && board.fields[7][7]?.type  == PieceType.ROOK
                && freeSquare(5, 7)
                && freeSquare(6, 7)
            ) {
                if (!isSquareAttacked(4, 7)
                    && !isSquareAttacked(5, 7)
                    && !isSquareAttacked(6, 7)
                ) {
                    moves.add(CastlingMove(4, 7, 6, 7))
                }
            }
            if (board.blackQueensideCastling
                && board.fields[0][7]?.color == PieceColor.BLACK
                && board.fields[0][7]?.type  == PieceType.ROOK
                && freeSquare(1, 7)
                && freeSquare(2, 7)
                && freeSquare(3, 7)
            ) {
                if (!isSquareAttacked(4, 7)
                    && !isSquareAttacked(2, 7)
                    && !isSquareAttacked(3, 7)
                ) {
                    moves.add(CastlingMove(4, 7, 2, 7))
                }
            }
        }
    }


    //--------------------------------- ATTACKING MOVES GENERATION ---------------------------------
    private fun addPawnAttackingMoves(col: Int, row: Int, color: PieceColor, moves: MutableList<Move>) {
        if (color == PieceColor.WHITE) {
            if (onChessboard(col + 1, row + 1)) {
                moves.add(Move(col, row, col + 1, row + 1))
            }
            if (onChessboard(col - 1, row + 1)) {
                moves.add(Move(col, row, col - 1, row + 1))
            }
        }
        else if (color == PieceColor.BLACK) {
            if (onChessboard(col + 1, row - 1)) {
                moves.add(Move(col, row, col + 1, row - 1))
            }
            if (onChessboard(col - 1, row - 1)) {
                moves.add(Move(col, row, col - 1, row - 1))
            }
        }
    }


    private fun addKingAttackingMoves(col: Int, row: Int, color: PieceColor, moves: MutableList<Move>) {
        val oppColor = opponentColor(color)
        if (onChessboard(col, row + 1) && freeOrColor(col, row + 1, oppColor)) {
            moves.add(Move(col, row, col, row + 1))
        }
        if (onChessboard(col, row - 1) && freeOrColor(col, row - 1, oppColor)) {
            moves.add(Move(col, row, col, row - 1))
        }
        if (onChessboard(col + 1, row) && freeOrColor(col + 1, row, oppColor)) {
            moves.add(Move(col, row, col + 1, row))
        }
        if (onChessboard(col - 1, row) && freeOrColor(col - 1, row, oppColor)) {
            moves.add(Move(col, row, col - 1, row))
        }
        if (onChessboard(col + 1, row + 1) && freeOrColor(col + 1, row + 1, oppColor)) {
            moves.add(Move(col, row, col + 1, row + 1))
        }
        if (onChessboard(col + 1, row - 1) && freeOrColor(col + 1, row - 1, oppColor)) {
            moves.add(Move(col, row, col + 1, row - 1))
        }
        if (onChessboard(col - 1, row + 1) && freeOrColor(col - 1, row + 1, oppColor)) {
            moves.add(Move(col, row, col - 1, row + 1))
        }
        if (onChessboard(col - 1, row - 1) && freeOrColor(col - 1, row - 1, oppColor)) {
            moves.add(Move(col, row, col - 1, row - 1))
        }
    }
}
