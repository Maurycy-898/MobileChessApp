package com.mychessapp.chess.logic

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.GameMetadata
import com.mychessapp.chess.model.ChessBoard
import com.mychessapp.chess.model.ChessField

interface MovesGenerator {

  fun generateAllLegalMoves(
    board: ChessBoard,
    metadata: GameMetadata
  ): List<ChessMove>

  fun generateAllLegalMovesFromField(
    board: ChessBoard,
    metadata: GameMetadata,
    field: ChessField
  ): List<ChessMove>
}