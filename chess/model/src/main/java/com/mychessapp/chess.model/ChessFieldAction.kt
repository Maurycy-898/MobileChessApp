package com.mychessapp.chess.model

sealed class ChessFieldAction {

  data class SelectPiece(val fieldWithPiece: ChessFieldPosition) : ChessFieldAction()

  data object UnselectPiece : ChessFieldAction()

  data class MakeMove(val start: ChessFieldPosition, val destination: ChessFieldPosition) :
    ChessFieldAction()
}
