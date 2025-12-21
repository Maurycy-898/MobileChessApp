package com.mobile.chessapp.backend.game.engineUtils

import com.mobile.chessapp.backend.game.boardUtils.ChessBoard
import com.mobile.chessapp.backend.game.engineUtils.Evaluator.evaluate
import com.mobile.chessapp.backend.game.moveUtils.ChessMove
import com.mobile.chessapp.backend.game.moveUtils.MoveGenerator

data class Evaluation(val bestMove: ChessMove?, val positionScore: Int)

object ChessEngine {
  fun findBestMove(board: ChessBoard, depth: Int = 3): Evaluation {
    return board.alfaBeta(alfa = LOSING_SCORE, beta = WINNING_SCORE, depth)
  }

  private fun ChessBoard.alfaBeta(
    alfa: Int,
    beta: Int,
    depth: Int
  ): Evaluation {


    if (depth == 0) {
      return Evaluation(null, (colorFactor * evaluate(this)))
    }

    var score: Int
    var bestScore = Int.MIN_VALUE
    var bestMove: ChessMove? = null

    val moves = MoveGenerator.generateMoves(this)

    if (moves.isEmpty()) return when {
      blackKingAttacked -> colorFactor * LOSING_SCORE
      whiteKingAttacked -> colorFactor * WINNING_SCORE
      else -> DRAWING_SCORE
    }.let { Evaluation(null, it) }


    var currAlfa = alfa
    for (move in moves) {

      doMove(move)
      score = -this.alfaBeta(-beta, -currAlfa).positionScore
      undoMove(move)

      if (score >= beta) return Evaluation(move, beta)

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

private val ChessBoard.colorFactor get() = activeColor.colorFactor
