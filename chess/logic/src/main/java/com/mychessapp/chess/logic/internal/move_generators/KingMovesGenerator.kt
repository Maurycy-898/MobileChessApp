package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.MoveDirection
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.PieceMoveGenerator
import com.mychessapp.chess.logic.internal.model.PieceMoveRange
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessPieceType.Rook
import javax.inject.Inject

internal class KingMovesGenerator @Inject constructor() : PieceMoveGenerator {
  override fun generate(context: MoveGeneratorContext): List<ChessMove> =
    with(context) {
      generateAttackingMoves() + generateCastlingMoves()
    }

  private fun MoveGeneratorContext.generateAttackingMoves(): List<ChessMove> =
    kingMoveDirections.flatMap { direction -> simpleMovesInDirection(direction, kingMoveRange) }

  private fun MoveGeneratorContext.generateCastlingMoves(): List<ChessMove> =
    listOfNotNull(
      generateCastlingMove(MoveDirection.Left),
      generateCastlingMove(MoveDirection.Right),
    )
}

private val kingMoveRange = PieceMoveRange(range = 1)
private val kingMoveDirections = MoveDirection.entries // can move in all directions

internal fun MoveGeneratorContext.generateCastlingMove(direction: MoveDirection): ChessMove? {
  val rookField = firstPieceFieldInDirection(direction)
    ?.takeIf { it.piece.color == activeColor && it.piece.type == Rook }
    ?: return null

  val castling = castlingStatus[activeColor]
    ?.firstOrNull { it.rookStartingPosition == rookField.position }
    ?: return null

  val rookMove = rookField moveTo castling.rookTargetPosition.field()
  val kingField = board[fromPosition] as? ChessField.WithPiece ?: return null
  val targetField = board[castling.kingTargetPosition] as? ChessField.Empty ?: return null

  return ChessMove.Castling(
    from = kingField,
    to = targetField,
    rookMove = rookMove,
  )
}
