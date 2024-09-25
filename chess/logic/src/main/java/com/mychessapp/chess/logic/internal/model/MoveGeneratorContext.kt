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
  val kingsStatus: Map<PlayerColor, KingStatus>,
  val attackedFields: Set<ChessFieldPosition>,
) {
  fun isEmptyField(position: ChessFieldPosition) = board[position] is ChessField.Empty
  fun isOnChessBoard(position: ChessFieldPosition) = board.run { position.isOnChessBoard() }
  fun canBeCaptured(position: ChessFieldPosition) = board[position].canBeCaptured()
  fun isEmptyOrToCapture(position: ChessFieldPosition) = board[position].isEmptyOrToCapture()

  private fun isEmptyAndOnChessBoard(position: ChessFieldPosition) =
    isEmptyField(position) and isOnChessBoard(position)

  fun moveTo(position: ChessFieldPosition) = moveTo(board[position])

  private fun moveTo(field: ChessField) =
    when (field) {
      is ChessField.Empty -> ChessMove.SimpleMove(fromField, field)
      is ChessField.WithPiece -> {
        if (field.piece.color != activeColor) ChessMove.CaptureMove(fromField, field)
        else throw IllegalStateException("Can't capture your own pieces!")
      }
    }

  fun getMovesInDirection(
    direction: MoveDirection,
    moveRange: PieceMoveRange,
  ) = buildList {
    var currTargetPosition = fromField.position.moved(direction steps 1)
    while (isEmptyAndOnChessBoard(currTargetPosition)) {
      add(moveTo(currTargetPosition)).also {
        if (inMoveRange(moveRange)) return@buildList
      }
      currTargetPosition = currTargetPosition.moved(direction steps 1)
    }
    if (isEmptyOrToCapture(currTargetPosition) and inMoveRange(moveRange)) {
      add(moveTo(currTargetPosition))
    }
  }

  private fun ChessField.isEmptyOrToCapture() = when (this) {
    is ChessField.Empty -> true
    is ChessField.WithPiece -> piece.color != activeColor
  }

  private fun ChessField.canBeCaptured() =
    when (this) {
      is ChessField.Empty -> false
      is ChessField.WithPiece -> piece.color != fromField.piece.color
    }
}
