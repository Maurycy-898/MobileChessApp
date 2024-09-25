package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.MoveDirection
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.PieceMoveRange
import com.mychessapp.core.common.numbers.one
import javax.inject.Inject

private val kingMoveRange = PieceMoveRange(Int.one)

private val kingMoveDirections = listOf(
  MoveDirection.Down,
  MoveDirection.Left,
  MoveDirection.Up,
  MoveDirection.Right,
  MoveDirection.DownLeft,
  MoveDirection.DownRight,
  MoveDirection.UpLeft,
  MoveDirection.UpRight,
)

internal class KingMovesGenerator @Inject constructor() {

  fun generateMoves(context: MoveGeneratorContext) = with(context) {
    generateAttackingMoves() + generateCastlingMoves()
  }

  fun MoveGeneratorContext.generateAttackingMoves() =
    kingMoveDirections.flatMap { direction ->
      getMovesInDirection(direction, kingMoveRange)
    }

  private fun MoveGeneratorContext.generateCastlingMoves(): List<ChessMove> =
    listOfNotNull(
      generateCastlingMove(MoveDirection.Left),
      generateCastlingMove(MoveDirection.Right),
    )
}

fun MoveGeneratorContext.generateCastlingMove(direction: MoveDirection): ChessMove? {
  val rookPosition = getRookPosition(direction)
  val kingPosition = fromPosition
  val rook = board[rookPosition]
  val king = board[kingPosition]

  if (rook?.isRook(activeColor) == true && king?.isKing(activeColor) == true) {
    val rookDirection = direction.opposite()
    val rookMove = getMovesInDirection(rookDirection, PieceMoveRange(Int.one)).firstOrNull()
    val kingMove = getMovesInDirection(direction, PieceMoveRange(Int.one)).firstOrNull()
    if (rookMove != null && kingMove != null) {
      return ChessMove(kingPosition, kingMove, rookPosition, rookMove)
    }
  }
  return null
}
