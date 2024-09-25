package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.MoveDirection
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.PieceMoveRange
import com.mychessapp.core.common.numbers.infinity
import javax.inject.Inject

private val rookMoveRange = PieceMoveRange(Int.infinity)

private val rookMoveDirections = listOf(
  MoveDirection.Up,
  MoveDirection.Down,
  MoveDirection.Right,
  MoveDirection.Left
)

internal class RookMovesGenerator @Inject constructor() {
  fun generateMoves(context: MoveGeneratorContext) =
    with(context) {
      rookMoveDirections.flatMap { direction ->
        getMovesInDirection(direction, rookMoveRange)
      }
    }
}
