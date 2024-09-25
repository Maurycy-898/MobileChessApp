package com.mychessapp.chess.ui.model

import com.mychessapp.core.common.collections.matrix.Matrix

data class ChessBoardUiState(
  val fields: Matrix<ChessFieldUiState>,
) : Matrix<ChessFieldUiState> by fields
