package com.mychessapp.chess.logic.internal.model

import com.mychessapp.chess.model.ChessFieldPosition

internal enum class MoveDirection {
  Left,
  Right,
  Up,
  Down,
  UpRight,
  UpLeft,
  DownRight,
  DownLeft,
}

internal data class PositionShift(val direction: MoveDirection, val steps: Int)

internal fun <T> MutableList<T>.inMoveRange(moveRange: PieceMoveRange) = size < moveRange.range

internal fun ChessFieldPosition.moved(
  up: Int = 0,
  down: Int = 0,
  right: Int = 0,
  left: Int = 0,
) = ChessFieldPosition(
  row = row - up + down,
  column = column - left + right
)

internal fun ChessFieldPosition.moved(
  vararg shifts: PositionShift,
): ChessFieldPosition {
  var currPosition = this
  shifts.forEach { currPosition = currPosition.moved(it) }
  return currPosition
}

internal infix fun MoveDirection.steps(steps: Int) = PositionShift(this, steps)

internal fun ChessFieldPosition.moved(direction: MoveDirection, steps: Int) = when (direction) {
  MoveDirection.Down -> moved(down = steps)
  MoveDirection.Left -> moved(left = steps)
  MoveDirection.Up -> moved(up = steps)
  MoveDirection.Right -> moved(right = steps)
  MoveDirection.DownLeft -> moved(down = steps, left = steps)
  MoveDirection.DownRight -> moved(down = steps, right = steps)
  MoveDirection.UpLeft -> moved(up = steps, left = steps)
  MoveDirection.UpRight -> moved(up = steps, right = steps)
}

internal fun ChessFieldPosition.moved(shift: PositionShift) = with(shift) {
  moved(direction, steps)
}
