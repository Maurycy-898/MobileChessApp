package com.mychessapp.chess.logic.internal.model

import com.mychessapp.chess.model.ChessBoard
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.PlayerColor
import com.mychessapp.core.common.scope_functions.takeIfIsInstance

internal data class MoveGeneratorContext(
  val board: ChessBoard,
  val fromField: ChessField.WithPiece,
  val fromPosition: ChessFieldPosition,
  val activeColor: PlayerColor,
  val enPassantStatus: EnPassantStatus,
  val kingsPosition: Map<PlayerColor, ChessFieldPosition>,
  val castlingStatus: Map<PlayerColor, Set<CastlingData>>,
) {

  fun isEmptyField(position: ChessFieldPosition) = board[position] is ChessField.Empty

  fun isOnChessBoard(position: ChessFieldPosition) = board.isOnChessBoard(position)

  fun canBeCaptured(position: ChessFieldPosition) = board[position].canBeCaptured()

  fun isEmptyOrToCapture(position: ChessFieldPosition) = board[position].isEmptyOrToCapture()

  fun moveTo(position: ChessFieldPosition) =
    ChessMove.Simple(
      from = fromField,
      to = board[position]
    )

  infix fun ChessField.WithPiece.moveTo(target: ChessFieldPosition) =
    ChessMove.Simple(this, board[target])

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

  fun firstOrNullInDirection(
    direction: MoveDirection,
    condition: (ChessField) -> Boolean,
  ): ChessField? {
    var currTargetPosition = fromField.position.moved(direction steps 1)
    while (!condition(board[currTargetPosition])) {
      currTargetPosition = currTargetPosition.moved(direction steps 1)
      if (!isOnChessBoard(currTargetPosition)) return null
    }
    return board[currTargetPosition]
  }

  private fun isEmptyAndOnChessBoard(position: ChessFieldPosition) =
    isEmptyField(position) and isOnChessBoard(position)

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
