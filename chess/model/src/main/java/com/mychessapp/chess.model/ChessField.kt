package com.mychessapp.chess.model

sealed class ChessField(
  open val position: ChessFieldPosition,
) {
  data class Empty(
    override val position: ChessFieldPosition,
  ) : ChessField(position)

  data class WithPiece(
    override val position: ChessFieldPosition,
    val piece: ChessPiece
  ) : ChessField(position)
}

fun ChessField.toChessNotation(
  option: PieceNotationOption = PieceNotationOption.Text
) =
  when (this) {
    is ChessField.Empty -> position.toChessFieldNotation()
    is ChessField.WithPiece -> piece.toChessNotation(option) + position.toChessFieldNotation()
  }
