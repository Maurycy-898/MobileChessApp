package com.mychessapp.chess.ui.model

import com.mychessapp.chess.model.ChessField

enum class ChessFieldColor {
  Dark,
  Light
}

internal val ChessField.color: ChessFieldColor
  get() = when ((position.row + position.column) % 2 == 0) {
    true -> ChessFieldColor.Dark
    false -> ChessFieldColor.Light
  }
