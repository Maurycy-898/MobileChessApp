package com.mychessapp.chess.ui.preview

import com.mychessapp.chess.model.ChessBoard
import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.ChessPiece
import com.mychessapp.chess.model.ChessPieceType
import com.mychessapp.chess.model.PlayerColor
import com.mychessapp.chess.model.copy
import com.mychessapp.chess.ui.model.ChessBoardUiState
import com.mychessapp.chess.ui.model.ChessFieldMark
import com.mychessapp.chess.ui.model.ChessFieldUiState
import com.mychessapp.core.common.collections.matrix.mapFields

object Preview {
  val exampleChessPiece = ChessPiece(
    type = ChessPieceType.King,
    color = PlayerColor.Black
  )

  private val exampleChessFieldPosition = ChessFieldPosition(row = 1, column = 2)

  val exampleChessField = ChessField.WithPiece(
    position = exampleChessFieldPosition,
    piece = exampleChessPiece
  )

  val exampleChessBoard = ChessBoardUiState(
    fields = ChessBoard
      .empty(4)
      .apply {
        get(exampleChessFieldPosition).apply {
          set(exampleChessFieldPosition, copy(piece = exampleChessPiece))
        }
      }
      .mapFields { field ->
        ChessFieldUiState(
          field = field,
          mark = ChessFieldMark.None
        )
      }
  )
}
