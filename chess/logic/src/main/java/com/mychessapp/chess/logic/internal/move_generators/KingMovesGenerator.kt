package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.MoveDirection
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.PieceMoveRange
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessPiece
import com.mychessapp.chess.model.ChessPieceType.Rook
import com.mychessapp.core.common.numbers.one
import javax.inject.Inject

internal class KingMovesGenerator @Inject constructor() {
  fun generateMoves(context: MoveGeneratorContext) = with(context) {
    generateAttackingMoves() + generateCastlingMoves()
  }

  private fun MoveGeneratorContext.generateAttackingMoves() =
    kingMoveDirections.flatMap { direction -> getMovesInDirection(direction, kingMoveRange) }

  private fun MoveGeneratorContext.generateCastlingMoves(): List<ChessMove> =
    listOfNotNull(
      generateCastlingMove(MoveDirection.Left),
      generateCastlingMove(MoveDirection.Right),
    )
}

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

internal fun MoveGeneratorContext.generateCastlingMove(direction: MoveDirection): ChessMove? {
  val rookField = firstOrNullInDirection(direction) {
    it is ChessField.WithPiece && it.piece == ChessPiece(activeColor, Rook)
  } as? ChessField.WithPiece ?: return null

  val castling = castlingStatus[activeColor]
    ?.firstOrNull { it.rookStartingPosition == rookField.position }
    ?: return null

  val rookMove = rookField moveTo castling.rookTargetPosition

  val kingField = board[fromPosition] as? ChessField.WithPiece ?: return null
  val targetField = board[castling.kingTargetPosition] as? ChessField.Empty ?: return null

  return ChessMove.Castling(
    from = kingField,
    to = targetField,
    rookMove = rookMove,
  )
}
