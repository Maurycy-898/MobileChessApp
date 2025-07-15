package com.mychessapp.chess.logic.internal.model

import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessPiece

sealed class ChessMove(
  open val from: ChessField.WithPiece,
  open val to: ChessField,
) {
  data class Simple(
    override val from: ChessField.WithPiece,
    override val to: ChessField,
  ) : ChessMove(from, to)

  data class EnPassant(
    override val from: ChessField.WithPiece,
    override val to: ChessField.Empty,
    val capturedPiece: ChessPiece
  ) : ChessMove(from, to)

  data class Castling(
    override val from: ChessField.WithPiece,
    override val to: ChessField.Empty,
    val rookMove: Simple
  ) : ChessMove(from, to)

  data class Promotion(
    override val from: ChessField.WithPiece,
    override val to: ChessField,
    val promotedPiece: ChessPiece
  ) : ChessMove(from, to)

  fun isCapture() = this is EnPassant || to is ChessField.WithPiece
}
