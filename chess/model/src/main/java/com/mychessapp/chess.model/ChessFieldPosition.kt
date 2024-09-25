package com.mychessapp.chess.model

data class ChessFieldPosition(val row: Int, val column: Int)

fun ChessFieldPosition.toChessFieldNotation() =
  when (column) {
    1 -> "a"
    2 -> "b"
    3 -> "c"
    4 -> "d"
    5 -> "e"
    6 -> "f"
    7 -> "g"
    8 -> "h"
    else -> "unknown"
  } + row.toString()