package com.mychessapp.chess.model

data class ChessPiece(
  val color: PlayerColor,
  val type: ChessPieceType
)

enum class PieceNotationOption {
  UnicodeGraphic, Text
}

fun ChessPiece.toChessNotation(option: PieceNotationOption) =
  when (option) {
    PieceNotationOption.Text -> toChessNotationString()
    PieceNotationOption.UnicodeGraphic -> toChessNotationUnicodeGraphic()
  }

private fun ChessPiece.toChessNotationUnicodeGraphic() =
  when (color) {
    PlayerColor.White -> when (type) {
      ChessPieceType.Pawn -> "♙"
      ChessPieceType.Knight -> "♘"
      ChessPieceType.Bishop -> "♗"
      ChessPieceType.Rook -> "♖"
      ChessPieceType.Queen -> "♕"
      ChessPieceType.King -> "♔"
    }

    PlayerColor.Black -> when (type) {
      ChessPieceType.Pawn -> "♟"
      ChessPieceType.Knight -> "♞"
      ChessPieceType.Bishop -> "♝"
      ChessPieceType.Rook -> "♜"
      ChessPieceType.Queen -> "♛"
      ChessPieceType.King -> "♚"
    }
  }

private fun ChessPiece.toChessNotationString() =
  when (type) {
    ChessPieceType.Pawn -> "P"
    ChessPieceType.Knight -> "N"
    ChessPieceType.Bishop -> "B"
    ChessPieceType.Rook -> "R"
    ChessPieceType.Queen -> "Q"
    ChessPieceType.King -> "K"
  }
