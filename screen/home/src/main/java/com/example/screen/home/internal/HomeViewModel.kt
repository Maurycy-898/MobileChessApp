package com.example.screen.home.internal

import androidx.lifecycle.ViewModel
import com.example.screen.home.internal.mode_card.Mode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor() : ViewModel() {

  fun onModeCardSelected(mode: Mode) {
    when(mode) {
      Mode.PlayOnline -> TODO()
      Mode.PlayWithAI -> TODO()
      Mode.Learn -> TODO()
      Mode.Puzzles -> TODO()
      Mode.AnalyzeGames -> TODO()
      Mode.News -> TODO()
    }
  }
}
