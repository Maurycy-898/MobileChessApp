package com.example.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.screen.home.internal.HomeViewModel
import com.example.screen.home.internal.mode_card.Mode
import com.example.screen.home.internal.mode_card.ModeCardGrid

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
  val viewModel = viewModel<HomeViewModel>()
  Scaffold(
    modifier = modifier.statusBarsPadding(),
  ) { contentPadding ->
    HomeScreenContent(
      onModeSelected = viewModel::onModeCardSelected,
      modifier = Modifier
        .padding(contentPadding)
        .fillMaxSize()
    )
  }
}

@Composable
private fun HomeScreenContent(
  modifier: Modifier = Modifier,
  onModeSelected: (Mode) -> Unit = {},
) {
  Surface {
    ModeCardGrid(
      modes = Mode.entries,
      onCardClicked = onModeSelected,
      modifier = modifier,
    )
  }
}
