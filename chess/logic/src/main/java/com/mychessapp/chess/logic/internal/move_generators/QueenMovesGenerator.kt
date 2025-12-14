package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.PieceMoveGenerator
import javax.inject.Inject

internal class QueenMovesGenerator @Inject constructor(
  private val bishopMovesGenerator: BishopMovesGenerator,
  private val rookMovesGenerator: RookMovesGenerator,
) : PieceMoveGenerator {
  override fun generate(context: MoveGeneratorContext): List<ChessMove> =
    bishopMovesGenerator.generate(context) + rookMovesGenerator.generate(context)
}
