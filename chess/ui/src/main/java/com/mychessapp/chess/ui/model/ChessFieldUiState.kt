package com.mychessapp.chess.ui.model

import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.ChessPiece

data class ChessFieldUiState(
  val field: ChessField,
  val mark: ChessFieldMark
)

val ChessFieldUiState.position: ChessFieldPosition
  get() = field.position
