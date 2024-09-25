package com.mychessapp.chess.logic.internal.model

import com.mychessapp.chess.model.ChessField
import com.mychessapp.chess.model.ChessPiece

sealed class ChessMove(
  open val from: ChessField.WithPiece,
  open val to: ChessField,
) {
  data class SimpleMove(
    override val from: ChessField.WithPiece,
    override val to: ChessField,
  ) : ChessMove(from, to)

  data class CaptureMove(
    override val from: ChessField.WithPiece,
    override val to: ChessField.WithPiece,
  ) : ChessMove(from, to)

  data class EnPassantMove(
    override val from: ChessField.WithPiece,
    override val to: ChessField,
    val capturedPiece: ChessPiece
  ) : ChessMove(from, to)

  data class CastlingMove(
    override val from: ChessField.WithPiece,
    override val to: ChessField,
    val rookMove: SimpleMove
  ) : ChessMove(from, to)

  data class PromotionMove(
    override val from: ChessField.WithPiece,
    override val to: ChessField,
    val promotedPiece: ChessPiece
  ) : ChessMove(from, to)
}
