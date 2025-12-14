package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.MoveDirection
import com.mychessapp.chess.logic.internal.model.MoveDirection.Left
import com.mychessapp.chess.logic.internal.model.MoveDirection.Right
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.PieceMoveGenerator
import com.mychessapp.chess.logic.internal.model.moved
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.ChessPiece
import com.mychessapp.chess.model.ChessPieceType
import com.mychessapp.chess.model.PlayerColor
import com.mychessapp.chess.model.promotionPieceTypes
import javax.inject.Inject

internal class PawnMovesGenerator @Inject constructor() : PieceMoveGenerator {
  override fun generate(context: MoveGeneratorContext): List<ChessMove> = with(context) {
    listOfNotNull(
      fromPosition.moved(direction, steps = 1).takeIf { isEmptyField(it) },
      fromPosition.moved(direction, steps = 2).takeIf { canMoveTwoSteps() },
    )
      .mapNotNull(board::getOrNull)
      .flatMap(::toChessMoves) + generateAttackingMoves(context)
  }

  private fun generateAttackingMoves(context: MoveGeneratorContext): List<ChessMove> =
    with(context) {
      val inFrontPosition = fromPosition.moved(direction, steps = 1)
      listOfNotNull(
        inFrontPosition.moved(Right, steps = 1),
        inFrontPosition.moved(Left, steps = 1),
      )
        .mapNotNull(board::getOrNull)
        .filter { it.canBeCaptured() }
        .flatMap(::toChessMoves)
    }
}

private val MoveGeneratorContext.direction: MoveDirection
  get() = when (activeColor) {
    PlayerColor.White -> MoveDirection.Up
    PlayerColor.Black -> MoveDirection.Down
  }

private fun MoveGeneratorContext.toChessMoves(target: ChessField): List<ChessMove> =
  when (isPromotionTarget(target.position)) {
    true -> promotionMovesTo(target)
    false -> listOf(moveTo(target))
  }

private fun MoveGeneratorContext.canMoveTwoSteps() =
  isInStartingRow() && haveEmptyFieldsInFront(2)

private fun MoveGeneratorContext.isInStartingRow() = when (activeColor) {
  PlayerColor.White -> fromPosition.row == 1
  PlayerColor.Black -> fromPosition.row == (board.rowsSize - 2)
}

private fun MoveGeneratorContext.haveEmptyFieldsInFront(number: Int) =
  (1..number)
    .map { steps -> fromPosition.moved(direction, steps) }
    .all { isOnChessBoard(it) && isEmptyField(it) }

private fun MoveGeneratorContext.isPromotionTarget(target: ChessFieldPosition) =
  when (activeColor) {
    PlayerColor.White -> target.row == (board.rowsSize - 1)
    PlayerColor.Black -> target.row == 0
  }

private fun MoveGeneratorContext.promotionMovesTo(target: ChessField) =
  promotionPieceTypes.map { type -> promotionMoveTo(target, type) }

private fun MoveGeneratorContext.promotionMoveTo(
  target: ChessField,
  type: ChessPieceType,
) = ChessMove.Promotion(
  from = fromField,
  to = target,
  promotedPiece = ChessPiece(activeColor, type)
)
