package com.example.screen.home.internal.mode_card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun ModeCardGrid(
  modifier: Modifier = Modifier,
  modes: List<Mode>,
  onCardClicked: (Mode) -> Unit = {},
) {
  LazyVerticalGrid(
    modifier = modifier,
    columns = GridCells.Fixed(2),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
  ) {
    modes.forEachIndexed { index, mode ->
      modeCardGridItem(
        index = index,
        mode = mode,
        modifier = Modifier.clickable { onCardClicked(mode) },
      )
    }
  }
}

private fun LazyGridScope.modeCardGridItem(
  index: Int,
  mode: Mode,
  modifier: Modifier = Modifier,
) {
  when {
    index % 3 == 0 -> item(span = { GridItemSpan(maxLineSpan) }) {
      ModeCard(
        mode = mode,
        modifier = modifier.height(200.dp)
      )
    }
    else -> item {
      ModeCard(
        mode = mode,
        modifier = modifier.height(180.dp)
      )
    }
  }
}

@Preview
@Composable
private fun ModeCardGridPreview() {
  Surface {
    ModeCardGrid(
      modifier = Modifier.fillMaxSize(),
      modes = Mode.entries,
    )
  }
}