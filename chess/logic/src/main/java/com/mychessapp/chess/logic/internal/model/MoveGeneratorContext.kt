package com.mychessapp.chess.logic.internal.model

import com.mychessapp.chess.model.ChessBoard
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.PlayerColor

internal data class MoveGeneratorContext(
  val board: ChessBoard,
  val fromField: ChessField.WithPiece,
  val fromPosition: ChessFieldPosition,
  val activeColor: PlayerColor,
  val enPassantStatus: EnPassantStatus,
  val kingsPosition: Map<PlayerColor, ChessFieldPosition>,
  val castlingStatus: Map<PlayerColor, Set<CastlingData>>,
) {
  fun isOnChessBoard(position: ChessFieldPosition) = board.isOnChessBoard(position)
  fun isEmptyField(position: ChessFieldPosition) = board.getOrNull(position)?.isEmpty() ?: false
  fun canBeCaptured(position: ChessFieldPosition) = board.getOrNull(position)?.canBeCaptured() ?: false
  fun isEmptyOrToCapture(position: ChessFieldPosition) =
    board.getOrNull(position)?.isEmptyOrToCapture() ?: false
//  fun isEmptyOrToCapture(field: ChessField) = field.isEmptyOrToCapture()

  fun ChessFieldPosition.field() = board[this]
  fun ChessFieldPosition.fieldOrNull() = board.getOrNull(this)

  fun moveTo(target: ChessField) = ChessMove.Simple(fromField, target)
  fun moveTo(position: ChessFieldPosition) = moveTo(board[position])
  infix fun ChessField.WithPiece.moveTo(target: ChessField) = ChessMove.Simple(this, target)
  infix fun ChessField.WithPiece.moveTo(position: ChessFieldPosition) = this moveTo position.field()

  fun simpleMovesInDirection(
    direction: MoveDirection,
    moveRange: PieceMoveRange,
  ): List<ChessMove> {
    var captured = false
    return(1..moveRange.range)
      .mapNotNull { steps -> fromField.moved(direction, steps) }
      .takeWhile { field ->
        when {
          field.isEmpty() && !captured -> true
          field.canBeCaptured() && !captured -> true.also { captured = true }
          else -> false
        }
      }
      .map(::moveTo)
  }

  fun firstPieceFieldInDirection(direction: MoveDirection): ChessField.WithPiece? {
    var position = fromField.position.moved(direction, steps = 1)
    while (board[position] !is ChessField.WithPiece) {
      if (!isOnChessBoard(position)) return null
      position = position.moved(direction, steps = 1)
    }
    return board[position] as? ChessField.WithPiece
  }

  private fun ChessField.moved(direction: MoveDirection, steps: Int): ChessField? =
    board[position.moved(direction, steps)]

  private fun ChessField.isEmpty() = this is ChessField.Empty

  fun ChessField.isEmptyOrToCapture() = when (this) {
    is ChessField.Empty -> true
    is ChessField.WithPiece -> piece.color != activeColor
  }

  fun ChessField.canBeCaptured() = when (this) {
    is ChessField.Empty -> false
    is ChessField.WithPiece -> piece.color != fromField.piece.color
  }
}
