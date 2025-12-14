package com.mychessapp.chess.logic.internal.model

internal interface PieceMoveGenerator {
  /**
   * Generates a list of chess moves for a specific piece based on the provided context.
   *
   * @param context The context containing the current state of the game.
   */
  fun generate(context: MoveGeneratorContext): List<ChessMove>
}