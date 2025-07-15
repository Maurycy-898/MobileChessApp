package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.MoveDirection
import com.mychessapp.chess.logic.internal.model.MoveDirection.Left
import com.mychessapp.chess.logic.internal.model.MoveDirection.Right
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.moved
import com.mychessapp.chess.logic.internal.model.steps
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.ChessPiece
import com.mychessapp.chess.model.ChessPieceType
import com.mychessapp.chess.model.PlayerColor
import com.mychessapp.chess.model.promotionPieceTypes
import com.mychessapp.core.common.numbers.two
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class PawnMovesGenerator @Inject constructor() {

  fun generateMoves(context: MoveGeneratorContext) = with(context) {
    listOfNotNull(
      fromPosition.moved((direction steps 1)).takeIf(::isEmptyField),
      fromPosition.moved((direction steps 2)).takeIf(::canMoveTwoSteps),
    ).flatMap(::toChessMove) + generateAttackingMoves(context)
  }

  private fun generateAttackingMoves(context: MoveGeneratorContext) = with(context) {
    listOfNotNull(
      fromPosition.moved((direction steps 1), (Right steps 1)).takeIf(::canBeCaptured),
      fromPosition.moved((direction steps 1), (Left steps 1)).takeIf(::canBeCaptured),
    ).flatMap(::toChessMove)
  }
}

private val MoveGeneratorContext.direction
  get() = when (activeColor) {
    PlayerColor.White -> MoveDirection.Up
    PlayerColor.Black -> MoveDirection.Down
  }

private fun MoveGeneratorContext.toChessMove(targetPosition: ChessFieldPosition) =
  when (isPromotionTarget(targetPosition)) {
    true -> promotionMovesTo(targetPosition)
    false -> listOf(moveTo(targetPosition))
  }

private fun MoveGeneratorContext.canMoveTwoSteps(position: ChessFieldPosition) =
  when (activeColor) {
    PlayerColor.White -> (position.row == 1) and haveEmptyFieldsInFront(Int.two)
    PlayerColor.Black -> (position.row == board.rowsSize - 2) and haveEmptyFieldsInFront(Int.two)
  }

private fun MoveGeneratorContext.haveEmptyFieldsInFront(number: Int) = with(board) {
  var position = fromPosition
  repeat(number) {
    position = position.moved(direction steps 1)
    if (position.isNotEmptyField()) return@with false
  }
  return@with true
}

private fun MoveGeneratorContext.isPromotionTarget(position: ChessFieldPosition) = with(position) {
  when (activeColor) {
    PlayerColor.White -> row == (board.rowsSize - 1)
    PlayerColor.Black -> row == 0
  }
}

private fun MoveGeneratorContext.promotionMovesTo(targetPosition: ChessFieldPosition) =
  promotionPieceTypes.map { type ->
    promotionMoveTo(targetPosition, type)
  }

private fun MoveGeneratorContext.promotionMoveTo(
  position: ChessFieldPosition,
  type: ChessPieceType,
) = ChessMove.Promotion(
  from = fromField,
  to = board[position],
  promotedPiece = ChessPiece(activeColor, type)
)
