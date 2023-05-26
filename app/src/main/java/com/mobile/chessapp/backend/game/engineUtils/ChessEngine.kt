package com.mobile.chessapp.backend.game.engineUtils

import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.engineUtils.Evaluator.evaluate
import com.mobile.chessapp.backend.game.moveUtils.ChessMove
import com.mobile.chessapp.backend.game.moveUtils.MoveGenerator

data class Evaluation(val bestMove: ChessMove?, val positionScore: Int)

object ChessEngine {
    fun findBestMove(board: ChessBoard, depth: Int = 3) : Evaluation {
        return alfaBeta(board, alfa = LOSING_SCORE, beta = WINNING_SCORE, depth)
    }

    private fun alfaBeta(board: ChessBoard, alfa: Int, beta: Int, depth: Int) : Evaluation {
        if (depth == 0) {
            return Evaluation(bestMove = null, (board.activeColor.colorFactor*evaluate(board)))
        }
        var score: Int
        var bestScore = Int.MIN_VALUE
        var bestMove: ChessMove? = null

        val moves: List<ChessMove> = MoveGenerator.generateMoves(board)
        if (moves.isEmpty()) {
            if (board.blackKingAttacked) return Evaluation(bestMove = null, LOSING_SCORE)
            if (board.whiteKingAttacked) return Evaluation(bestMove = null, WINNING_SCORE)
            return Evaluation(bestMove = null, DRAWING_SCORE)
        }

        var currAlfa = alfa
        for (move in moves) {
            // move
            board.doMove(move)
            // evaluate
            score = -alfaBeta(board, -beta, -currAlfa, (depth - 1)).positionScore
            // undo move
            board.undoMove(move)

            if (score >= beta) {
                return Evaluation(move, beta)
            }
            if (score > bestScore) {
                bestMove = move
                bestScore = score
                if (score > currAlfa) {
                    currAlfa = score
                }
            }
        }
        return Evaluation(bestMove, bestScore)
    }
}
