package com.mychessapp.chess.model

sealed class ChessFieldClickedAction {

  data class SelectPiece(val fieldWithPiece: ChessFieldPosition) : ChessFieldClickedAction()

  data object UnselectPiece : ChessFieldClickedAction()

  data class MakeMove(val start: ChessFieldPosition, val destination: ChessFieldPosition) :
    ChessFieldClickedAction()
}
