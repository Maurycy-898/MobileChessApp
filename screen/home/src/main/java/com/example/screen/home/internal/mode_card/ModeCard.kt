package com.example.screen.home.internal.mode_card

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mychessapp.screen.home.R

@Composable
internal fun ModeCard(
  mode: Mode,
  modifier: Modifier = Modifier,
  style: TextStyle = MaterialTheme.typography.body2,
) {
  Card(
    modifier = modifier.alpha(0.96f),
    shape = RoundedCornerShape(12.dp),
    elevation = 8.dp,
    backgroundColor = MaterialTheme.colors.secondary,
    contentColor = MaterialTheme.colors.onSecondary,
  ) {
    Column {
      ModeImage(
        image = mode.image(),
        modifier = Modifier.weight(.75f)
      )
      TitleFooter(
        footerTitle = mode.title(),
        style = style,
        modifier = Modifier.weight(.25f)
      )
    }
  }
}

@Composable
private fun ModeImage(
  image: ImageBitmap,
  modifier: Modifier = Modifier,
) {
  Image(
    modifier = modifier,
    bitmap = image,
    contentScale = ContentScale.Crop,
    contentDescription = null
  )
}

@Composable
private fun TitleFooter(
  footerTitle: String,
  style: TextStyle,
  modifier: Modifier = Modifier,
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp, horizontal = 16.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = footerTitle,
      style = style,
    )
    Icon(
      imageVector = Icons.Outlined.Info,
      contentDescription = null,
    )
  }
}

@Composable
private fun Mode.image(): ImageBitmap = when (this) {
  Mode.PlayOnline -> R.drawable.play_chess
  Mode.PlayWithAI -> R.drawable.play_chess
  Mode.Learn -> R.drawable.play_chess
  Mode.Puzzles -> R.drawable.play_chess
  Mode.AnalyzeGames -> R.drawable.play_chess
  Mode.News -> R.drawable.play_chess
}.let { ImageBitmap.imageResource(it) }

@Composable
private fun Mode.title(): String = when (this) {
  Mode.PlayOnline -> R.string.mode_play_online_title
  Mode.PlayWithAI -> R.string.mode_play_ai_title
  Mode.Learn -> R.string.mode_learn_title
  Mode.Puzzles -> R.string.mode_puzzle_title
  Mode.AnalyzeGames -> R.string.mode_analyze_title
  Mode.News -> R.string.mode_news_title
}.let { stringResource(it) }

@Preview
@Composable
private fun ModeCardPreview() {
  Surface {
    ModeCard(
      mode = Mode.Learn,
      modifier = Modifier.size(300.dp, 250.dp)
    )
  }
}
