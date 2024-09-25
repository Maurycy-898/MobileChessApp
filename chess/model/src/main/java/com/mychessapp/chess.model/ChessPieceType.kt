package com.mychessapp.chess.model

enum class ChessPieceType {
  Pawn,
  Knight,
  Bishop,
  Rook,
  Queen,
  King,
}

val promotionPieceTypes
  get() = listOf(
    ChessPieceType.Knight,
    ChessPieceType.Bishop,
    ChessPieceType.Rook,
    ChessPieceType.Queen,
  )
