package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.PieceMoveGenerator
import com.mychessapp.chess.logic.internal.model.moved
import javax.inject.Inject

internal class KnightMovesGenerator @Inject constructor() : PieceMoveGenerator {
  override fun generate(context: MoveGeneratorContext): List<ChessMove.Simple> =
    with(context) {
      listOf(
        fromPosition.moved(up = 2, left = 1),
        fromPosition.moved(down = 2, left = 1),
        fromPosition.moved(up = 2, right = 1),
        fromPosition.moved(down = 2, right = 1),
      )
        .mapNotNull(board::getOrNull)
        .filter { it.isEmptyOrToCapture() }
        .map(::moveTo)
    }
}
