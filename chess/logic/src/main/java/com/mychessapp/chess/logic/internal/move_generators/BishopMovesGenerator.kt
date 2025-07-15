package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.MoveDirection
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.PieceMoveRange
import com.mychessapp.core.common.numbers.infinity
import javax.inject.Inject

internal class BishopMovesGenerator @Inject constructor() {

  fun generateMoves(context: MoveGeneratorContext) = with(context) {
    bishopMoveDirections.flatMap { direction ->
      getMovesInDirection(direction, bishopMoveRange)
    }
  }
}

private val bishopMoveRange = PieceMoveRange(Int.infinity)

private val bishopMoveDirections = listOf(
  MoveDirection.UpRight,
  MoveDirection.DownLeft,
  MoveDirection.UpLeft,
  MoveDirection.DownRight
)
