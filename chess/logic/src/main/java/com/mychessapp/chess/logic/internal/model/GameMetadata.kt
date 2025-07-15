package com.mychessapp.chess.logic.internal.model

import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.PlayerColor

data class GameMetadata(
  val gameStatus: GameStatus,
  val enPassantStatus: EnPassantStatus,
  val kingsPosition: Map<PlayerColor, ChessFieldPosition>,
  val castlingStatus: Map<PlayerColor, Set<CastlingData>>,
)

sealed class GameStatus {
  data class Over(val winner: PlayerColor) : GameStatus()
  data class Pending(val activeColor: PlayerColor) : GameStatus()
}

data class CastlingData(
  val rookStartingPosition: ChessFieldPosition,
  val rookTargetPosition: ChessFieldPosition,
  val kingTargetPosition: ChessFieldPosition,
)

sealed class EnPassantStatus {
  data object None : EnPassantStatus()
  data class Possible(val target: ChessFieldPosition) : EnPassantStatus()
}
