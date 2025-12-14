package com.mychessapp.chess.logic

import com.mychessapp.chess.logic.internal.model.ChessMove
import com.mychessapp.chess.logic.internal.model.GameMetadata
import com.mychessapp.chess.model.ChessBoard
import com.mychessapp.chess.model.ChessField

interface MovesGenerator {
  /**
   * Generates all legal moves for the current state of the board.
   *
   * @param board The current chess board.
   * @param metadata The game metadata containing information about the game state.
   * @return A list of all legal chess moves.
   */
  fun generateAllLegalMoves(
    board: ChessBoard,
    metadata: GameMetadata
  ): List<ChessMove>

  /**
   * Generates all legal moves for a specific field on the chess board.
   *
   * @param field The chess field from which to generate moves.
   * @param board The current chess board.
   * @param metadata The game metadata containing information about the game state.
   * @return A list of all legal chess moves from the specified field.
   */
  fun generateAllLegalMovesFromField(
    field: ChessField,
    board: ChessBoard,
    metadata: GameMetadata,
  ): List<ChessMove>
}