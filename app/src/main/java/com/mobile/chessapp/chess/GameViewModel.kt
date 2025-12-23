package com.mobile.chessapp.chess

import androidx.lifecycle.ViewModel
import com.mobile.chessapp.backend.game.BoardUI
import com.mobile.chessapp.backend.game.boardUtils.PlayerColor

class GameViewModel : ViewModel() {

}

data class GameUiState(
  val boardUI: BoardUI,
  val activeColor: PlayerColor,
)