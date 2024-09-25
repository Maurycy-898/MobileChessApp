package com.mychessapp.chess.logic.internal.move_generators

import com.mychessapp.chess.logic.internal.model.MoveGeneratorContext
import com.mychessapp.chess.logic.internal.model.moved
import javax.inject.Inject

internal class KnightMovesGenerator @Inject constructor() {

  fun generateMoves(context: MoveGeneratorContext) =
    with(context) {
      listOf(
        fromPosition.moved(up = 2, left = 1),
        fromPosition.moved(down = 2, left = 1),
        fromPosition.moved(up = 2, right = 1),
        fromPosition.moved(down = 2, right = 1),
      )
        .filter(::isOnChessBoard)
        .filter(::isEmptyOrToCapture)
        .map(::moveTo)
    }
}
