package com.mychessapp.chess.logic.internal.model

import com.mychessapp.chess.model.ChessFieldPosition
import com.mychessapp.chess.model.PlayerColor

data class GameMetadata(
  val gameStatus: GameStatus,
  val enPassantStatus: EnPassantStatus,
  val kingsStatus: MutableMap<PlayerColor, KingStatus>,
)

sealed class GameStatus {
  data class Over(val winner: PlayerColor) : GameStatus()
  data class Pending(val activeColor: PlayerColor) : GameStatus()
}

data class KingStatus(
  val position: ChessFieldPosition,
  val isAttacked: Boolean,
  val canKingsideCastle: Boolean,
  val canQueensideCastle: Boolean
)

sealed class EnPassantStatus {
  data object None : EnPassantStatus()
  data class Possible(val target: ChessFieldPosition) : EnPassantStatus()
}
