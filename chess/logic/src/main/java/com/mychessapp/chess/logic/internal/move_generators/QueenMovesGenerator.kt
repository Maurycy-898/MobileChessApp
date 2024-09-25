package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import javax.inject.Inject

internal class QueenMovesGenerator @Inject constructor(
  private val bishopMovesGenerator: BishopMovesGenerator,
  private val rookMovesGenerator: RookMovesGenerator,
) {
  fun generateMoves(context: MoveGeneratorContext) = with(context) {
    bishopMoves() + rookMoves()
  }

  private fun MoveGeneratorContext.bishopMoves() =
    bishopMovesGenerator.generateMoves(this)

  private fun MoveGeneratorContext.rookMoves() =
    rookMovesGenerator.generateMoves(this)
}
