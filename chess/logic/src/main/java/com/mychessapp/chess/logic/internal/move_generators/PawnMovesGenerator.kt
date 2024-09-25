package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.MoveDirection
import com.mychessapp.chess.logic.internal.model.MoveDirection.Right
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.moved
import com.mychessapp.chess.logic.internal.model.steps
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.ChessPiece
import com.mychessapp.chess.model.PlayerColor
import com.mychessapp.chess.model.promotionPieceTypes
import com.mychessapp.core.common.numbers.two
import javax.inject.Inject

internal class PawnMovesGenerator @Inject constructor() {

  fun generateMoves(context: MoveGeneratorContext) =
    with(context) {
      listOfNotNull(
        fromPosition.moved((direction steps 1)).takeIf(::isEmptyField),
        fromPosition.moved((direction steps 2)).takeIf { canMoveTwoSteps() },
        fromPosition.moved((direction steps 1), (Right steps 1)).takeIf(::canBeCaptured),
        fromPosition.moved((direction steps 1)).moved(left = 1).takeIf(::canBeCaptured),
      ).map(::moveTo)
    }

  fun MoveGeneratorContext.generateAttackingMoves() =
    listOfNotNull<String>()
}

private val MoveGeneratorContext.direction
  get() = when (activeColor) {
    PlayerColor.White -> MoveDirection.Up
    PlayerColor.Black -> MoveDirection.Down
  }

private fun MoveGeneratorContext.toChessMove(targetPosition: ChessFieldPosition) =
  when (targetPosition.isPromotionTarget()) {
    true -> promotionMovesTo(targetPosition)
    false -> listOf(moveTo(targetPosition))
  }

private fun MoveGeneratorContext.canMoveTwoSteps() = when (activeColor) {
  PlayerColor.White -> (fromPosition.row == 1) and haveEmptyFieldsInFront(Int.two)
  PlayerColor.Black -> (fromPosition.row == board.rowsSize - 2) and haveEmptyFieldsInFront(Int.two)
}

private fun MoveGeneratorContext.haveEmptyFieldsInFront(number: Int) = with(board) {
  var position = fromPosition
  repeat(number) {
    position = position.moved(direction steps 1)
    if (position.isEmptyField().not()) return@with false
  }
  return@with true
}

context(MoveGeneratorContext)
private fun ChessFieldPosition.isPromotionTarget() =
  when (activeColor) {
    PlayerColor.White -> row == (board.rowsSize - 1)
    PlayerColor.Black -> row == 0
  }

context(MoveGeneratorContext)
private fun promotionMovesTo(targetPosition: ChessFieldPosition) =
  promotionPieceTypes.map { type ->
    moveTo(targetPosition)
  }
